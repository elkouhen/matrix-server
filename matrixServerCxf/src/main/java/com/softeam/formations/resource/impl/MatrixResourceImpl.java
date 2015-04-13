package com.softeam.formations.resource.impl;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;

import org.springframework.stereotype.Service;

import com.softeam.formations.datalayer.dto.Matrix;
import com.softeam.formations.datalayer.dto.Pair;
import com.softeam.formations.resource.MatrixResource;
import com.softeam.springconfig.JaxrsResource;

@JaxrsResource
@Service("com.softeam.formations.resource.HelloworldResource")
public class MatrixResourceImpl implements MatrixResource {

	@Override
	public void power(@Suspended AsyncResponse response, Pair<Matrix, Integer> m) {
		// TODO Auto-generated method stub
		return ;
	}
}
