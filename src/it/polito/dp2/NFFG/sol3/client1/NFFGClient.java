package it.polito.dp2.NFFG.sol3.client1;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;

import javax.ws.rs.ForbiddenException;
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

import com.sun.glass.ui.Application;

import it.polito.dp2.NFFG.FactoryConfigurationError;
import it.polito.dp2.NFFG.LinkReader;
import it.polito.dp2.NFFG.NffgReader;
import it.polito.dp2.NFFG.NffgVerifier;
import it.polito.dp2.NFFG.NffgVerifierException;
import it.polito.dp2.NFFG.NffgVerifierFactory;
import it.polito.dp2.NFFG.NodeReader;
import it.polito.dp2.NFFG.PolicyReader;
import it.polito.dp2.NFFG.ReachabilityPolicyReader;
import it.polito.dp2.NFFG.VerificationResultReader;
import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;
import it.polito.dp2.NFFG.lab3.ServiceException;
import it.polito.dp2.NFFG.lab3.UnknownNameException;
import it.polito.dp2.NFFG.sol3.service.*;
import scala.util.parsing.json.JSON;



public class NFFGClient implements it.polito.dp2.NFFG.lab3.NFFGClient {
	
	
	WebTarget target;
	NffgVerifier Nffg_V;
	Set<NffgVerifier> NffgSet = new HashSet<NffgVerifier>();
	NffgReader nffg_R;
	
	private  List<TypeNffg> Nffg;
	private Set<NffgReader> nffgset;
	private Set<PolicyReader> policyset;
	
