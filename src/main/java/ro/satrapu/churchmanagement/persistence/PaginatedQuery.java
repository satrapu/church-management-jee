package ro.satrapu.churchmanagement.persistence;

import javax.persistence.EntityManager;

/**
 * Represents an object used for querying a database.
 *
 * @param <T> The entity type to fetch.
 */
public interface PaginatedQuery<T> extends Query<T> {
    /**
     * Fetches a single page of entities matching the underlying search criteria.
     *
     * @param entityManager
     * @param firstResult
     * @param maxResults
     * @return
     */
    PaginatedQuerySearchResult<T> getSearchResult(EntityManager entityManager, Integer firstResult, Integer maxResults);
}
