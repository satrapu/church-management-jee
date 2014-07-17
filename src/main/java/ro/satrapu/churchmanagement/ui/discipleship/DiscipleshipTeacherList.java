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
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.DiscipleshipTeacher;
import ro.satrapu.churchmanagement.persistence.Person;

/**
 * Displays a list of {@link Person} instances to be marked or unmarked as discipleship teachers.
 *
 * @author satrapu
 */
@Named
@ViewScoped
public class DiscipleshipTeacherList implements Serializable {

    private static final Class<Person> personClazz = Person.class;
    private static final Class<DiscipleshipTeacher> discipleshipTeacherClazz = DiscipleshipTeacher.class;
    private static final long serialVersionUID = 1L;

    @Inject
    PersistenceService persistenceService;

    @Inject
    @LoggerInstance
    Logger logger;

    LazyDataModel<Person> data;
    List<Person> selectedPersons;

    @PostConstruct
    public void init() {
	data = new LazyDataModel<Person>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public List<Person> load(int currentPageIndex, int recordsPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		logger.debug("Loading page #{} containing maximum {} instances of type {} ...", currentPageIndex + 1, recordsPerPage,
			personClazz.getName());
		return persistenceService.fetch(personClazz, currentPageIndex, recordsPerPage);
	    }
	};

	long count = persistenceService.count(personClazz);
	logger.debug("Found {} records", count);
	data.setRowCount(new Long(count).intValue());
    }

    public LazyDataModel<Person> getData() {
	return data;
    }

    public List<Person> getSelectedPersons() {
	return selectedPersons;
    }

    public void setSelectedPersons(List<Person> selectedPersons) {
	this.selectedPersons = selectedPersons;
    }

    public void markCurrentSelectionAsTeachers(ActionEvent actionEvent) {
	if (selectedPersons != null && selectedPersons.size() > 0) {
	    List<DiscipleshipTeacher> discipleshipTeachers = new ArrayList<>(selectedPersons.size());

	    for (Person person : selectedPersons) {
		DiscipleshipTeacher discipleshipTeacher = persistenceService.fetch(discipleshipTeacherClazz, person.getId());

		if (discipleshipTeacher == null) {
		    discipleshipTeacher = new DiscipleshipTeacher();
		    discipleshipTeacher.setPerson(person);
		    discipleshipTeachers.add(discipleshipTeacher);
		}
	    }

	    if (!discipleshipTeachers.isEmpty()) {
		persistenceService.persist(discipleshipTeachers);
	    }
	}
    }

    public void unmarkCurrentSelectionAsTeachers(ActionEvent actionEvent) {
	if (selectedPersons != null && selectedPersons.size() > 0) {
	    List<DiscipleshipTeacher> discipleshipTeachers = new ArrayList<>(selectedPersons.size());

	    for (Person person : selectedPersons) {
		DiscipleshipTeacher discipleshipTeacher = persistenceService.fetch(discipleshipTeacherClazz, person.getId());

		if (discipleshipTeacher != null) {
		    discipleshipTeachers.add(discipleshipTeacher);
		}
	    }

	    if (!discipleshipTeachers.isEmpty()) {
		persistenceService.remove(discipleshipTeachers);
	    }
	}
    }
}
