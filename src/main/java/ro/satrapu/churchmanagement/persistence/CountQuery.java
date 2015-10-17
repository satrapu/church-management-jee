package ro.satrapu.churchmanagement.persistence;

import javax.persistence.EntityManager;

/**
 * Represents an object used for querying a database.
 */
public interface CountQuery {
    /**
     * Counts the entities matching the underlying search criteria.
     *
     * @param entityManager
     * @return
     */
    long getTotalRecords(EntityManager entityManager);
}
