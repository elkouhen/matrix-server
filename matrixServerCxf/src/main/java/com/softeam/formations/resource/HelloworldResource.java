package com.softeam.formations.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/helloworld")
public interface HelloworldResource {
	
	@GET
	public String hello();

	@GET
	@Path("/disable")
	public void disable();

	@GET
	@Path("/enable")
	public void enable();
}
