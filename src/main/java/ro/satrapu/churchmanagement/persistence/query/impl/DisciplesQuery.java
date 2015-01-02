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
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import ro.satrapu.churchmanagement.model.EmailAddress;
import ro.satrapu.churchmanagement.model.NamePart;
import ro.satrapu.churchmanagement.model.discipleship.DiscipleInfo;
import ro.satrapu.churchmanagement.model.text.StringWrapperExtensions;
import ro.satrapu.churchmanagement.persistence.Disciple;
import ro.satrapu.churchmanagement.persistence.Disciple_;
import ro.satrapu.churchmanagement.persistence.Person;
import ro.satrapu.churchmanagement.persistence.Person_;
import ro.satrapu.churchmanagement.persistence.query.EntityCountQuery;
import ro.satrapu.churchmanagement.persistence.query.EntityQuery;

/**
 *
 * @author satrapu
 */
public class DisciplesQuery implements EntityQuery<DiscipleInfo>, EntityCountQuery {

    @Override
    public List<DiscipleInfo> list(EntityManager entityManager, Integer firstResult, Integer maxResults) {
	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
	Root<Person> person = criteriaQuery.from(Person.class);
	Join<Person, Disciple> disciple = person.join(Person_.disciple, JoinType.LEFT);
	criteriaQuery.where(criteriaBuilder.isNull(person.get(Person_.discipleshipTeacher)));
	criteriaQuery.multiselect(person.get(Person_.id), person.get(Person_.firstName), person.get(Person_.middleName), person.get(Person_.lastName),
		person.get(Person_.emailAddress), disciple.get(Disciple_.id));

	TypedQuery<Tuple> typedQuery = entityManager.createQuery(criteriaQuery);
	typedQuery.setFirstResult(firstResult);
	typedQuery.setMaxResults(maxResults);

	List<Tuple> records = typedQuery.getResultList();
	List<DiscipleInfo> result = new ArrayList<>(records.size());

	for (Tuple record : records) {
	    Integer id = record.get(0, Integer.class);
	    NamePart firstName = record.get(1, NamePart.class);
	    NamePart middleName = record.get(2, NamePart.class);
	    NamePart lastName = record.get(3, NamePart.class);
	    EmailAddress emailAddress = record.get(4, EmailAddress.class);
	    Integer discipleId = record.get(5, Integer.class);

	    DiscipleInfo discipleshipTeacherInfo = new DiscipleInfo();
	    discipleshipTeacherInfo.setDiscipleId(discipleId);
	    discipleshipTeacherInfo.setAvailableAsDisciple(discipleId != null);
	    discipleshipTeacherInfo.setPersonEmailAddress(emailAddress.getValue());
	    discipleshipTeacherInfo.setPersonId(id);
	    discipleshipTeacherInfo.setPersonName(StringWrapperExtensions.join(firstName, middleName, lastName).getValue());

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
