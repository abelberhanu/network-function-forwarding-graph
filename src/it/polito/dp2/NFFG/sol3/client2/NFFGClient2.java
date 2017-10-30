package it.polito.dp2.NFFG.sol3.client2;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.NFFG.FunctionalType;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.TraversalPolicyReader;
import it.polito.dp2.NFFG.sol3.service.TypeLink;
import it.polito.dp2.NFFG.sol3.service.TypeNffg;
import it.polito.dp2.NFFG.sol3.service.TypeNode;
import it.polito.dp2.NFFG.sol3.service.TypePolicy;
import it.polito.dp2.NFFG.sol3.service.TypeReachPolicy;

public class NFFGClient2 implements it.polito.dp2.NFFG.NffgVerifier {

	WebTarget target;	
	
	 public static Set<MyNffg>   NffgList = new HashSet<MyNffg>();
	 public static Set<MyReachPolicy> PolicyList = new HashSet<MyReachPolicy>();
	 DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm z");
	 
	 /**
	  * fills the Nffg  list and policy list . after parsing the XML file .it gets 
	  * the XML  file to be parsed from the system property.
	  * @throws NffgVerifierException
	  */	
	  public NFFGClient2() throws NffgVerifierException, ParseException 
	 {
		  Client client = ClientBuilder.newClient();
			// create a web target for the intended URI
			target = client.target(getBaseURI());
		  List<TypeNffg> nffglist = new ArrayList<>();
		  List<TypePolicy> po = new ArrayList<>();
		  nffglist = getallnffg();
		  po = getallpolicy();
		System.out.println("client2: the getted policy  size is "+po.size());	
		
		
	NffgList.clear();	  
		  Set<NffgReader> nffg_r = new HashSet<NffgReader>();
			
		  for(TypeNffg tnffg:nffglist)
		  {		
		System.out.println("client2: nffg  name is . . ."+tnffg.getName()+"and the its node size is (before) "+tnffg.getNode().size());					 			  
			  MyNffg mynffg =null;
			  List<TypeNode> Nodelist= tnffg.getNode();
			  Set<NodeReader> node_r = new HashSet<NodeReader>();
			  for(TypeNode tnode:Nodelist)
			  {
				 String name= tnode.getName();
				 // System.out.println("node name is . . ."+name);
				 FunctionalType func= FunctionalType.valueOf(tnode.getType()) ;
				 Set<LinkReader> lr= new HashSet<LinkReader>();
				 // List<TypeLink> myLinklist=tnode.getLink();
				 Mynode nodes = null;
				 for(TypeLink tl:tnode.getLink())
				 {
					 String linkname= tl.getName();
					 Mynode source = new Mynode (tl.getSource());
					 Mynode destination= new Mynode(tl.getDestination());
					 Mylink link= new Mylink(linkname,source,destination);
					 lr.add(link);					 
					 //nodes=new Mynode(name,func,lr);
				 }
				// System.out.println("client2 :node name is . . ."+name);
				
				 nodes=new Mynode(name,func,lr);
				 node_r.add(nodes);
			  }
			// System.out.println("client2 after parsing the nodes :the nffg name "+tnffg.getName()+" the node size is  . . ."+node_r.size());

			 mynffg=new MyNffg(tnffg.getName(), tnffg.getUpdatetime().toGregorianCalendar(),node_r);
			// nffg_r.add(mynffg);
			 NffgList.add(mynffg);
			 System.out.println("client2 after parsing the nodes :the nffg name    "+mynffg.getName()+"     the node size is  . . ."+mynffg.getNodes().size());
			 
		  }		     
		  PolicyList.clear();
		  
		  int i=0;
		  for(TypePolicy policy_r:po)
		  {
			  //policy
				  if(policy_r!=null)
				  {
					  i++;
						 // System.out.println("the  "+i+"th   not null policy is "+policy_r.getName());	  
						  String pname= policy_r.getName();
						  MyNffg nfname= new MyNffg(policy_r.getNffgname());
						  Boolean ispos = policy_r.isIsPosetive();
						  TypeReachPolicy treach= new TypeReachPolicy();
						  
						  Mynode src=  new Mynode( policy_r.getReachPolicy().getSource()) ;
						  Mynode dest=  new Mynode(policy_r.getReachPolicy().getDestination()) ;
						 if(policy_r.getNffgResult()!=null)
						  {
							  //System.out.println("the nffg result is not null");
								  Boolean result=policy_r.getNffgResult().isResult();
								  String msg= policy_r.getNffgResult().getMessage();
								  XMLGregorianCalendar vTime= policy_r.getNffgResult().getVerifierTime();
								  if(result!=null && msg!=null && vTime!=null)
									  {
											      MyPolicy pol= new MyPolicy(pname);
												  Myresult verifier= new Myresult(pol,result,msg,vTime.toGregorianCalendar());
												  MyReachPolicy mp= new MyReachPolicy(pname,ispos,nfname,verifier,src,dest);
												  PolicyList.add(mp);										
									  }
								  else
								  { 
										  		  MyReachPolicy mp= new MyReachPolicy(pname,ispos,nfname,src,dest);
												  PolicyList.add(mp);
									  
									  
								  }
						  }
						 else
						 {
							  MyReachPolicy mp= new MyReachPolicy(pname,ispos,nfname,src,dest);
							  PolicyList.add(mp);
						 }
			  
			  }
			  else
			  {
				  System.out.println("policy is null");	  

				  
			  }
		  }
	System.out.println("client2: parsed policy size :"+PolicyList.size());			 		  
		
	 }
	  private URI getBaseURI() {
		  System.out.println("in the get base url");
			if(System.getProperty("it.polito.dp2.NFFG.lab3.URL")==null){
			   System.setProperty("it.polito.dp2.NFFG.lab3.URL","http://localhost:8080/NffgService/rest/");
			}
			   return UriBuilder.fromUri(System.getProperty("it.polito.dp2.NFFG.lab3.URL")).build();
		
		}
	private List<TypePolicy> getallpolicy() {
		  //System.out.println(" --- client2: getting all poicies --- ");
		    List<TypePolicy> Response = target.path("Policy")
		    								.request().accept(MediaType.APPLICATION_JSON)
		    								.get(new GenericType<List<TypePolicy>>() {});
	    	
		   // System.out.println(" ---client2: Response of GET  policy received --- ");
		return Response;
	}

