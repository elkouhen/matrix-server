package com.softeam.formations.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;

@Path("/matrix")
public interface MatrixResource {
	
	@POST
    public void power(@Suspended AsyncResponse response, final Pair<Matrix, Integer> m);
}
