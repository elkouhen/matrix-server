package com.softeam.formations.springconfig;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by elkouhen on 06/04/15.
 */
@Configuration
public class ServletConfig {
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        return tomcat;
    }

    /*private Connector createConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");

        return  connector;
    }*/

}