	private List<TypeNffg> getallnffg() {
		 //System.out.println(" - client 2: getting all nffgs --- ");
		    List<TypeNffg> Response = target.path("Nffg")
		    								.request()
		    								.accept(MediaType.APPLICATION_JSON)
		    								.get(new GenericType<List<TypeNffg>>() {});
	    	
		   // System.out.println(" --- client2: Response of GET  nffg received --- ");
		return Response;
	}

	@Override
	public NffgReader getNffg(String arg0) {
		NffgReader nff = null;
		for(MyNffg mynf:NffgList)
		{
			if(mynf.getName().equals(arg0))
			{
				nff = mynf;
				System.out.println("client2: (with string) nffg  name is . . ."+nff.getName()+"and its node size is (from getnffg) "+nff.getNodes().size());				
			}
		}
	
		return nff;
	}

	@Override
	public Set<NffgReader> getNffgs() {
		 Set<NffgReader> nr = new HashSet<NffgReader>();
		 if(NffgList.isEmpty()) return null;
	      for(MyNffg ng:NffgList)
			{					 			  
	    	  nr.add(ng);
			System.out.println("client2: (without string) nffg  name is . . ."+ng.getName()+"and its node size is (from getnffg) "+ng.getNodes().size());				
	    	  
			}  
	      
			return nr;
	}

	@Override
	public Set<PolicyReader> getPolicies() {
		Set<PolicyReader> pr = new HashSet<PolicyReader>();
		for (MyReachPolicy po:PolicyList)
		{
			pr.add(po);
			
		}
		  System.out.println("client2 getpolicies(wihtout string ): policy size "+pr.size());	  

		// TODO Auto-generated method stub
		return pr;
	}

	@Override
	public Set<PolicyReader> getPolicies(String arg0) {
		System.out.println("client2 getpolicies(wiht string ): arguement name "+arg0);
		Set<PolicyReader> pr = new HashSet<PolicyReader>();
		for(MyReachPolicy po:PolicyList)
		{		
			//System.out.println("client2: inside get poli policy name  "+po.getName());
			//System.out.println("client2:nffg  "+po.getNffg().getName());
			if(po.getNffg().getName().equals(arg0))
			{
				//System.out.println("client2 getpolicies inside if");
				pr.add((PolicyReader) po);
			}
			
		}
		System.out.println("client2 getpolicies(wiht string ): policy size "+pr.size());
		// TODO Auto-generated method stub
		return pr;
	}

	@Override
	public Set<PolicyReader> getPolicies(Calendar arg0) {
		Set<PolicyReader> pr = new HashSet<PolicyReader>();
		for(MyReachPolicy po:PolicyList)
		{
			if(po.getResult().getVerificationTime().equals(arg0))
			{
				pr.add((PolicyReader) po);
			}
			
		}
		System.out.println("client2 getpolicies(calander ): policy size "+pr.size());
		// TODO Auto-generated method stub
		return pr;
	}

}
