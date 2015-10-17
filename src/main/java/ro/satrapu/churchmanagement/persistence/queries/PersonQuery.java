package ro.satrapu.churchmanagement.persistence.queries;

import ro.satrapu.churchmanagement.persistence.CountQuery;
import ro.satrapu.churchmanagement.persistence.PaginatedQuery;
import ro.satrapu.churchmanagement.persistence.PaginatedQuerySearchResult;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.QuerySearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Represents an object used for querying a database for entities of type {@link Person}.
 */
public class PersonQuery implements PaginatedQuery<Person>, CountQuery {
    @Override
    public QuerySearchResult<Person> getSearchResult(EntityManager entityManager) {
        List<Person> persons = fetchPersons(entityManager, null, null);
        QuerySearchResult<Person> result = new QuerySearchResult<>(persons);
        return result;
    }

    @Override
    public PaginatedQuerySearchResult<Person> getSearchResult(EntityManager entityManager, Integer firstResult, Integer maxResults) {
        List<Person> resultList = fetchPersons(entityManager, firstResult, maxResults);
        long totalCount = countPersons(entityManager);

        PaginatedQuerySearchResult<Person> result = new PaginatedQuerySearchResult<>(resultList, totalCount);
        return result;
    }

    @Override
    public long getTotalRecords(EntityManager entityManager) {
        long result = countPersons(entityManager);
        return result;
    }

    private List<Person> fetchPersons(EntityManager entityManager, Integer firstResult, Integer maxResults) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
        criteria.select(criteria.from(Person.class));
        TypedQuery<Person> query = entityManager.createQuery(criteria);

        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }

        //TODO apply filters and sorters

        List<Person> result = query.getResultList();
        return result;
    }

    private long countPersons(EntityManager entityManager) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        criteriaQuery.select(builder.count(criteriaQuery.from(Person.class)));

        //TODO apply filters

        long result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result;
    }
}
