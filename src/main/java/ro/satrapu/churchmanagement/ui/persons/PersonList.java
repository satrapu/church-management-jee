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

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;

/**
 * Displays a list of {@link Person} instances matching a given criteria.
 *
 * @author satrapu
 */
@Model
public class PersonList {

    private static final long serialVersionUID = 1L;

    @Inject
    PersistenceService persistenceService;

    @Inject
    @LoggerInstance
    Logger logger;

    LazyDataModel<Person> data;
    private static final Class<Person> clazz = Person.class;

    @PostConstruct
    public void init() {
	data = new LazyDataModel<Person>() {
	    private static final long serialVersionUID = 1L;

	    @Override
	    public List<Person> load(int currentPageIndex, int recordsPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
		logger.debug("Loading page #{} containing maximum {} instances of type {} ...", currentPageIndex + 1, recordsPerPage,
			clazz.getName());
		return persistenceService.fetch(clazz, currentPageIndex, recordsPerPage);
	    }
	};

	long count = persistenceService.count(clazz);
	logger.debug("Found {} records", count);
	data.setRowCount(new Long(count).intValue());
    }

    public LazyDataModel<Person> getData() {
	return data;
    }
}
