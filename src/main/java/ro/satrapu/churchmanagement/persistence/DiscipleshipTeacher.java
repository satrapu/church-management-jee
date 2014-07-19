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
package ro.satrapu.churchmanagement.persistence;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a {@link Person} who is available to act in a church as a discipleship teacher.
 *
 * @author satrapu
 */
@Entity
@Table(name = "Discipleship_Teachers")
@Data
@EqualsAndHashCode(exclude = "person")
@ToString(callSuper = true, exclude = "person")
//excluded person field from lombok's @EqualsAndHashCode and @ToString annotations to avoid a StackOverflowError 
//caused by a circular reference between a teacher and a person, 
//as detaild here: https://groups.google.com/forum/#!topic/project-lombok/Xr13lPinsvg
public class DiscipleshipTeacher implements ManagedEntity {

    private static final long serialVersionUID = 1L;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @Id
    private Person person;

    @Override
    public Serializable getId() {
	return person.getId();
    }

    @Override
    public Long getVersion() {
	return person.getVersion();
    }
}
