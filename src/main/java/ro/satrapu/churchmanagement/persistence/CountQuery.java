package ro.satrapu.churchmanagement.persistence;

import javax.persistence.EntityManager;

/**
 * Represents an object used for counting records found in a database.
 */
public interface CountQuery {
    /**
     * Counts the records matching the search criteria to be created using the given {@code entityManager} object.
     *
     * @param entityManager
     * @return
     */
    long getTotalRecords(EntityManager entityManager);
}
