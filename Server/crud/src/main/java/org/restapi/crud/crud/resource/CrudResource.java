package org.restapi.crud.crud.resource;

import org.restapi.crud.crud.model.CrudModel;
import org.restapi.crud.crud.service.CrudService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/crud")
public class CrudResource {
	private CrudService service = new CrudService();
	
	@GET
	@Path("/words/{word}")
	@Produces(MediaType.APPLICATION_JSON)
	public CrudModel getword(@PathParam("word") String word) {
		return service.getEntries(word);
	}
}
