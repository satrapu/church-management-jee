/*
 * Copyright 2014 satrapu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.satrapu.churchmanagement.persistence;

import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages all CRUD application operations.
 *
 * @author satrapu
 */
@Stateless
public class PersistenceService implements Serializable {
    private static final long serialVersionUID = 1L;
    private EntityManager entityManager;
    private Logger logger;

    /**
     * This default constructor is required in order to support the one annotated with @Inject - see more details <a href="http://stackoverflow.com/a/9192342">here</a>.
     */
    public PersistenceService() {
    }

    @Inject
    public PersistenceService(@NotNull @ChurchManagementDatabase EntityManager entityManager,
                              @NotNull Logger logger) {
        this();
        this.entityManager = entityManager;
        this.logger = logger;
    }

    public <T extends Serializable> T persist(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Parameter \"entity\" is null");
        }

        logger.info("Persisting entity of type {} ...", entity.getClass().getName());
        entityManager.persist(entity);
        return entity;
    }

    public <T extends Serializable> List<T> persist(List<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Cannot persist null entity list");
        }

        if (entities.isEmpty()) {
            logger.info("Entity list is empty, there is nothing to persist");
            return new ArrayList<>();
        }

        List<T> persistedEntities = new ArrayList<>(entities.size());

        for (T entity : entities) {
            logger.info("Persisting entity of type {} ...", entity.getClass().getName());
            entityManager.persist(entity);
            persistedEntities.add(entity);
        }

        return persistedEntities;
    }

    public <T extends Serializable> void remove(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot remove null entity");
        }

        logger.info("Removing entity of type {} ...", entity.getClass().getName());
        T mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

    public <T extends Serializable> void remove(List<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Cannot remove null entity list");
        }

        if (entities.isEmpty()) {
            logger.info("Entity list is empty, there is nothing to remove");
            return;
        }

        for (T entity : entities) {
            logger.info("Removing entity of type {} ...", entity.getClass().getName());
            T mergedEntity = entityManager.merge(entity);
            entityManager.remove(mergedEntity);
        }
    }

    public <T extends Serializable> T merge(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot merge null entity");
        }

        logger.debug("Merging entity of type {} ...", entity.getClass().getName());
        T mergedEntity = entityManager.merge(entity);
        return mergedEntity;
    }

    public <T extends Serializable> List<T> merge(List<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Cannot merge null entity list");
        }

        if (entities.isEmpty()) {
            logger.info("Entity list is empty, there is nothing to merge");
            return new ArrayList<>();
        }

        List<T> mergedEntities = new ArrayList<>(entities.size());

        for (T entity : entities) {
            logger.info("Merging entity of type {} ...", entity.getClass().getName());
            entityManager.merge(entity);
            mergedEntities.add(entity);
        }

        return mergedEntities;
    }

    public <T extends Serializable> void detach(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot detach null entity");
        }

        logger.info("Detaching entity of type {} ...", entity.getClass().getName());
        entityManager.detach(entity);
    }

    public <T extends Serializable> boolean isManaged(T entity) {
        if (entity == null) {
            return false;
        }

        logger.info("Checking whether entity of type {} is managed or not ...", entity.getClass().getName());
        return entityManager.contains(entity);
    }

    public <T extends Serializable> T fetch(Class<T> entityClass, Serializable entityId) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot fetch entity by using null as entity class");
        }

        if (entityId == null) {
            throw new IllegalArgumentException("Cannot fetch entity by using null as entity id");
        }

        logger.debug("Fetching entity of type {} using id {} ...", entityClass.getName(), entityId);
        T entity = entityManager.find(entityClass, entityId);
        return entity;
    }

    public <T> PaginatedQuerySearchResult<T> fetch(PaginatedQuery<T> entityQuery, Integer firstResult, Integer maxResults) {
        if (entityQuery == null) {
            throw new IllegalArgumentException("Cannot fetch entities using null as query");
        }

        PaginatedQuerySearchResult<T> result = entityQuery.getSearchResult(entityManager, firstResult, maxResults);
        return result;
    }

    public <T> QuerySearchResult<T> fetch(Query<T> entityQuery) {
        if (entityQuery == null) {
            throw new IllegalArgumentException("Cannot fetch entities using null as query");
        }

        QuerySearchResult<T> result = entityQuery.getSearchResult(entityManager);
        return result;
    }

    public <T extends Serializable> T fetchReference(Class<T> entityClass, Serializable entityId) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot fetch entity reference by using null as entity class");
        }

        if (entityId == null) {
            throw new IllegalArgumentException("Cannot fetch entity reference by using null as entity id");
        }

        logger.info("Fetching entity reference of type {}, using id {} ...", entityClass.getName(), entityId);
        T entity = entityManager.getReference(entityClass, entityId);
        return entity;
    }

    public long count(CountQuery countQuery) {
        if (countQuery == null) {
            throw new IllegalArgumentException("Cannot count entities using null as query");
        }

        long result = countQuery.getTotalRecords(entityManager);
        return result;
    }
}
