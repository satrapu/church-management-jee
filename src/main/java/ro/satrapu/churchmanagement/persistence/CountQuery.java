package ro.satrapu.churchmanagement.persistence;

import javax.persistence.EntityManager;

/**
 * Represents an object used for counting records found in a database.
 */
public interface CountQuery {
    /**
     * Counts the records matching the search criteria to be created using the given {@code entityManager} object.
     *
     * @param entityManager The {@code EntityManager} instance to use for creating a search criteria.
     * @return The number of records matching the search criteria.
     */
    long getTotalRecords(EntityManager entityManager);
}
