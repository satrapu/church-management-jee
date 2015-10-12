package ro.satrapu.churchmanagement.persistence;

import io.codearte.jfairy.Fairy;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class ChurchManagementDatabaseInitializer {
    /**
     * The magic number {@value ChurchManagementDatabaseInitializer#MAX_AMOUNT_OF_PERSONS_TO_GENERATE} has been picked
     * in order to have several pages of entities available inside the person search page.
     */
    private static final int MAX_AMOUNT_OF_PERSONS_TO_GENERATE = 117;

    @Inject
    private PersistenceService persistenceService;

    @PostConstruct
    public void initializeDatabase() {
        Fairy fairy = Fairy.create();

        generatePersons(fairy, MAX_AMOUNT_OF_PERSONS_TO_GENERATE);
    }

    private void generatePersons(Fairy fairy, int maxAmount) {
        for (int i = 0; i < maxAmount; i++) {
            io.codearte.jfairy.producer.person.Person generatedPerson = fairy.person();

            Person person = new Person();
            person.setFirstName(generatedPerson.firstName());
            person.setMiddleName(generatedPerson.middleName());
            person.setLastName(generatedPerson.lastName());
            person.setEmailAddress(generatedPerson.companyEmail());

            persistenceService.persist(person);
        }
    }
}
