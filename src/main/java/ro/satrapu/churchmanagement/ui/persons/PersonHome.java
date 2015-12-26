/*
 * Copyright 2014 satrapu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.satrapu.churchmanagement.ui.persons;

import org.slf4j.Logger;
import ro.satrapu.churchmanagement.persistence.DiscipleshipStatus;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.ui.Urls;
import ro.satrapu.churchmanagement.ui.messages.Messages;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages a specific {@link Person} instance.
 * <br/>
 * Code inspired by Andy Gibson's post:
 * <a href="http://www.andygibson.net/blog/tutorial/pattern-for-conversational-crud-in-java-ee-6">Conversational CRUD in Java EE 6</a>.
 *
 * @author satrapu
 */
@Named
@ConversationScoped
public class PersonHome implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Class<Person> entityClass = Person.class;
    private static Map<String, String> discipleshipStatusValues;
    private PersistenceService persistenceService;
    private Conversation conversation;
    private Messages messages;
    private Logger logger;
    private ValidatorFactory validatorFactory;
    private Serializable id;
    private Person instance;

    @Inject
    @Valid
    public PersonHome(@NotNull PersistenceService persistenceService,
                      @NotNull Conversation conversation,
                      @NotNull Messages messages,
                      @NotNull Logger logger,
                      @NotNull ValidatorFactory validatorFactory) {
        this.persistenceService = persistenceService;
        this.conversation = conversation;
        this.messages = messages;
        this.logger = logger;
        this.validatorFactory = validatorFactory;

        discipleshipStatusValues = new LinkedHashMap<>();
        discipleshipStatusValues.put(messages.getMessageFor("enumerations.DiscipleshipStatus.DISCIPLE_CANDIDATE"), DiscipleshipStatus.DISCIPLE_CANDIDATE.toString());
        discipleshipStatusValues.put(messages.getMessageFor("enumerations.DiscipleshipStatus.NOT_INTERESTED"), DiscipleshipStatus.NOT_INTERESTED.toString());
        discipleshipStatusValues.put(messages.getMessageFor("enumerations.DiscipleshipStatus.TEACHING_CANDIDATE"), DiscipleshipStatus.TEACHING_CANDIDATE.toString());
    }

    /**
     * Gets the entity managed by this instance.
     *
     * @return An entity managed by this instance.
     */
    public Person getInstance() {
        if (instance == null) {
            Serializable entityId = getId();

            if (entityId != null) {
                instance = loadInstance();
            } else {
                instance = createInstance();
            }
        }

        return instance;
    }

    /**
     * Gets the entity identifier.
     *
     * @return The entity identifier.
     */
    public Serializable getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     *
     * @param id The identifier to set.
     */
    public void setId(Serializable id) {
        logger.debug("Old entity id: {}", this.id);
        this.id = id;
        logger.debug("Entity id has been set to: {}", this.id);

    }

    /**
     * Gets all available {@link DiscipleshipStatus} values a user may choose from when editing a {@link Person} instance.
     *
     * @return All available {@link DiscipleshipStatus} enumeration.
     */
    public Map<String, String> getDiscipleshipStatusValues() {
        return discipleshipStatusValues;
    }

    /**
     * Loads an entity based on the value returned by the {@link PersonHome#getId()} method.
     *
     * @return the {@link Person} entity
     */
    public Person loadInstance() {
        logger.debug("Loading entity using id: {} ...", id);
        Person existingEntity = persistenceService.fetch(entityClass, id);
        logger.debug("An existing entity with id: {} has been loaded", id);

        return existingEntity;
    }

    /**
     * Creates a new entity.
     *
     * @return A new entity.
     */
    public Person createInstance() {
        logger.debug("Creating a new entity ...");
        Person newEntity = new Person();
        logger.debug("A new entity has been created");

        return newEntity;
    }

    /**
     * Gets whether the current entity has a persistent identity (i.e. represents a new entity) or not.
     * See more <a href="http://stackoverflow.com/a/2780067">here</a>.
     *
     * @return True, if the entity has a persistent identity; false, otherwise.
     */
    public boolean isNew() {
        return getInstance().getId() == null;
    }

    /**
     * Saves the changes of the current entity to the underlying persistent storage.
     *
     * @return The operation outcome, if successful; null, otherwise.
     */
    public String save() {
        logger.debug("Saving entity ...");
        boolean wasEntitySaved = false;

        if (!isValid()) {
            logger.warn("Encountered an invalid entity, aborting save operation");
            messages.addError("global.fields.invalid");
            return null;
        }

        Person person = getInstance();

        if (isNew()) {
            try {
                person = persistenceService.persist(person);
                messages.addInfo("entities.person.actions.save.success");
                wasEntitySaved = true;
            } catch (Exception e) {
                logger.error("Could not persist entity", e);
                messages.addError("entities.person.actions.save.failure");
            }
        } else {
            try {
                person = persistenceService.merge(person);
                messages.addInfo("entities.person.actions.update.success");
                wasEntitySaved = true;
            } catch (Exception e) {
                logger.error("Could not merge entity", e);
                messages.addError("entities.person.actions.update.failure");
            }
        }

        if (wasEntitySaved) {
            conversation.end();
            logger.info("Entity with id: {} was saved", person.getId());

            return Urls.Secured.Persons.LIST;
        }

        logger.warn("Entity with id: {} was not saved", person.getId());
        return null;
    }

    /**
     * Cancels the current operation and closes the current conversation.
     *
     * @return The operation outcome.
     */
    public String cancel() {
        Serializable entityId = getId();
        String conversationId = conversation.getId();

        conversation.end();
        logger.info("Editing entity with id: {} has been cancelled and conversation with id: {} has ended", entityId, conversationId);

        return Urls.Secured.Persons.LIST;
    }

    /**
     * Initializes a long-running conversation.
     */
    public void beginConversation() {
        if (conversation.isTransient()) {
            conversation.begin();
            logger.debug("Conversation with id: {} has begun", conversation.getId());
        }
    }

    /**
     * Removes the current entity from the underlying persistent storage.
     *
     * @return The operation outcome, if successful; null, otherwise.
     */
    public String remove() {
        Serializable entityId = getId();
        logger.debug("Removing entity with id: {} ...", entityId);
        boolean wasEntityRemoved = false;

        try {
            Person person = getInstance();
            persistenceService.remove(person);
            messages.addInfo("entities.person.actions.remove.success");
            wasEntityRemoved = true;
        } catch (Exception e) {
            logger.error("Could not remove instance", e);
            messages.addError("entities.person.actions.remove.failure");
        }

        if (wasEntityRemoved) {
            conversation.end();
            logger.info("Entity with id: {} was removed", entityId);

            return Urls.Secured.Persons.LIST;
        }

        logger.warn("Entity with id: {} was not removed", entityId);
        return null;
    }

    /**
     * Checks whether the current edited instance is valid or not.
     *
     * @return True, if the instance is valid; false, otherwise.
     */
    private boolean isValid() {
        boolean result = true;
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Person>> constraintViolations = validator.validate(getInstance());

        if (constraintViolations.size() > 0) {
            result = false;
            StringBuilder sb = new StringBuilder();

            for (ConstraintViolation<Person> constraintViolation : constraintViolations) {
                sb.append(MessageFormat.format("\t\t{0}: {1}{2}",
                        constraintViolation.getPropertyPath(), constraintViolation.getMessage(), System.lineSeparator()));
            }

            logger.warn("Encountered constraint violations ... {}{}", System.lineSeparator(), sb.toString());
        }

        return result;
    }
}
