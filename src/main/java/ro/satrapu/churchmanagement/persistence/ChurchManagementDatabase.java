package ro.satrapu.churchmanagement.persistence;

import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * CDI qualifier used for producing {@link EntityManager} instances pointing to the database storing
 * all entities used by the <b>Church Management</b> application.
 *
 * @author satrapu
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
@interface ChurchManagementDatabase {
}
