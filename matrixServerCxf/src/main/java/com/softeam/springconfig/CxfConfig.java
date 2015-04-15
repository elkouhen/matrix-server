package com.softeam.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Responsable du chargement des fichiers de configuration XML (notamment les
 * fichiers CXF et les fichiers de gestion de la sécurité).
 * <p/>
 * Le chargement ne doit être réalisé qu'une unique fois et ceci avant la
 * configuration des services SOAP et REST, le cas échéant.
 */
@Configuration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class CxfConfig {

}
