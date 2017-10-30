package it.polito.dp2.NFFG.sol3.service;

import java.net.URI;
import java.util.List;

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

import it.polito.dp2.NFFG.lab3.AlreadyLoadedException;

//baseURI : http://localhost:8080/NffgService/rest
@Path("/Nffg")
@Api(value = "/Nffg", description = "a collection of NFFG objects")
public class NffgResource {

	NffgService service = new NffgService();
	
	@GET
	@ApiOperation(	value = "get the Nffg objects ", notes = "xml and json formats")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
	@Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public List<TypeNffg> getNffgsXML(){
		System.out.println("Inside get Method NffgResource");
		List<TypeNffg> nffgs= service.getNffg();
		return nffgs;
	}
	@GET 
    @Path("{NffgName}")
    @ApiOperation(	value = "get a single Nffg object", notes = "json and xml formats"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "OK"),
    		@ApiResponse(code = 404, message = "Not Found"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public TypeNffg getSingleNffg(@PathParam("NffgName") String NffgName) {
		System.out.println("Inside get by name method ");
		System.out.println("Nffg name is "+NffgName);
		TypeNffg nffg = service.getSingleNffg(NffgName);
		return nffg;
    }
	
    @POST
    @ApiOperation(	value = "create a new NFFG object", notes = "xml and json format"
	)
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Created"),
    		@ApiResponse(code = 403, message = "Forbidden because Nffg post failed"),
    		@ApiResponse(code = 500, message = "Internal Server Error")})
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response postNffgXML(TypeNffg nffg,@Context UriInfo uriInfo) throws DatatypeConfigurationException, AlreadyLoadedException {
	
    	System.out.println("Inside Post Method");
    		TypeNffg created=service.creatNffg(nffg);
    		//Nffg neoCreated=service.createNeo4JNffg(nffg);
    		System.out.println("nffgsResource - After create nffg");
    	if (created != null) { // success
        	UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        	URI u = builder.path(created.getName()).build();
        	return Response.created(u).entity(created).build();
    	} else{
    		throw new ForbiddenException("Forbidden because Nffg post failed");}
    	
    }
    
}
