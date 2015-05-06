package com.softeam.springconfig;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

/**
 * Fonctionnalités :
 * <ul>
 * <li>Recherche de composants Spring (avec une annotation donnée)</li>
 * <li>Caractérisation de composants Spring (interface du composant, nom du
 * module parent)</li>
 * </ul>
 */
@Service("com.softeam.ramlapp.springconfig.ServicesConfigUtils")
@SuppressWarnings({ "unchecked", "rawtypes" })
class ServicesConfigUtils {

	/**
	 * Logger de la classe
	 */
	private static final Log LOG = LogFactory.getLog(ServicesConfigUtils.class);

	/**
	 * Sous-package des classes des beans Spring
	 */
	private static final String IMPL_PACKAGE = ".impl";

	/**
	 * Suffixe des classes des beans Spring (Impl à la SNCF)
	 */
	private static final String IMPL_SUFFIX = "Impl";

	/**
	 * Package de base des applis, dans lequel on recherche des services à
	 * déployer
	 */
	private static final String BASE_PACKAGE = "com.softeam";

	/**
	 * Contexte SPRING
	 */
	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * Recherche (dans le package BASE_PACKAGE) de tous les beans Spring annotés
	 * "annotation"
	 *
	 * @param beanList
	 *          liste des beans trouvés
	 * @param annotation
	 *          annotation utilisée pour la recherche des classes
	 */

	public void findBeans(List<Object> beanList, Class annotation) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));

		for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {
			beanList.add(applicationContext.getBean(interfaceName(bd.getBeanClassName())));
		}
	}

	/**
	 * Recherche (dans le package BASE_PACKAGE) des classes annotées "annotation"
	 *
	 * @param classList
	 *          liste des classes trouvées
	 * @param annotation
	 *          annotation sur les classes recherchées
	 */
	public void findClass(List<Class> classList, Class annotation) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));

		for (BeanDefinition bd : scanner.findCandidateComponents(BASE_PACKAGE)) {

			try {
				classList.add(Class.forName(bd.getBeanClassName()));
			} catch (ClassNotFoundException e) {

				LOG.error("problème de déploiement du service associé au bean : " + bd.getBeanClassName());
			}
		}
	}

	/**
	 * Fonction de correspondance entre une classe d'implémentation d'un Bean
	 * Spring et son interface (basé sur les conventions de nommage SNCF)
	 *
	 * @param classe
	 *          nom de la classe du composant Spring
	 * @return Retourne le nom de l'interface de la classe
	 */
	public String interfaceName(String classeName) {
		return classeName.replaceAll("(?i)(" + IMPL_PACKAGE + ")(.+?)(" + IMPL_SUFFIX + ")", "$2");
	}

	/**
	 * Détermine le nom du module fonctionnel auquel appartient la classe
	 *
	 * @param classe
	 *          classe du composant Spring
	 * @return le nom du module fonctionnel de la classe
	 */
	public String moduleName(Class classe) {

		String classeName = classe.getCanonicalName();

		int indexStart = classeName.indexOf('.', BASE_PACKAGE.length() + 1);
		int indexEnd = classeName.indexOf('.', classeName.lastIndexOf('.') - ".dl.dto".length());

		return classeName.substring(indexStart + 1, indexEnd).replace(".", "/");
	}
}
