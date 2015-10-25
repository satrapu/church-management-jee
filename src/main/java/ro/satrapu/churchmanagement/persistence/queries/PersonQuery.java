package ro.satrapu.churchmanagement.persistence.queries;

import lombok.Data;
import ro.satrapu.churchmanagement.model.text.StringExtensions;
import ro.satrapu.churchmanagement.persistence.CountQuery;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.Person_;
import ro.satrapu.churchmanagement.persistence.Query;
import ro.satrapu.churchmanagement.persistence.QuerySearchResult;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an object used for querying a database for entities of type {@link Person}.
 */
@Data
public class PersonQuery implements Query<Person>, CountQuery {
    private Integer id;
    private String firstNamePattern;
    private String middleNamePattern;
    private String lastNamePattern;
    private String emailAddressNamePattern;
    private Fields sortByField;
    private boolean sortAscending = true;

    /**
     * @param entityManager
     * @return
     */
    @Override
    public QuerySearchResult<Person> getSearchResult(EntityManager entityManager, Integer firstResult, Integer maxResults) {
        List<Person> persons = fetchPersons(entityManager, firstResult, maxResults);
        QuerySearchResult<Person> result = new QuerySearchResult<>(persons);
        return result;
    }

    /**
     * @param entityManager
     * @return
     */
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

        Order order = getOrder(criteriaBuilder, root);

        if (order != null) {
            criteriaQuery.orderBy(order);
        }

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

        if (id != null) {
            // satrapu - 2015-10-18: perform LIKE over an integer value - see http://stackoverflow.com/a/10230847
            result.add(criteriaBuilder.like(root.get(Person_.id).as(String.class), MessageFormat.format("%{0}%", id)));
        }

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

    private Order getOrder(CriteriaBuilder criteriaBuilder, Root<Person> root) {
        if (sortByField == null) {
            return null;
        }

        Order result;
        Expression<?> sortExpression;

        switch (sortByField) {
            case EMAIL_ADDRESS:
                sortExpression = root.get(Person_.emailAddress);
                break;
            case FIRST_NAME:
                sortExpression = root.get(Person_.firstName);
                break;
            case ID:
                sortExpression = root.get(Person_.id);
                break;
            case LAST_NAME:
                sortExpression = root.get(Person_.lastName);
                break;
            case MIDDLE_NAME:
                sortExpression = root.get(Person_.middleName);
                break;
            default:
                throw new IllegalStateException(String.format("Encountered invalid field key: %s", sortByField.getKey()));
        }

        if (sortAscending) {
            result = criteriaBuilder.asc(sortExpression);
        } else {
            result = criteriaBuilder.desc(sortExpression);
        }

        return result;
    }

    public enum Fields {
        ID("id"),
        FIRST_NAME("firstName"),
        MIDDLE_NAME("middleName"),
        LAST_NAME("lastName"),
        EMAIL_ADDRESS("emailAddress");

        private final String fieldKey;

        Fields(String fieldKey) {
            this.fieldKey = fieldKey;
        }

        public String getKey() {
            return fieldKey;
        }
    }
}
