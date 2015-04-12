package com.softeam.formations.resource;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by elkouhen on 12/04/15.
 */
@Component
public class MyFF4J {

    @Autowired
    private FF4j ff4j;

    @Cacheable (value ="ff4j" )
    public boolean check(String key){
        return ff4j.check(key);
    }

    @CacheEvict (value ="ff4j" )
    public void enable(String key){
         ff4j.enable(key);
    }

    @CacheEvict (value ="ff4j" )
    public void disable(String key){
         ff4j.disable(key);
    }
}
