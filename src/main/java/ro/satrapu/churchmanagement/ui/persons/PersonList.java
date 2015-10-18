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

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ro.satrapu.churchmanagement.model.text.StringExtensions;
import ro.satrapu.churchmanagement.persistence.PaginatedQuerySearchResult;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.queries.PersonQuery;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Displays a list of {@link Person} instances matching a given criteria.
 */
@Named
@ViewScoped
public class PersonList implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_MIDDLE_NAME = "middleName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_EMAIL_ADDRESS = "emailAddress";
    private final PersistenceService persistenceService;
    private LazyDataModel<Person> dataModel;

    @Inject
    public PersonList(@NotNull PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    public void init() {
        dataModel = new LazyDataModel<Person>() {
            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
                PersonQuery personQuery = getQuery(sortField, sortOrder, filters);
                PaginatedQuerySearchResult<Person> paginatedQuerySearchResult = persistenceService.fetch(personQuery, first, pageSize);

                List<Person> result = paginatedQuerySearchResult.getRecords();
                return result;
            }
        };

        PersonQuery personQuery = getQuery(null, null, null);
        long count = persistenceService.count(personQuery);
        dataModel.setRowCount(Long.valueOf(count).intValue());
    }

    public LazyDataModel<Person> getDataModel() {
        return dataModel;
    }

    private PersonQuery getQuery(String sortField, SortOrder sortOrder, Map<String, String> filters) {
        PersonQuery result = new PersonQuery();

        if (!StringExtensions.isNullOrWhitespace(sortField)) {
            result.setSortAscending(sortOrder == SortOrder.ASCENDING);

            switch (sortField) {
                case KEY_EMAIL_ADDRESS:
                    result.setSortByField(PersonQuery.Fields.EMAIL_ADDRESS);
                    break;
                case KEY_ID:
                    result.setSortByField(PersonQuery.Fields.ID);
                    break;
                case KEY_FIRST_NAME:
                    result.setSortByField(PersonQuery.Fields.FIRST_NAME);
                    break;
                case KEY_LAST_NAME:
                    result.setSortByField(PersonQuery.Fields.LAST_NAME);
                    break;
                case KEY_MIDDLE_NAME:
                    result.setSortByField(PersonQuery.Fields.MIDDLE_NAME);
                    break;
            }
        }

        if (filters != null) {
            for (Iterator<Map.Entry<String, String>> iterator = filters.entrySet().iterator(); iterator.hasNext(); ) {
                Map.Entry<String, String> entry = iterator.next();

                switch (entry.getKey()) {
                    case KEY_EMAIL_ADDRESS:
                        result.setEmailAddressNamePattern(entry.getValue());
                        break;
                    case KEY_ID:
                        result.setId(Integer.parseInt(entry.getValue()));
                        break;
                    case KEY_FIRST_NAME:
                        result.setFirstNamePattern(entry.getValue());
                        break;
                    case KEY_LAST_NAME:
                        result.setLastNamePattern(entry.getValue());
                        break;
                    case KEY_MIDDLE_NAME:
                        result.setMiddleNamePattern(entry.getValue());
                        break;
                }
            }
        }

        return result;
    }
}