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
import ro.satrapu.churchmanagement.persistence.DiscipleshipTeacher;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.query.EntityQuery;
import static org.torpedoquery.jpa.Torpedo.*;
import ro.satrapu.churchmanagement.model.EmailAddress;
import ro.satrapu.churchmanagement.model.NamePart;

/**
 *
 * @author satrapu
 */
public class DiscipleshipTeachersQuery implements EntityQuery<DiscipleshipTeacherInfo> {

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
	DiscipleshipTeacher discipleshipTeacher = leftJoin(person.getDiscipleshipTeacher());
	Query<Object[]> torpedoQuery = select(person.getFirstName().getValue(), person.getMiddleName().getValue(),
		person.getLastName().getValue(), person.getEmailAddress().getValue(), discipleshipTeacher.getId() != null);

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
	    discipleshipTeacherInfo.setFirstName(new NamePart(record[0].toString()));
	    discipleshipTeacherInfo.setMiddleName(new NamePart(record[1].toString()));
	    discipleshipTeacherInfo.setLastName(new NamePart(record[2].toString()));
	    discipleshipTeacherInfo.setEmailAddress(new EmailAddress(record[3].toString()));
	    discipleshipTeacherInfo.setTeacher(record[4] == null ? false : Boolean.parseBoolean(record[4].toString()));

	    result.add(discipleshipTeacherInfo);
	}

	return result;
    }

}
