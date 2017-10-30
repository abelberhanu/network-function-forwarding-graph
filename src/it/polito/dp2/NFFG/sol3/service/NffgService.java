package it.polito.dp2.NFFG.sol3.service;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.Static;

import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.lab2.ServiceException;
import it.polito.dp2.NFFG.lab2.UnknownNameException;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;

/*
 * class that manages nffg and policy objects
 */
public class NffgService {
		 
		 WebTarget target;
		 NffgVerifier Nffg_V;
		 NffgReader nffg_R;
		 public  static Map<String,TypeNffg> MapNffg = NffgDB.getMapNffg();
		 public  static Map<String,TypePolicy> MapPolicy = policyDB.getMapPolicy();
		
		public NffgService()
		{		
			Client client = ClientBuilder.newClient();
			// create a web target for the intended URI
			target = client.target(getBaseURI());
			
		}
		private URI getBaseURI() {
			
			   // return UriBuilder.fromUri(System.getProperty("it.polito.dp2.NFFG.lab2.URL")).build();
					return UriBuilder.fromUri("http://localhost:8080/Neo4JXML/rest/").build();
		
				// TODO Auto-generated method stub
			}
		public List<TypeNffg> getNffg() {
			return new ArrayList<TypeNffg>(MapNffg.values());
		}
		@SuppressWarnings("unused")
		public TypeNffg getSingleNffg(String name) {
			if(name == null) return null;
			System.out.println("getting single nffg from the map named "+name);
			/* TypeNffg MyNffg =MapNffg.get(name);
			 
		System.out.println("the getted nffg is "+MyNffg.getName());
			if (MyNffg==null) return null;	
			*/
			return MapNffg.get(name);
		}
		public TypeNffg creatNffg(TypeNffg nffg) throws DatatypeConfigurationException, AlreadyLoadedException  {
			System.out.println("Inside service: creating nffg named " +nffg.getName());
			if(nffg==null) return null;		
			for(TypeNffg tn:MapNffg.values()){
				if(nffg.getName().equals(tn.getName())){
					throw new AlreadyLoadedException();
				}
			}
			
			System.out.println("inside service: the nffg is new adding to neo4jxml");
			nffg.setUpdatetime(getXMLGregorianCalendarNow());
			TypeNffg newnffg = addN2Neo(nffg);
			if(newnffg==null)
			{
				System.out.print("problem adding to neo4jxml. throwing . . .");
				throw new DatatypeConfigurationException();
			}
			MapNffg.put(nffg.getName(), nffg);
			
			// if (nffg==null) return null;
				System.out.println("Service: the created nfg is "+ nffg.getName());
			 //TypeNffg MyNffg2=MapNffg.put(nffg.getName(), nffg);
			return nffg;
		}
		private TypeNffg addN2Neo(TypeNffg nffg) {
			System.out.println("inside service neo: adding to neo4j  nffg  " +nffg.getName());
			String node_id;			 
			// TypeNode mynode = new TypeNode();			
			Property prop = new Property();
			Labels lab = new Labels();
			Node node = new Node();
			prop.setName("name");
			lab.getValue().add("Nffg");
			node.setLabels(lab);
			prop.setValue(nffg.getName());
			node.getProperty().add(prop);
			node_id = performnode(node);
			
			//CopyOnWriteArrayList<TypeNode> node_L = new CopyOnWriteArrayList<>();
			//node_L.addAll(nffg.getNode());
			
			List<TypeNode> node_L = nffg.getNode();
			for(TypeNode ng:node_L)
			{
				String sourceN_id;
				Property sourcepro = new Property();
				Node sourcenode = new Node();
				sourcepro.setName("name");
				sourcepro.setValue(ng.getName());
				sourcenode.getProperty().add(prop);
				sourceN_id= performnode(sourcenode);
				//created the source node. now set the relationship with belong 
				//check if necessary 
				Relationship relation = new Relationship();
				relation.setDstNode(node_id);
				relation.setSrcNode(sourceN_id);
				relation.setType("belong");
				createRelation(relation);
				if (node_id ==null)
				{
					System.out.println("the node is null returning null");
					return null;
					//createNode(node);
				}
					for(TypeLink lr:ng.getLink())
					{
						String src_id,dest_id;
						Property src_prop = new Property();
						Property dest_prop = new Property();
						//Relationship relation = new Relationship();
						Node src = new Node();
						Node dest = new Node();
						//set the source and get its id 
						src_prop.setName("name");
						src_prop.setValue(lr.getSource());
						src.getProperty().add(src_prop);
						
						src_id=performnode(src);

						//check if the source node already exist. if not created, then create it else get the node
						if (src_id ==null)
						{									
							//System.out.println("the source id is null ");
							return null;
						}
						System.out.println("the source id is " + src_id);									
						
						dest_prop.setName("name");
						dest_prop.setValue(lr.getDestination());
						dest.getProperty().add(dest_prop);
						dest_id = performnode(dest);
						if(dest_id==null)
						{
							//System.out.println("the destination id is null ");									
							return null;
							//createNode(node);
						}
						System.out.println("the destination id is " + dest_id);									
						
						relation.setDstNode(dest_id);
						relation.setSrcNode(src_id);
						relation.setType("Link");
						createRelation(relation);
					}//end of link for loop				
			}//end of node loop
			System.out.println("done adding to neo");
			return nffg;
		}
		public TypePolicy getSinglePolicy(String name) {
			
			if (name== null) return null;
			return MapPolicy.get(name);
			
		}
		public List<TypePolicy> getPolicies() {
			return new ArrayList<TypePolicy>(MapPolicy.values());
		}
	
		
		private void createRelation(it.polito.dp2.NFFG.sol3.service.Relationship relation) {
			// TODO Auto-generated method stub
			 System.out.println("performing relationship  ");
	
			 Relationship response = target.path("resource")
					 				.path("node")
					 				.path(relation.getSrcNode())
					 				.path("relationship")
					 				
	                   .request(MediaType.APPLICATION_XML)
	                   .post(Entity.entity(relation,MediaType.APPLICATION_XML),Relationship.class);
			 System.out.println("relationship created ");
				System.out.println("the source id is " + response.getSrcNode());
				System.out.println("the dest id is " + response.getDstNode());
				
			
		}
		private String performnode(it.polito.dp2.NFFG.sol3.service.Node node) {
			String n_id=null;
			List<Node> node_list= new ArrayList<Node>();
			node_list=getAllNodes();
			for(Node n:node_list)
			{
				if(n.getProperty().get(0).getValue().equals(node.getProperty().get(0).getValue()))
						{
							n_id=n.getId();
						}			
			}
			if(n_id==null)
			{
				 System.out.println();
				    System.out.println("--- Performing a Post --- \n");
			    	System.out.println(" node name " + node.getProperty().get(0));
				  Node node_resp = target.path("resource")
						  				.path("node")
				    		             .request(MediaType.APPLICATION_XML)
				    		              .post(Entity.entity(node,MediaType.APPLICATION_XML),Node.class);
			    	System.out.println();
			    	n_id=node_resp.getId();
				
			}
			
			return n_id;			
		}
		public TypePolicy createdPolicy(TypePolicy policy) {
			 if (policy==null) return null;
			 String poliname = policy.getName();
			 System.out.println("inside service: the policy name is "+poliname);			 
			 MapPolicy.put(poliname, policy);
			 System.out.println("inside service: the policy name "+poliname+"is created to the map");
			 return policy;
		}		
		public TypePolicy remove(String name) {
			System.out.println("inside service: removing ");
			if(name==null) return null;
			TypePolicy policy2remove= getSinglePolicy(name);
			if(policy2remove!=null)
			{
			 MapPolicy.remove(name);
			 System.out.println("inside service: removing policy removed");
			 return policy2remove;
			}
			else return null;
			
		}
		private List<Node> getAllNodes() {
			//	System.out.println();
			    System.out.println(" ---inside service: Performing a GET all node--- ");
			    List<Node> node_resp       =         target.path("resource")
															.path("nodes")
															.request(MediaType.APPLICATION_XML)
			    									 .get(new GenericType<List<Node>>() {});
			    System.out.println(" --- Response of GET received --- ");	
			    return node_resp;
			}
		 public XMLGregorianCalendar getXMLGregorianCalendarNow() 
		            throws DatatypeConfigurationException
		    {
		        GregorianCalendar gregorianCalendar = new GregorianCalendar();
		        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		        XMLGregorianCalendar now = 
		            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
		        return now;
		    }
		public TypePolicy putpolicy(TypePolicy policy2b) {
			if(policy2b==null) return null;
			return MapPolicy.replace(policy2b.getName(), policy2b);
		}
		public boolean testReachability(String src, String dst) throws ServiceException, UnknownNameException {
			
			List<Node> node_list = new ArrayList<Node>();
			List<Path> path_list = new ArrayList<Path>();
			node_list= getAllNodes();
			if (node_list==null)
			{
				//UnknownNameException unex = new UnknownNameException();
				ServiceException srex = new ServiceException();
				throw srex;
			}
			Property prop = new Property();
			Property prop_dest = new Property();
			Node src_n = new Node();
			Node dest_n = new Node();
			int flag1=0,flag2=0;
			String src_id = null,dest_id = null;
			
			//set source node, perform  and get the id 
			prop.setName("name");
			prop.setValue(src);
			src_n.getProperty().add(prop);
			System.out.println("1 source is before updated "+src_n.getProperty().get(0).getValue());
			
			prop_dest.setName("name");
			prop_dest.setValue(dst);
			dest_n.getProperty().add(prop_dest);
			System.out.println("1 source is   "+src_n.getProperty().get(0).getValue());
			System.out.println(" and the dest id is    "+dest_n.getProperty().get(0).getValue() );
			
						
			for (Node node_r:node_list)
			{
				if(node_r.getProperty().get(0).getValue().equals(src_n.getProperty().get(0).getValue()))
				{
					flag1=1;
					src_id=node_r.getId();
				}
				if(node_r.getProperty().get(0).getValue().equals(dest_n.getProperty().get(0).getValue()))
					
				{
					flag2=1;
					dest_id=node_r.getId();
				}
			}
			System.out.println("before comparison.   the source is   "+src_id + " and the dest id is    "+dest_id);
			
			if(flag1==0||flag2==0)
			{//one of the given node is not found 
				UnknownNameException unex = new UnknownNameException();
				//ServiceException srex = new ServiceException();
				System.out.println("one of the nodes can not be found");
				throw unex;
			}
			System.out.println("the nodes found");
			//get list of paths b/n them (queryparam).relation and node 
			path_list=getAllPath(src_id,dest_id);
			System.out.println("after the get all path");
			
			for (Path p:path_list)
			{
				System.out.println("i am in the path");				
				if(p.getRelationship().isEmpty()&&p.getNode().isEmpty())
				{

				}
				else
				{
					System.out.println("found link berween the source and destination ");

				   return true;
				}
			}
			System.out.println("could not find any link berween the source and destination ");
			
			return false;
			
		}
		private List<Path> getAllPath(String src_id, String dest_id) {
	    	//System.out.println();
		    System.out.println(" --- getting all path- src id ="+ src_id + "dest id "+dest_id);
		    
		    List<Path> path_resp       =         target.path("resource")
														.path("node")
														.path(src_id)
														.path("paths")
														.queryParam("dst",dest_id)
														.request(MediaType.APPLICATION_XML)
		    									 .get(new GenericType<List<Path>>() {});
	    //	System.out.println();
		    System.out.println(" --- Response of GET received --- ");	
		    return path_resp;
		}
		
}
