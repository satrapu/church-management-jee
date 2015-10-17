package ro.satrapu.churchmanagement.persistence.queries;

import lombok.Data;
import ro.satrapu.churchmanagement.model.text.StringExtensions;
import ro.satrapu.churchmanagement.persistence.CountQuery;
import ro.satrapu.churchmanagement.persistence.PaginatedQuery;
import ro.satrapu.churchmanagement.persistence.PaginatedQuerySearchResult;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.Person_;
import ro.satrapu.churchmanagement.persistence.QuerySearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an object used for querying a database for entities of type {@link Person}.
 */
@Data
public class PersonQuery implements PaginatedQuery<Person>, CountQuery {
    private Integer id;
    private String firstNamePattern;
    private String middleNamePattern;
    private String lastNamePattern;
    private String emailAddressNamePattern;

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

    /**
     * Fetches a list of {@link Person} matching the underlying search criteria.
     * Add conditions only for those fields which have been set - see http://stackoverflow.com/a/12199937
     * Perform case-insensitive searches - see http://stackoverflow.com/a/4591615
     *
     * @param entityManager
     * @param firstResult
     * @param maxResults
     * @return
     */
    private List<Person> fetchPersons(EntityManager entityManager, Integer firstResult, Integer maxResults) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        List<Predicate> predicates = getPredicates(criteriaBuilder, root);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        TypedQuery<Person> query = entityManager.createQuery(criteriaQuery);

        if (firstResult != null) {
            query.setFirstResult(firstResult);
        }

        if (maxResults != null) {
            query.setMaxResults(maxResults);
        }

        List<Person> result = query.getResultList();
        return result;
    }

    private long countPersons(EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        List<Predicate> predicates = getPredicates(criteriaBuilder, root);
        criteriaQuery.select(criteriaBuilder.count(root)).where(predicates.toArray(new Predicate[]{}));

        long result = entityManager.createQuery(criteriaQuery).getSingleResult();
        return result;
    }

    private List<Predicate> getPredicates(CriteriaBuilder criteriaBuilder, Root<Person> root) {
        List<Predicate> result = new ArrayList<>();

        if (!StringExtensions.isNullOrWhitespace(firstNamePattern)) {
            result.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Person_.firstName)),
                    MessageFormat.format("%{0}%", firstNamePattern.toLowerCase())));
        }

        if (!StringExtensions.isNullOrWhitespace(middleNamePattern)) {
            result.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Person_.middleName)),
                    MessageFormat.format("%{0}%", middleNamePattern.toLowerCase())));
        }

        if (!StringExtensions.isNullOrWhitespace(lastNamePattern)) {
            result.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Person_.lastName)),
                    MessageFormat.format("%{0}%", lastNamePattern.toLowerCase())));
        }

        if (!StringExtensions.isNullOrWhitespace(emailAddressNamePattern)) {
            result.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Person_.emailAddress)),
                    MessageFormat.format("%{0}%", emailAddressNamePattern.toLowerCase())));
        }

        return result;
    }
}
