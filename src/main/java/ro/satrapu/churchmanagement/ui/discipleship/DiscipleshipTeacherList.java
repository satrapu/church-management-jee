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
import ro.satrapu.churchmanagement.model.discipleship.DiscipleshipTeacherInfo;
import ro.satrapu.churchmanagement.persistence.DiscipleshipTeacher;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.query.impl.DiscipleshipTeachersQuery;

/**
 * Displays a list of {@link Person} instances to be marked or unmarked as discipleship teachers.
 *
 * @author satrapu
 */
@Named
@ViewScoped
public class DiscipleshipTeacherList implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Class<DiscipleshipTeacherInfo> discipleshipTeacherInfoClazz = DiscipleshipTeacherInfo.class;
    private static final Class<DiscipleshipTeacher> discipleshipTeacherClazz = DiscipleshipTeacher.class;
    private static final Class<Person> personClazz = Person.class;

    @Inject
    PersistenceService persistenceService;

    @Inject
    @LoggerInstance
    Logger logger;

    LazyDataModel<DiscipleshipTeacherInfo> data;
    List<DiscipleshipTeacherInfo> selectedPersons;

    @PostConstruct
    public void init() {
	final DiscipleshipTeachersQuery query = new DiscipleshipTeachersQuery();
	data = new LazyDataModel<DiscipleshipTeacherInfo>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public List<DiscipleshipTeacherInfo> load(int currentPageIndex, int recordsPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		logger.debug("Loading page #{} containing maximum {} instances of type {} ...", currentPageIndex + 1, recordsPerPage,
			discipleshipTeacherInfoClazz.getCanonicalName());
		return persistenceService.fetch(query, currentPageIndex, recordsPerPage);
	    }
	};

	long count = persistenceService.count(query);
	data.setRowCount(new Long(count).intValue());
    }

    public LazyDataModel<DiscipleshipTeacherInfo> getData() {
	return data;
    }

    public List<DiscipleshipTeacherInfo> getSelectedPersons() {
	return selectedPersons;
    }

    public void setSelectedPersons(List<DiscipleshipTeacherInfo> selectedPersons) {
	this.selectedPersons = selectedPersons;
    }

    public void markCurrentSelectionAsTeachers(ActionEvent actionEvent) {
	if (selectedPersons != null && selectedPersons.size() > 0) {
	    List<DiscipleshipTeacher> discipleshipTeachers = new ArrayList<>();

	    for (DiscipleshipTeacherInfo selectedPerson : selectedPersons) {
		if (!selectedPerson.isAvailableAsTeacher()) {
		    Person person = persistenceService.fetchReference(personClazz, selectedPerson.getPersonId());

		    DiscipleshipTeacher discipleshipTeacher = new DiscipleshipTeacher();
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
	    List<DiscipleshipTeacher> discipleshipTeachers = new ArrayList<>();

	    for (DiscipleshipTeacherInfo selectedPerson : selectedPersons) {
		if (selectedPerson.isAvailableAsTeacher()) {
		    DiscipleshipTeacher discipleshipTeacher = persistenceService.fetchReference(discipleshipTeacherClazz, selectedPerson.getPersonId());
		    discipleshipTeachers.add(discipleshipTeacher);
		}
	    }

	    if (!discipleshipTeachers.isEmpty()) {
		persistenceService.remove(discipleshipTeachers);
	    }
	}
    }
}
