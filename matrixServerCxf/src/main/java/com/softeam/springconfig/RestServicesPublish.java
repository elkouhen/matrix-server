package com.softeam.springconfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;

/**
 * Publication automatique des services REST
 */
@Configuration
@DependsOn({"appConfig", "cxfConfig"})
public class RestServicesPublish {

    /**
     * Préfixe des services REST (exemple d'url d'un service REST :
     * http://localhost/ContextApp/services/rest/monServiceRest)
     */
    private static final String REST_SERVICES_PREFIX = "/rest";

    /**
     * Bus CXF
     */
    @Autowired
    private SpringBus springBus = null;

    /**
     * Injection d'une classe de recherche et caractérisation (module,
     * interface) des services à publier
     */
    @Autowired
    private ServicesConfigUtils util = null;

    /**
     * Déploiement des services REST
     *
     * @return bean Serveur REST listant les services déployés
     */
    @Bean(name = "jaxrs")
    public JAXRSServerFactoryBean jaxrsServerFactoryBean() {

        // Construction d'une factory d'un conteneur de services REST
        JAXRSServerFactoryBean serverFactory = new JAXRSServerFactoryBean();
        serverFactory.setAddress(REST_SERVICES_PREFIX);

        List<Object> beans = new ArrayList<>();

        // recherche des classes des services REST (classes annotées Path.class)
        util.findBeans(beans, JaxrsResource.class);

        if (beans.size() > 0) {
            serverFactory.setBus(springBus);
            serverFactory.setServiceBeans(beans);
            serverFactory.setStart(true);
            // configuration du provider Json
            JacksonJsonProvider provider = new JacksonJsonProvider();
            serverFactory.setProvider(provider);
            serverFactory.create();
        }

        return serverFactory;
    }

}
