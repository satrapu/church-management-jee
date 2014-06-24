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

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.ui.messages.Messages;
import ro.satrapu.churchmanagement.ui.Urls;

/**
 * Manages a specific {@link Person} instance.
 *
 * @see <a href="http://www.andygibson.net/blog/tutorial/pattern-for-conversational-crud-in-java-ee-6">
 * Conversational CRUD in Java EE 6</a> by Andy Gibson.
 * @author satrapu
 */
@Named
@ConversationScoped
public class PersonHome implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    PersistenceService persistenceService;

    @Inject
    Conversation conversation;

    @Inject
    Messages messages;

    @Inject
    @LoggerInstance
    Logger logger;

    private Serializable id;
    private Person instance;

    /**
     * Gets the entity managed by this instance.
     *
     * @return An entity managed by this instance.
     */
    public Person getInstance() {
	if (instance == null) {
	    if (id != null) {
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
	this.id = id;
    }

    /**
     * Loads an entity based on the value returned by the {@link EntityHome#getId()} method.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Person loadInstance() {
	Class<Person> clazz = Person.class;
	logger.debug("Loading instance of type {} using id {} ...", clazz.getName(), id);
	return persistenceService.fetch(clazz, id);
    }

    /**
     * Creates a new entity.
     *
     * @return A new entity.
     */
    @SuppressWarnings("unchecked")
    public Person createInstance() {
	logger.debug("Creating entity of type {} ...", Person.class.getName());
	return new Person();
    }

    /**
     * Gets whether the current entity is managed or not.
     *
     * @return True, if the entity is managed; false, otherwise.
     */
    public boolean isManaged() {
	return getInstance().getId() != null;
    }

    /**
     * Saves the changes of the current entity to the underlying persistent storage.
     *
     * @return The operation outcome, if successful; null, otherwise.
     */
    public String save() {
	boolean hasErrors = true;

	if (!isValid()) {
	    messages.error("global.fields.invalid");
	} else {
	    if (isManaged()) {
		Person person = getInstance();

		try {
		    logger.debug("Merging instance: {} ...", person);
		    persistenceService.merge(person);
		    messages.info("entities.person.actions.update.success");
		    hasErrors = false;
		} catch (Exception e) {
		    logger.error("Could not merge instance", e);
		    messages.error("entities.person.actions.update.failure");
		}
	    } else {
		Person person = getInstance();

		try {
		    logger.debug("Persisting instance: {} ...", person);
		    persistenceService.persist(person);
		    messages.info("entities.person.actions.save.success");
		    hasErrors = false;
		} catch (Exception e) {
		    logger.error("Could not persist instance", e);
		    messages.error("entities.person.actions.save.failure");
		}
	    }
	}

	if (hasErrors) {
	    return null;
	} else {
	    conversation.end();
	    return Urls.Secured.Persons.LIST;
	}
    }

    /**
     * Cancels the current operation and closes the current conversation.
     *
     * @return The operation outcome.
     */
    public String cancel() {
	logger.debug("Cancelling editing instance ...");
	conversation.end();
	return Urls.Secured.Persons.LIST;
    }

    /**
     * Initializes a long-running conversation.
     */
    public void beginConversation() {
	if (conversation.isTransient()) {
	    conversation.begin();
	    logger.debug("Starting conversation with id: {}", conversation.getId());
	}
    }

    /**
     * Removes the current entity from the underlying persistent storage.
     *
     * @return The operation outcome, if successful; null, otherwise.
     */
    public String remove() {
	boolean hasErrors = true;
	Person person = getInstance();
	logger.debug("Removing instance: {} ...", person);

	try {
	    persistenceService.remove(person);
	    messages.info("entities.person.actions.remove.success");
	    hasErrors = false;
	} catch (Exception e) {
	    logger.error("Could not remove instance", e);
	    messages.error("entities.person.actions.remove.failure");
	}

	if (!hasErrors) {
	    conversation.end();
	    return Urls.Secured.Persons.LIST;
	}

	return null;
    }

    /**
     * Checks whether the current edited instance is valid or not.
     *
     * @return True, if the instance is valid; false, otherwise.
     */
    private boolean isValid() {
	boolean result = true;
	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();
	Set<ConstraintViolation<Person>> constraintViolations = validator.validate(getInstance());

	if (constraintViolations.size() > 0) {
	    StringBuilder sb = new StringBuilder();
	    Iterator<ConstraintViolation<Person>> iterator = constraintViolations.iterator();

	    while (iterator.hasNext()) {
		ConstraintViolation<Person> constraintViolation = iterator.next();
		sb.append(MessageFormat.format("{0}: {1}{2}",
			constraintViolation.getPropertyPath(), constraintViolation.getMessage(), System.lineSeparator()));
		result = false;
	    }

	    logger.error("Encountered an invalid Person instance: {}{}", System.lineSeparator(), sb.toString());
	}

	return result;
    }
}
