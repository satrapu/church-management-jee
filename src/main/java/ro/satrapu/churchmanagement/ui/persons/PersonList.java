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
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.model.text.StringExtensions;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.QuerySearchResult;
import ro.satrapu.churchmanagement.persistence.queries.PersonQuery;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
    private Logger logger;
    private LazyDataModel<Person> dataModel;

    @Inject
    @Valid
    public PersonList(@NotNull PersistenceService persistenceService,
                      @NotNull Logger logger) {
        this.persistenceService = persistenceService;
        this.logger = logger;
    }

    @PostConstruct
    public void init() {
        logger.debug("Fetching the first page of entities ...");
        dataModel = new LazyDataModel<Person>() {
            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                PersonQuery personQuery = getQuery(sortField, sortOrder, filters);
                QuerySearchResult<Person> querySearchResult = persistenceService.fetch(personQuery, first, pageSize);

                List<Person> result = querySearchResult.getRecords();

                if (logger.isDebugEnabled()) {
                    logger.debug("Fetched {} entities", result.size());
                }

                return result;
            }
        };

        PersonQuery personQuery = getQuery(null, null, null);
        long count = persistenceService.count(personQuery);
        logger.debug("Found a total of {} entities", count);
        dataModel.setRowCount(Long.valueOf(count).intValue());
    }

    public LazyDataModel<Person> getDataModel() {
        return dataModel;
    }

    private PersonQuery getQuery(String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        logger.debug("Building the query used for fetching entities ...");
        PersonQuery result = new PersonQuery();

        if (filters != null) {
            for (Map.Entry<String, Object> entry : filters.entrySet()) {
                logger.debug("Filtering entities by key: {} and value: {}", entry.getKey(), entry.getValue());

                switch (entry.getKey()) {
                    case KEY_EMAIL_ADDRESS:
                        result.setEmailAddressNamePattern(entry.getValue().toString());
                        break;
                    case KEY_ID:
                        result.setId(Integer.parseInt(entry.getValue().toString()));
                        break;
                    case KEY_FIRST_NAME:
                        result.setFirstNamePattern(entry.getValue().toString());
                        break;
                    case KEY_LAST_NAME:
                        result.setLastNamePattern(entry.getValue().toString());
                        break;
                    case KEY_MIDDLE_NAME:
                        result.setMiddleNamePattern(entry.getValue().toString());
                        break;
                }
            }
        }

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

            logger.debug("Entities will be sorted by field: {}, using order: {}", sortField, sortOrder);
        }

        return result;
    }
}