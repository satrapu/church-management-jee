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
import ro.satrapu.churchmanagement.persistence.PaginatedQuerySearchResult;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.queries.PersonQuery;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Displays a list of {@link Person} instances matching a given criteria.
 */
@Model
public class PersonList extends LazyDataModel<Person> {
    private final PersistenceService persistenceService;

    @Inject
    public PersonList(@NotNull PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @PostConstruct
    public void init() {
        PersonQuery personQuery = new PersonQuery();
        long total = persistenceService.count(personQuery);
        super.setRowCount(Long.valueOf(total).intValue());
    }

    @Override
    public List<Person> load(int firstResult, int maxResults, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        PersonQuery personQuery = new PersonQuery();
        PaginatedQuerySearchResult<Person> paginatedQuerySearchResult = persistenceService.fetch(personQuery, firstResult, maxResults);
        super.setRowCount(Long.valueOf(paginatedQuerySearchResult.getTotalRecords()).intValue());

        List<Person> result = paginatedQuerySearchResult.getRecords();
        return result;
    }

    public LazyDataModel<Person> getData() {
        return this;
    }
}