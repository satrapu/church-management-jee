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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import ro.satrapu.churchmanagement.model.discipleship.DiscipleshipTeacherInfo;
import ro.satrapu.churchmanagement.model.text.StringWrapperExtensions;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.query.EntityCountQuery;
import ro.satrapu.churchmanagement.persistence.query.EntityQuery;

/**
 *
 * @author satrapu
 */
public class DiscipleshipTeachersQuery implements EntityQuery<DiscipleshipTeacherInfo>, EntityCountQuery {

    @Override
    public List<DiscipleshipTeacherInfo> list(EntityManager entityManager, Integer firstResult, Integer maxResults) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Person> criteriaQuery = criteriaBuilder.createQuery(Person.class);
	TypedQuery<Person> typedQuery = entityManager.createQuery(criteriaQuery);
	List<Person> persons = typedQuery.getResultList();
	List<DiscipleshipTeacherInfo> result = new ArrayList<>(persons.size());

	for (Person localPerson : persons) {
	    DiscipleshipTeacherInfo discipleshipTeacherInfo = new DiscipleshipTeacherInfo();
	    discipleshipTeacherInfo.setAvailableAsTeacher(localPerson.getDiscipleshipTeacher() != null);
	    discipleshipTeacherInfo.setPersonEmailAddress(localPerson.getEmailAddress().getValue());
	    discipleshipTeacherInfo.setPersonId(localPerson.getId());
	    discipleshipTeacherInfo.setPersonName(
		    StringWrapperExtensions.join(localPerson.getFirstName(), localPerson.getMiddleName(), localPerson.getLastName()).getValue());
	    result.add(discipleshipTeacherInfo);
	}

	return result;
    }

    @Override
    public Long count(EntityManager entityManager) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
	criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Person.class)));

	Long result = entityManager.createQuery(criteriaQuery).getSingleResult();
	return result;
    }
}
