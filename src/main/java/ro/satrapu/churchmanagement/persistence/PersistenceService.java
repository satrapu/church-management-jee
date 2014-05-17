/*
 * Copyright 2014 Satrapu.
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

import java.io.Serializable;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;

/**
 *
 * @author satrapu
 */
@Stateless
public class PersistenceService {

    @Inject
    @LoggerInstance
    Logger logger;

    @PersistenceContext(unitName = PersistenceUnits.CHURCH_MANAGEMENT)
    EntityManager entityManager;

    public <T extends Entity> T persist(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot persist null entity");
        }

        logger.debug("Persisting entity of type {} ...", entity.getClass().getName());
        entityManager.persist(entity);
        return entity;
    }

    public <T extends Entity> void remove(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot remove null entity");
        }

        logger.debug("Removing entity of type {} ...", entity.getClass().getName());
        T mergedEntity = entityManager.merge(entity);
        entityManager.remove(mergedEntity);
    }

    public <T extends Entity> T merge(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot merge null entity");
        }

        logger.debug("Merging entity of type {} ...", entity.getClass().getName());
        T mergedEntity = entityManager.merge(entity);
        return mergedEntity;
    }

    public <T extends Entity> List<T> fetch(Class<T> entityClass) {
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

    public <T extends Entity> T fetch(Class<T> entityClass, Serializable entityId) {
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

    public <T extends Entity> T fetchReference(Class<T> entityClass, Serializable entityId) {
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

    public <T extends Entity> List<T> fetch(Class<T> entityClass, int pageIndex, int recordsPerPage) {
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

    public <T extends Entity> long count(Class<T> entityClass) {
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
}
