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

import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ro.satrapu.churchmanagement.persistence.PersistenceService;
import ro.satrapu.churchmanagement.persistence.Person;

/**
 *
 * @author Satrapu
 */
@Model
public class PersonList {

    private static final long serialVersionUID = 1L;
    @Inject
    private PersistenceService persistenceService;
    private LazyDataModel<Person> data;
    private static final Class<Person> clazz = Person.class;

    @PostConstruct
    public void init() {
        data = new LazyDataModel<Person>() {

            @Override
            public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                return persistenceService.fetch(clazz, first, pageSize);
            }
        };
        data.setRowCount(new Long(persistenceService.count(clazz)).intValue());
    }

    public LazyDataModel getData() {
        return data;
    }

    public boolean isDataAvailable() {
        return data.getRowCount() > 0;
    }
}
