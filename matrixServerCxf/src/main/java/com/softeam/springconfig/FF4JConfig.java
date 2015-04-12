package com.softeam.springconfig;

import org.ff4j.FF4j;
import org.ff4j.cache.FeatureCacheProviderEhCache;
import org.ff4j.cache.FeatureStoreCacheProxy;
import org.ff4j.core.FeatureStore;
import org.ff4j.store.InMemoryFeatureStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by elkouhen on 12/04/15.
 */
@Configuration
public class FF4JConfig {

    @Bean
    public FF4j ff4j() {
        FF4j ff4j = new FF4j();

        ff4j.setFeatureStore(store());

        return ff4j;
    }

    @Bean
    public FeatureStore store() {
        InMemoryFeatureStore store = new InMemoryFeatureStore();

        store.setLocation("ff4j.xml");

        final FeatureCacheProviderEhCache cache = new FeatureCacheProviderEhCache();

        FeatureStoreCacheProxy proxy = new FeatureStoreCacheProxy(store, cache);

        return proxy;
    }

}
