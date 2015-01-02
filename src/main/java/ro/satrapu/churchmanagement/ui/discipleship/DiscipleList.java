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
package ro.satrapu.churchmanagement.ui.discipleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;
import ro.satrapu.churchmanagement.model.discipleship.DiscipleInfo;
import ro.satrapu.churchmanagement.persistence.Disciple;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.query.impl.DisciplesQuery;
import ro.satrapu.churchmanagement.ui.messages.Messages;

/**
 * Displays a list of {@link Person} instances to be marked or unmarked as available for discipleship.
 *
 * @author satrapu
 */
@Named
@ViewScoped
public class DiscipleList implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Class<DiscipleInfo> discipleInfoClazz = DiscipleInfo.class;
    private static final Class<Disciple> discipleClazz = Disciple.class;
    private static final Class<Person> personClazz = Person.class;

    @Inject
    PersistenceService persistenceService;

    @Inject
    @LoggerInstance
    Logger logger;

    @Inject
    Messages messages;

    LazyDataModel<DiscipleInfo> data;
    List<DiscipleInfo> selectedPersons;

    @PostConstruct
    public void init() {
	final DisciplesQuery query = new DisciplesQuery();
	data = new LazyDataModel<DiscipleInfo>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public List<DiscipleInfo> load(int currentPageIndex, int recordsPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		logger.debug("Loading page #{} containing maximum {} instances of type {} ...", currentPageIndex + 1, recordsPerPage, discipleInfoClazz.getCanonicalName());
		return persistenceService.fetch(query, currentPageIndex, recordsPerPage);
	    }
	};

	long count = persistenceService.count(query);
	data.setRowCount(new Long(count).intValue());
    }

    public LazyDataModel<DiscipleInfo> getData() {
	return data;
    }

    public List<DiscipleInfo> getSelectedPersons() {
	return selectedPersons;
    }

    public void setSelectedPersons(List<DiscipleInfo> selectedPersons) {
	this.selectedPersons = selectedPersons;
    }

    public void markCurrentSelectionAsDisciples(ActionEvent actionEvent) {
	if (selectedPersons == null) {
	    return;
	}

	if (selectedPersons.isEmpty()) {
	    return;
	}

	try {
	    List<Disciple> disciples = new ArrayList<>();

	    for (DiscipleInfo selectedPerson : selectedPersons) {
		if (!selectedPerson.isAvailableAsDisciple()) {
		    Person person = persistenceService.fetchReference(personClazz, selectedPerson.getPersonId());

		    Disciple disciple = new Disciple();
		    disciple.setPerson(person);

		    disciples.add(disciple);
		}
	    }

	    if (!disciples.isEmpty()) {
		List<Disciple> persistedDisciples = persistenceService.persist(disciples);

		if (disciples.size() > 1) {
		    messages.info("pages.discipleship.availabilityAsDisciples.actions.markAsAvailable.success.many", persistedDisciples.size());
		} else {
		    messages.info("pages.discipleship.availabilityAsDisciples.actions.markAsAvailable.success.single");
		}
	    } else {
		messages.warning("pages.discipleship.availabilityAsDisciples.actions.markAsAvailable.warning.none");
	    }
	} catch (Exception e) {
	    logger.error("markCurrentSelectionAsDisciples - ERROR", e);
	    messages.error("pages.discipleship.availabilityAsDisciples.actions.markAsAvailable.failure");
	}
    }

    public void unmarkCurrentSelectionAsDisciples(ActionEvent actionEvent) {
	if (selectedPersons == null) {
	    return;
	}

	if (selectedPersons.isEmpty()) {
	    return;
	}

	try {
	    List<Disciple> disciples = new ArrayList<>();

	    for (DiscipleInfo selectedPerson : selectedPersons) {
		if (selectedPerson.isAvailableAsDisciple()) {
		    Disciple disciple = persistenceService.fetchReference(discipleClazz, selectedPerson.getDiscipleId());
		    disciples.add(disciple);
		}
	    }

	    if (!disciples.isEmpty()) {
		persistenceService.remove(disciples);

		if (disciples.size() > 1) {
		    messages.info("pages.discipleship.availabilityAsDisciples.actions.markAsUnavailable.success.many", disciples.size());
		} else {
		    messages.info("pages.discipleship.availabilityAsDisciples.actions.markAsUnavailable.success.single");
		}
	    } else {
		messages.warning("pages.discipleship.availabilityAsDisciples.actions.markAsUnavailable.warning.none");
	    }
	} catch (Exception e) {
	    logger.error("unmarkCurrentSelectionAsDisciples - ERROR", e);
	    messages.error("pages.discipleship.availabilityAsDisciples.actions.markAsUnavailable.failure");
	}
    }
}
