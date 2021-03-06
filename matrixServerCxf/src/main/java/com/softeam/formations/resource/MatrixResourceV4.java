package com.softeam.formations.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.impl.MatrixResourceV4Impl;

@Path(MatrixResourceV4Impl.RESOURCE + MatrixResourceV4Impl.VERSION)
@Consumes("application/json")
@Produces("application/json")
public interface MatrixResourceV4 {

	@POST
	@Path("/power")
	public void power(@Suspended AsyncResponse response, final Pair<Matrix, Integer> m) throws Exception;
}
