package com.softeam.formations.springconfig;

//import org.apache.catalina.connector.Connector;
//import org.apache.coyote.AbstractProtocol;
//import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
//	@Bean
//	public EmbeddedServletContainerFactory servletContainer() {
//		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
//		
//		tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
//			@Override
//			public void customize(Connector connector) {
//				ProtocolHandler handler = connector.getProtocolHandler();
//				if (handler instanceof AbstractProtocol) {
//					@SuppressWarnings("rawtypes")
//					AbstractProtocol protocol = (AbstractProtocol) handler;
//					protocol.setMaxThreads(600);
//				}
//			}
//		});
//		return tomcat;
//	}
}