	public NFFGClient(){
	Client client = ClientBuilder.newClient();
	// create a web target for the intended URI
	target = client.target(getBaseURI());
	//it.polito.dp2.NFFG.NffgVerifierFactory factory = NffgVerifierFactory.newInstance().newNffgVerifier();
	System.setProperty("it.polito.dp2.NFFG.NffgVerifierFactory", "it.polito.dp2.NFFG.Random.NffgVerifierFactoryImpl"); 
	try {
		Nffg_V =  NffgVerifierFactory.newInstance().newNffgVerifier();
	} catch (NffgVerifierException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 }
	
	
	private static URI getBaseURI() {
		System.out.println("in the get base url");
		if(System.getProperty("it.polito.dp2.NFFG.lab3.URL")==null){
		   System.setProperty("it.polito.dp2.NFFG.lab3.URL","http://localhost:8080/NffgService/rest/");
		}
		   return UriBuilder.fromUri(System.getProperty("it.polito.dp2.NFFG.lab3.URL")).build();
	
			// TODO Auto-generated method stub
		}

	@Override
	public void loadNFFG(String name) throws UnknownNameException, AlreadyLoadedException, ServiceException {
		System.out.println("loading single nffg");
		NffgReader nffgs = Nffg_V.getNffg(name);
		if(name == null)
		{
			System.out.println("the argument name is empty ");
			throw new ServiceException();
		}
		if(nffgs==null)
		{
			System.out.println("cant get the nffgs ");
			throw new ServiceException();
		}
		//boolean checkNffg = NffgCheck(name);
		//if(checkNffg)
		//{
			//throw new AlreadyLoadedException();
		//}
		TypeNffg nffg = new TypeNffg();
	    nffg.setName(name);
	    try {
	    	GregorianCalendar gc= new GregorianCalendar();
	    	gc.setTimeInMillis(System.currentTimeMillis());
			nffg.setUpdatetime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  //  Set<NodeReader> nodes= nffg_R.getNodes();
	  //  List<TypeNode> myNodeList = new ArrayList<TypeNode>();
	    for(NodeReader nr:nffgs.getNodes())
	    {
	    	  TypeNode tn= new TypeNode();
	    	  tn.setName(nr.getName());
	    	  //tn.setType(n);
	    	
	    	 Set<LinkReader> links= nr.getLinks();
		     for(LinkReader lr:links)
			    {
			    	TypeLink tl= new TypeLink();
			    	tl.setName(lr.getName());
			    	tl.setSource(lr.getSourceNode().getName());
			    	tl.setDestination(lr.getDestinationNode().getName());
					tn.getLink().add(tl);
			    }
			    nffg.getNode().add(tn);
			    performnffg(nffg);

	    }
	   // Nffg.add(nffg);
		System.out.println("nffg loaded ");

	
	}
	@Override
	public void loadAll() throws AlreadyLoadedException, ServiceException {
		   //System.out.println("loading all nffgs");
		    	    
		    nffgset = Nffg_V.getNffgs();
		    int i=0;
		    System.out.println("generated nffg size: "+nffgset.size());

			for(NffgReader nf:nffgset)	
			{		//check if  Nffg exists do if not null
				TypeNffg nffg = new TypeNffg();	
			    System.out.println("client1: generated Nffgname: "+nf.getName()+"generated node size: "+nf.getNodes().size());
				if(nf.getName()!=null)
				{
					String nffgname ;
					nffgname= nf.getName();
					i++;
					boolean isNffg= NffgExist(nffgname);
					if(isNffg)
					{
						System.out.println("nffg  "+nffgname+"  already exists ");
						throw new AlreadyLoadedException();
					}					
				    nffg.setName(nf.getName());
				   try {
						SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
						sd.setTimeZone(TimeZone.getTimeZone("GMT"));
						String date = sd.format(new Date());						
						XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
						nffg.setUpdatetime(xmlCal);
						} catch (DatatypeConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//start setting the nodes
				    int k=0;
				    for(NodeReader nr:nf.getNodes())
				    {
						k++;
					    TypeNode tn= new TypeNode();
						System.out.println("inside node loop :  "+k+"   the generated  link size "+nr.getLinks().size());
						
				    	  tn.setName(nr.getName());
				    	  tn.setType(nr.getFuncType().name());
				    	  
				    	  Set<LinkReader> links= nr.getLinks();
					
				    	 List<TypeLink> myLinkList = new ArrayList<TypeLink>();
 
					      for(LinkReader lr:links)
						    {
					    	  	//System.out.println("inside link loop");
					          TypeLink tl= new TypeLink();
						    	tl.setName(lr.getName());
						    	tl.setSource(lr.getSourceNode().getName());
						    	tl.setDestination(lr.getDestinationNode().getName());
						    	//myLinkList.add(tl);
						    	//System.out.println("done loading links for node"+tn.getName());
								tn.getLink().add(tl);
						    }
					     
					      //tn.getLink().addAll(myLinkList);
			//				System.out.println("inside node loop :  "+k+"   the after generated  link size "+tn.getLink().size());

						    nffg.getNode().add(tn);
					      
				    }			
				      System.out.println("done loading nodes for nffg"+nffg.getName());
					performnffg(nffg);
					System.out.println("client1:  the "+i+"th  naffg is "+nffgname + "and its node size is "+nffg.getNode().size());
					
					}
				
			}
//		    System.out.println("generated nffg size: "+nffgset.size());

			System.out.print("don performing the nffgs starting policy . . . .the nffg size is "+nffgset.size());
	        /////////**************** policy *************////
			String policyname;			
			policyset = Nffg_V.getPolicies();
			for(PolicyReader po:policyset)
			{
				if(po!=null)
				{
							TypePolicy poli = new TypePolicy();	
							poli.setIsPosetive(po.isPositive());
							poli.setName(po.getName());
							poli.setNffgname(po.getNffg().getName());
							//add to  reachability 
							TypeReachPolicy reacho = new TypeReachPolicy();
							reacho.setDestination(((ReachabilityPolicyReader) po).getDestinationNode().getName());
							reacho.setSource(((ReachabilityPolicyReader) po).getSourceNode().getName());
							//add to nffgresult 
							TypeNffgResult result = new TypeNffgResult();
							VerificationResultReader readerver = po.getResult();
							if(readerver!=null)
							{
								result.setMessage(readerver.getVerificationResultMsg());
								result.setResult(readerver.getVerificationResult());				
								GregorianCalendar gc= new GregorianCalendar();
						    	gc.setTimeInMillis(System.currentTimeMillis());
								try {
									result.setVerifierTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
								} catch (DatatypeConfigurationException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							poli.setNffgResult(result);
							poli.setReachPolicy(reacho);
							boolean checkpolicy = PolicyCheck(po.getName());
							if(checkpolicy)
							{//if already exist perform put else perform post
								//System.out.println("policy found doing put . . . ");
							    policyname = putpolicy(poli);	
								if(policyname==null)
								throw new ServiceException();
							}
							else
							{
								//System.out.println("the policy is new performing post . . . ");		
								policyname= postpolicy(poli);//this will update if already exit
							
							}
				}//end of if !null policy
			    else{System.out.println("null policy ");}						
			}//end of policy reader
			System.out.println("done performing policy thing. the size "+policyset.size());	
	}
	
	@Override
	public void loadReachabilityPolicy(String name, String nffgName, boolean isPositive, String srcNodeName,
			String dstNodeName) throws UnknownNameException, ServiceException {
		/*Loads a new reachability policy into the remote service given the policy properties.
		 * If a policy with the given name already exists in the service, the new policy substitutes
		 * the old one. The new policy is uploaded without a verification result.
		 * @throws UnknownNameException	if nffgName is not the name of a known NFFG, or srcNodeName and dstNodeName are not both nodes belonging to the known NFFG named nffgName.
		*/
		System.out.println("loadReachability - source "+srcNodeName);
		System.out.println("loadReachability - destination "+dstNodeName);
		
		System.out.println("inside clint1: load rachability");
		int flagsource=0;
		int flagdestination =0;
		boolean isNffg= NffgExist(nffgName);
		if(!isNffg) throw new UnknownNameException("the given nffg cant be found"); 			
		TypeNffg mynffg =getnffg(nffgName);
		if(mynffg==null) throw new UnknownNameException("the nffg named nffg cant be found");			
		
		List<TypeNode> nodelist = new ArrayList<TypeNode>();
		nodelist=mynffg.getNode();
		for(TypeNode mynode:nodelist)
		{		if(mynode.getName().equals(srcNodeName)) flagsource=1;
				if(mynode.getName().equals(dstNodeName)) flagdestination=1;
		}
		if(flagsource==0||flagdestination==0) throw new UnknownNameException("source or destination node not found");
		
		//boolean checkpolicy = PolicyCheck(name);
		int i=0;
		String policyname;
		/*TypePolicy newpo = getpolicy(name);
		if(newpo!=null)
		{//policy found set for updating
			i++;
			TypeReachPolicy richo = new TypeReachPolicy();
			TypePolicy mypolicy = new TypePolicy();		
			System.out.println("LoadUnload - source "+newpo.getReachPolicy().getSource());
			System.out.println("LoadUnload - destination "+newpo.getReachPolicy().getDestination());
			richo.setDestination(newpo.getReachPolicy().getDestination());
			richo.setSource(newpo.getReachPolicy().getSource());				
			mypolicy.setName(name);
			mypolicy.setIsPosetive(isPositive);
			mypolicy.setReachPolicy(richo);		
			mypolicy.setNffgname(nffgName);
			policyname = putpolicy(mypolicy);
			if(policyname==null) throw new ServiceException("something got wrong while updating the new policy");
			System.out.println("client1: policy updated for the  "+i+"th  times. policy name  "+policyname);
		}	
		else
		{
			i++; */
		TypeReachPolicy richo = new TypeReachPolicy();
		TypePolicy mypolicy = new TypePolicy();		
		richo.setDestination(dstNodeName);
		richo.setSource(srcNodeName);		
		mypolicy.setName(name);
		mypolicy.setIsPosetive(isPositive);
		mypolicy.setReachPolicy(richo);		
		mypolicy.setNffgname(nffgName);
		policyname = postpolicy(mypolicy);
		if(policyname==null) throw new ServiceException("something got wrong while posting the new policy");
		System.out.println("client1: new policy posted for the  "+i+"th  times. policy name  "+policyname);
		//}
	}
	
	@Override
	public void unloadReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
//  Unloads the reachability policy with a given name from the remote service
		System.out.println("client1: inside unloadreachability ");
		if(name == null) throw new ServiceException("the policy name argument is null");
		String policyname;
		boolean checkpolicy = PolicyCheck(name);
		if(!checkpolicy) throw new UnknownNameException("couldnt find the policy ");			
		//delete the policy with a given name 
		System.out.println("client2: inside unload reach . . policy found poli name "+name);		
		policyname = removepolicy(name);
		if(policyname==null) throw new ServiceException("somthing got wrong while removing");
		}
	@Override
	public boolean testReachabilityPolicy(String name) throws UnknownNameException, ServiceException {
		//	 * Asks the service to test one of the previously uploaded reachability policies 
		System.out.println("inside test reachability");
		boolean checkpolicy = PolicyCheck(name);
		if(!checkpolicy)
		{
			System.out.println("the policy doesnt exist");
			throw new UnknownNameException();
		}
		//first get the given policy 
		TypePolicy mypolicy = new TypePolicy();
		mypolicy= getpolicy(name);
		/*if(mypolicy==null)
		{
			System.out.println("cant get the policy");
			throw new ServiceException();
		}*/
		/*TypeReachPolicy richo = new TypeReachPolicy();
		richo.setDestination(mypolicy.getReachPolicy().getDestination());
		richo.setSource(mypolicy.getReachPolicy().getSource());
		boolean result = gettest(richo);*/
		boolean result = gettest(mypolicy);
		
		
		return result;
		
	}
	

	private boolean gettest(TypePolicy mypolicy) {
		boolean result;
		System.out.println("inside gettest");
		//if(mypolicy.getReachPolicy()!=null){
			System.out.println(" client1: gettest source  "+mypolicy.getReachPolicy().getSource()+"  destination  "+mypolicy.getReachPolicy().getDestination());		
			TypePolicy response = target.path("Policy")
	
	                .request("application/json")
	                .accept("application/json")
	                .put(Entity.entity(mypolicy,"application/json"),TypePolicy.class);
			 System.out.println("verifiy policy response received");
			 
			 if(response.getNffgResult()!=null) result=true;
			 else result = false;
		//}else return false;
		return result;
	}
	private boolean NffgExist(String nffgname) {
		//System.out.println("getting the known Nffg for checking ");
		//System.out.println(" --- client1 NffgExist:  Performing a GET Nffg by name --- ");
		TypeNffg response = target.path("Nffg")
								    .path(nffgname)								  
								    .request()
					                .accept(MediaType.APPLICATION_JSON)
					                .get(TypeNffg.class);
		  System.out.println(" ---Nffg getted--- ");
		if(response!=null)
		{
			return true; 
		}				
		return false;
	}


	private TypePolicy getpolicy(String name) {
		//System.out.println(" --- Performing a GET policy by name --- ");
		TypePolicy response = target.path("Policy")
								    .path(name)								  
								    .request()
					                .accept(MediaType.APPLICATION_JSON)
					                .get(TypePolicy.class);
		  //System.out.println(" ---policy getted--- ");
		  return response;		
	}
	private String removepolicy(String name) {
		System.out.println("--- removing policy --- name "+name);
		String policyname;
		  TypePolicy response = target.path("Policy")
									   .path(name)
									   .request()
									   .accept(MediaType.APPLICATION_JSON)
									   .delete(TypePolicy.class);
		System.out.println();
		System.out.println("--- Response of Delete received --- \n");
		policyname= response.getName();
		return policyname;
		}
	private void performnffg(TypeNffg nffg2) throws AlreadyLoadedException, ServiceException {
		System.out.println("posting nffgs named"+nffg2.getName()+"   and its node size is  "+nffg2.getNode().size());		
		String nffgname = nffg2.getName() ;
		if(nffgname==null) throw new ServiceException("the nffg arguement is null");			
		//System.out.println("the name of the nffg is " + nffgname);
		TypeNffg response = target.path("Nffg")
									.request("application/json")
									.accept("application/json")
									.post(Entity.entity(nffg2,"application/json"),TypeNffg.class);
		System.out.println("nffg posted");
		System.out.println("client1: performnffg: the  naffg is "+response.getName() + "and its node size is "+response.getNode().size());
		
		if(response!=null)
		{
		nffgname = response.getName();
		//System.out.println("client: created nffg"+nffgname);
		}
		//return nffgname;

	}
	private String putpolicy(TypePolicy poli) {
		String policyname =poli.getName();
		//System.out.println(" client1: the policy to  be updated is  "+policyname);

		TypePolicy mypolicy = target.path("Policy")
				                .path(policyname)
				 				.request(MediaType.APPLICATION_JSON)
				 				.accept(MediaType.APPLICATION_JSON)
				 				.put(Entity.entity(poli,MediaType.APPLICATION_JSON),TypePolicy.class);
		 policyname = mypolicy.getName();
		 //System.out.println("---client1:  Put on policy  "+policyname+"  performed --- \n");
		 return policyname;
		
	}
	private String postpolicy(TypePolicy poli) {
		//System.out.println("client1: posting policy ");
		String policyname =poli.getName();
		//System.out.println("the policy to be posted is  "+policyname);
		TypePolicy response = target.path("Policy")
                  .request("application/json")
                  .accept("application/json")
                  .post(Entity.entity(poli,"application/json"),TypePolicy.class);
		//System.out.println("policy posted");
		policyname = response.getName();
		 //System.out.println("---client1: post performed on policy  "+policyname);
		return policyname;
	}
	private boolean PolicyCheck(String name) {		
		//System.out.println(" --- Performing a GET policy by name --- ");
		TypePolicy response = target.path("Policy")
								    .path(name)								  
								    .request()
					                .accept(MediaType.APPLICATION_JSON)
					                .get(TypePolicy.class);
		 // System.out.println("client1:  ---policy getted--- ");
		if(response!=null)
		{
			return true; 
		}				
		return false;
	}
	
	private TypeNffg getnffg(String nffgName) {		
		//System.out.println(" --- client1 getnffg:  Performing a GET Nffg by name --- ");
		TypeNffg response = target.path("Nffg")
								    .path(nffgName)								  
								    .request()
					                .accept(MediaType.APPLICATION_JSON)
					                .get(TypeNffg.class);
		 // System.out.println(" ---getnffg: Nffg getted--- ");
		  return response;		
	}
	 public static XMLGregorianCalendar toXMLGregorian(Calendar calendar)
			  throws DatatypeConfigurationException {
			  GregorianCalendar gcal = new GregorianCalendar();
			  gcal.setTimeInMillis(calendar.getTimeInMillis());
			 XMLGregorianCalendar xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
			 return xcal; }

	
}
