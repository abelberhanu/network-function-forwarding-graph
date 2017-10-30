package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.datatype.DatatypeConfigurationException;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import it.polito.dp2.NFFG.lab2.ServiceException;
import it.polito.dp2.NFFG.lab2.UnknownNameException;


/** Resource class hosted at the URI relative path "/NffgService"
 */
@Path("/Policy")
@Api(value = "/Policy", description = "a collection of policy objects")
public class PolicyRsource {
	  /**************** for policy*******************
	   * including remove 
	   */
	    NffgService service = new NffgService();	  
	  	@GET 
		@ApiOperation(	value = "get the policy objects", notes = "xml and json formats")
		@ApiResponses(value = {
				@ApiResponse(code = 200, message = "OK"),
				@ApiResponse(code = 500, message = "Internal Server Error")})
		@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		  public List<TypePolicy> getpolicy()
		  {
			  List<TypePolicy> policylist = service.getPolicies();
			  return policylist;
		  }		
		 @GET 
		 @Path("{name}")
		 @ApiOperation(	value = "get a single policy object", notes = "json and xml formats")
		 @ApiResponses(value = {
		    		@ApiResponse(code = 200, message = "OK"),
		    		@ApiResponse(code = 404, message = "Not Found"),
		    		@ApiResponse(code = 500, message = "Internal Server Error")})
		    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    public TypePolicy getSinglePolicy(@PathParam("name") String name) {
			System.out.println("Inside get by name method inside resourse");
			System.out.println("resourse: policy name is "+name);
     		TypePolicy MyPolicy = service.getSinglePolicy(name);
		    return MyPolicy;
		    }
		 
		  @POST
		  @ApiOperation(	value = "create a new policy object", notes = "json and xml formats"
			)
		    @ApiResponses(value = {
		    		@ApiResponse(code = 201, message = "Created"),
		    		@ApiResponse(code = 403, message = "Forbidden because policy failed"),
		    		@ApiResponse(code = 500, message = "Internal Server Error but resource . .")})
		    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		  public Response postPolicy(TypePolicy policy, @Context UriInfo uriInfo)
		  {
			  System.out.println("Inside post method inside resourse");
			  System.out.println("resourse: policy name is "+policy.getName());
			  TypePolicy created = service.createdPolicy(policy);
			  if (created != null) { // success
		        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		        	URI u = builder.path(created.getName()).build();
		        	return Response.created(u).entity(created).build();
		    	} else
		    	{
					  System.out.println("Inside resource post: couldnt create policy  ");
		    		throw new ForbiddenException("Forbidden because policy failed");
		    	}
		       
		  }
		  @PUT
		    @Path("{poliname}")
		    @ApiOperation(	value = "update a Policy object", notes = "json and xml formats"
			)
		    @ApiResponses(value = {
		    		@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 403, message = "Forbidden because negotiation failed"),
					@ApiResponse(code = 404, message = "Not found"),
		    		@ApiResponse(code = 500, message = "Internal Server Error")})
		    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    public Response  putPloicy(@PathParam("poliname") String poliname, TypePolicy polig, @Context UriInfo uriInfo) {
				System.out.println("inside resource: Policy to be uppdated is "+poliname);
				TypePolicy policy2b = service.getSinglePolicy(poliname);		
		    	if (policy2b == null)
		    		throw new NotFoundException();
				System.out.println("inside resource: Policy found "+policy2b.getName());
		    	TypePolicy newpoli = service.putpolicy(policy2b);    			
		    	if (newpoli == null)
		    		throw new ForbiddenException("Forbidden because policy failed");	
		    	else
		    		return Response.ok(newpoli).build();
		    		
		    }
		   @PUT

		    @ApiOperation(	value = "verify a Policy object", notes = "json and xml formats"
			)
		    @ApiResponses(value = {
		    		@ApiResponse(code = 200, message = "OK"),
					@ApiResponse(code = 403, message = "Forbidden because negotiation failed"),
					@ApiResponse(code = 404, message = "Not found"),
		    		@ApiResponse(code = 500, message = "Internal Server Error")})
		    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    public Response  putVerifyPloicy(TypePolicy polig, @Context UriInfo uriInfo) throws ServiceException, UnknownNameException, DatatypeConfigurationException {
				System.out.println("inside resource: Policy to be verified is "+polig.getName());
				TypePolicy verified = service.getSinglePolicy(polig.getName());		
		    	if (verified == null)
		    		throw new NotFoundException();
		    	TypeNffgResult res= new TypeNffgResult();
		    	System.out.println("source . "+polig.getReachPolicy().getSource());
		    	System.out.println("destination . "+polig.getReachPolicy().getDestination());
		    	if(service.testReachability(polig.getReachPolicy().getSource(),polig.getReachPolicy().getDestination()))
				{
					
					res.setResult(true);
					res.setMessage("Policy result true");
					res.setVerifierTime(service.getXMLGregorianCalendarNow());
					verified.setNffgResult(res);
					//service.putpolicy(verified);
				}
				else
				{
					res.setResult(false);
					res.setMessage("Policy result false");
					res.setVerifierTime(service.getXMLGregorianCalendarNow());
					verified.setNffgResult(res);
					//service.putpolicy(verified);
				}
		    	System.out.println("inside resource: Policy found "+verified.getName());
		    	TypePolicy newpoli = service.putpolicy(verified);    			
		    	if (newpoli == null)
		    		throw new ForbiddenException("Forbidden because policy failed");	
		    	else
		    		return Response.ok(newpoli).build();
		    		
		    }
		  
		  
		  
		    @DELETE
		    @Path("{name}")
		    @ApiOperation(	value = "remove a asingle policy object", notes = "json and xml formats"
			)
		    @ApiResponses(value = {
		    		@ApiResponse(code = 200, message = "OK"),
		    		@ApiResponse(code = 404, message = "Not Found"),
		    		@ApiResponse(code = 500, message = "Internal Server Error")})
		    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
		    public TypePolicy deletePolicyJson(@PathParam("name") String name){
		    	System.out.println("inside policy resource delete: policy to be deleted is  "+name); 	
		    	TypePolicy deleted = service.remove(name);		    	
		    	
		    	if (deleted != null) { // success
		        	return deleted;
		    	} else
		    		throw new NotFoundException("couldnt delete");
		    }
		
		    		
		    }
		    
