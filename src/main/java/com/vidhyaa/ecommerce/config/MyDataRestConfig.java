package com.vidhyaa.ecommerce.config;

import com.vidhyaa.ecommerce.entity.Country;
import com.vidhyaa.ecommerce.entity.Product;
import com.vidhyaa.ecommerce.entity.ProductCategory;
import com.vidhyaa.ecommerce.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;

    }
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
        //disable HTTP Method for Product : Put, Post, Delete
        disableHttpMethods(Product.class,config, theUnsupportedActions);

        //disable HTTP Method for Product : Put, Post, Delete
        disableHttpMethods(ProductCategory.class,config, theUnsupportedActions);
        disableHttpMethods(Country.class,config, theUnsupportedActions);
        disableHttpMethods(State.class,config, theUnsupportedActions);


        //call an internal helper method to expose Id
        exposeIds(config);



    }

    private void disableHttpMethods(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration().
                forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //expose entity id
        //get a list of all entity classes from EM
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        //create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        //get the entity types for the entities
        for (EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        //expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}