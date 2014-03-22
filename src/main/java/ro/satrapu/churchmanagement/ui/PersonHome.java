/*
 * Copyright 2014 Satrapu.
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
package ro.satrapu.churchmanagement.ui;

import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;

/**
 *
 * @author Satrapu
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
    private static final transient Logger logger = LoggerFactory.getLogger(PersonHome.class);
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
     * Loads an entity based on the value returned by the
     * {@link EntityHome#getId()} method.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Person loadInstance() {
        logger.debug("Loading instance: {} using id: {}", Person.class, getId());
        return persistenceService.fetch(Person.class, getId());
    }

    /**
     * Creates a new entity.
     *
     * @return A new entity.
     */
    @SuppressWarnings("unchecked")
    public Person createInstance() {
        logger.debug("Creating instance: {}", Person.class);
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
     * Saves the changes of the current entity to the underlying persistent
     * storage.
     *
     * @return The operation outcome, if successful; null, otherwise.
     */
    public String save() {
        if (isManaged()) {
            try {
                logger.debug("Merging instance: {}", getInstance());
                persistenceService.merge(getInstance());
                conversation.end();
            } catch (Exception e) {
                logger.error("Could not merge instance", e);
            }
        } else {
            try {
                logger.debug("Persisting instance: {}", getInstance());
                persistenceService.persist(getInstance());
                conversation.end();
            } catch (Exception e) {
                logger.error("Could not persist instance", e);
            }
        }

        return null;
    }

    /**
     * Cancels the current operation and closes the current conversation.
     *
     * @return The operation outcome.
     */
    public String cancel() {
        logger.debug("Cancel editor");
        conversation.end();
        return null;
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
        logger.debug("Removing instance: {}", getInstance());

        try {
            persistenceService.remove(getInstance());
            conversation.end();
        } catch (Exception e) {
            logger.error("Could not remove instance", e);
        }

        return null;
    }
}
