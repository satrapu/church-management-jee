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
package ro.satrapu.churchmanagement.persistence.query.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.torpedoquery.jpa.Query;
import ro.satrapu.churchmanagement.model.discipleship.DiscipleshipTeacherInfo;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.query.EntityQuery;
import static org.torpedoquery.jpa.Torpedo.*;
import ro.satrapu.churchmanagement.model.text.StringExtensions;
import ro.satrapu.churchmanagement.persistence.query.EntityCountQuery;

/**
 *
 * @author satrapu
 */
public class DiscipleshipTeachersQuery implements EntityQuery<DiscipleshipTeacherInfo>, EntityCountQuery {

    private static final int COLUMN_INDEX_PERSON_ID = 0;
    private static final int COLUMN_INDEX_PERSON_FIRST_NAME = 1;
    private static final int COLUMN_INDEX_PERSON_MIDDLE_NAME = 2;
    private static final int COLUMN_INDEX_PERSON_LAST_NAME = 3;
    private static final int COLUMN_INDEX_PERSON_EMAIL_ADDRESS = 4;
    private static final int COLUMN_INDEX_TEACHER_ID = 5;

    /**
     *
     * @param entityManager
     * @param firstResult
     * @param maxResults
     * @return
     */
    @Override
    public List<DiscipleshipTeacherInfo> list(EntityManager entityManager, Integer firstResult, Integer maxResults) {
	Person person = from(Person.class);
	Query<Object[]> torpedoQuery = select(person.getId(), person.getFirstName().getValue(), person.getMiddleName().getValue(),
		person.getLastName().getValue(), person.getEmailAddress().getValue(), person.getDiscipleshipTeacher());

	if (firstResult != null) {
	    torpedoQuery.setFirstResult(firstResult);
	}

	if (maxResults != null) {
	    torpedoQuery.setMaxResults(maxResults);
	}

	List<Object[]> records = torpedoQuery.list(entityManager);
	List<DiscipleshipTeacherInfo> result = new ArrayList<>(records.size());

	for (Object[] record : records) {
	    DiscipleshipTeacherInfo discipleshipTeacherInfo = new DiscipleshipTeacherInfo();
	    discipleshipTeacherInfo.setPersonId(Long.parseLong(record[COLUMN_INDEX_PERSON_ID].toString()));
	    discipleshipTeacherInfo.setPersonName(StringExtensions.join(record[COLUMN_INDEX_PERSON_FIRST_NAME].toString(),
		    record[COLUMN_INDEX_PERSON_MIDDLE_NAME].toString(), record[COLUMN_INDEX_PERSON_LAST_NAME].toString()));
	    discipleshipTeacherInfo.setPersonEmailAddress(record[COLUMN_INDEX_PERSON_EMAIL_ADDRESS].toString());
	    discipleshipTeacherInfo.setAvailableAsTeacher(record[COLUMN_INDEX_TEACHER_ID] != null);

	    result.add(discipleshipTeacherInfo);
	}

	return result;
    }

    @Override
    public Long count(EntityManager entityManager) {
	Person person = from(Person.class);
	Query<Long> torpedoQuery = select(org.torpedoquery.jpa.Torpedo.count(person.getId()));
	List<Long> result = torpedoQuery.list(entityManager);

	return result.get(0);
    }
}
