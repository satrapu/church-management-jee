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
import ro.satrapu.churchmanagement.persistence.query.EntityCountQuery;
import ro.satrapu.churchmanagement.persistence.query.EntityQuery;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

        logger.debug("Persisting entity of type {} ...", entity.getClass().getName());
        entityManager.persist(entity);
        return entity;
    }

    public <T extends Serializable> List<T> persist(List<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Cannot persist null entity list");
        }

        if (entities.isEmpty()) {
            logger.warn("Entity list is empty, there is nothing to persist");
            return new ArrayList<>();
        }

        List<T> persistedEntities = new ArrayList<>(entities.size());

        for (T entity : entities) {
            logger.debug("Persisting entity of type {} ...", entity.getClass().getName());
            entityManager.persist(entity);
            persistedEntities.add(entity);
        }

        return persistedEntities;
    }

    public <T extends Serializable> void remove(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot remove null entity");
        }

        logger.debug("Removing entity of type {} ...", entity.getClass().getName());
        T mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

    public <T extends Serializable> void remove(List<T> entities) {
        if (entities == null) {
            throw new IllegalArgumentException("Cannot remove null entity list");
        }

        if (entities.isEmpty()) {
            logger.warn("Entity list is empty, there is nothing to remove");
            return;
        }

        for (T entity : entities) {
            logger.debug("Removing entity of type {} ...", entity.getClass().getName());
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
            logger.warn("Entity list is empty, there is nothing to merge");
            return new ArrayList<>();
        }

        List<T> mergedEntities = new ArrayList<>(entities.size());

        for (T entity : entities) {
            logger.debug("Merging entity of type {} ...", entity.getClass().getName());
            entityManager.merge(entity);
            mergedEntities.add(entity);
        }

        return mergedEntities;
    }

    public <T extends Serializable> void detach(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot detach null entity");
        }

        logger.debug("Detaching entity of type {} ...", entity.getClass().getName());
        entityManager.detach(entity);
    }

    public <T extends Serializable> boolean isManaged(T entity) {
        if (entity == null) {
            return false;
        }

        logger.debug("Checking whether entity of type {} is managed or not ...", entity.getClass().getName());
        return entityManager.contains(entity);
    }

    public <T extends Serializable> List<T> fetch(Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot fetch entities by using null as entity class");
        }

        logger.debug("Fetching all entities of type {} ...", entityClass.getName());

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        criteria.select(criteria.from(entityClass));

        TypedQuery<T> query = entityManager.createQuery(criteria);
        List<T> resultList = query.getResultList();
        logger.debug("Fetched {} entities", resultList.size());
        return resultList;
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

    public <T extends Serializable> T fetchReference(Class<T> entityClass, Serializable entityId) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot fetch entity reference by using null as entity class");
        }

        if (entityId == null) {
            throw new IllegalArgumentException("Cannot fetch entity reference by using null as entity id");
        }

        logger.debug("Fetching entity reference of type {}, using id {} ...", entityClass.getName(), entityId);
        T entity = entityManager.getReference(entityClass, entityId);
        return entity;
    }

    public <T> List<T> fetch(EntityQuery<T> entityQuery) {
        return fetch(entityQuery, null, null);
    }

    public <T> List<T> fetch(EntityQuery<T> entityQuery, Integer firstResult, Integer maxResults) {
        if (entityQuery == null) {
            throw new IllegalArgumentException("Cannot fetch entities using null as query");
        }

        List<T> result = entityQuery.list(entityManager, firstResult, maxResults);
        return result;
    }

    public <T extends Serializable> List<T> fetch(Class<T> entityClass, int pageIndex, int recordsPerPage) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot fetch entities using null as entity class");
        }

        logger.debug("Fetching maximum {} entities of type {} for page #{} ...", recordsPerPage, entityClass.getName(), pageIndex + 1);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(entityClass);
        criteria.select(criteria.from(entityClass));

        TypedQuery<T> query = entityManager.createQuery(criteria);
        query.setFirstResult(pageIndex);
        query.setMaxResults(recordsPerPage);

        List<T> resultList = query.getResultList();
        logger.debug("Found {} entities", resultList.size());
        return resultList;
    }

    public <T extends Serializable> long count(Class<T> entityClass) {
        if (entityClass == null) {
            throw new IllegalArgumentException("Cannot count entities by using null as entity class");
        }

        logger.debug("Counting entities of type {} ...", entityClass.getName());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(entityClass)));

        long count = entityManager.createQuery(criteriaQuery).getSingleResult();
        logger.debug("Found {} entities", count);
        return count;
    }

    public long count(EntityCountQuery entityCountQuery) {
        if (entityCountQuery == null) {
            throw new IllegalArgumentException("Cannot count entities using null as query");
        }

        long count = entityCountQuery.count(entityManager);
        logger.debug("Found {} entities", count);
        return count;
    }
}
