package com.softeam.formations.resource.impl;

import com.softeam.formations.resource.HelloworldResource;
import com.softeam.formations.resource.MyFF4J;
import net.sf.ehcache.CacheManager;
import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softeam.springconfig.JaxrsResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@JaxrsResource
@Service("com.softeam.formations.resource.HelloworldResource")
public class HelloworldResourceImpl implements HelloworldResource {

	@Autowired
    private FF4j ff4j;
	
	@Override
	public String hello() {
		
		if (ff4j.check("matrix")){

            return "OK";
        }

        return "KO";
	}

    public void disable(){
        ff4j.disable("matrix");
    }

    public void enable(){
        ff4j.enable("matrix");
    }
}
