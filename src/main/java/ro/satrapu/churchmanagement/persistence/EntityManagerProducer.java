package ro.satrapu.churchmanagement.persistence;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Produces {@link EntityManager} instances belonging to the persistence unit named "{@value EntityManagerProducer#CHURCH_MANAGEMENT_PERSISTENCE_UNIT_NAME}".
 * <br />
 * Code inspired by Antonio Goncalves' post:
 * <a href="http://antoniogoncalves.org/2011/09/25/injection-with-cdi-part-iii">Injection with CDI (Part III)</a>.
 *
 * @author satrapu
 */
public class EntityManagerProducer {
    private static final String CHURCH_MANAGEMENT_PERSISTENCE_UNIT_NAME = "church-management";

    @Produces
    @PersistenceContext(unitName = CHURCH_MANAGEMENT_PERSISTENCE_UNIT_NAME)
    @ChurchManagementDatabase
    private EntityManager entityManager;
}
