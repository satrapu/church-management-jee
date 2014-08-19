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
import ro.satrapu.churchmanagement.model.EmailAddress;
import ro.satrapu.churchmanagement.model.NamePart;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ro.satrapu.churchmanagement.model.text.StringWrapperExtensions;

/**
 * Represents a person attending a church, either as a member or not.
 *
 * @author satrapu
 */
@Entity
@Table(name = "Persons", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"EmailAddress"}, name = "UK_Persons_EmailAddress")
})
@Data
@EqualsAndHashCode
@ToString
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of {@link Person} class.
     */
    public Person() {
	super();

	firstName = new NamePart();
	middleName = new NamePart();
	lastName = new NamePart();
	emailAddress = new EmailAddress();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Version
    @Column(name = "Version")
    private Integer version;

    @NotNull
    @Embedded
    @AttributeOverrides(value = {
	@AttributeOverride(name = "value", column = @Column(name = "FirstName", nullable = false, length = NamePart.MAX_LENGTH))})
    private NamePart firstName;

    @Embedded
    @AttributeOverrides(value = {
	@AttributeOverride(name = "value", column = @Column(name = "MiddleName", nullable = true, length = NamePart.MAX_LENGTH))})
    private NamePart middleName;

    @NotNull
    @Embedded
    @AttributeOverrides(value = {
	@AttributeOverride(name = "value", column = @Column(name = "LastName", nullable = false, length = NamePart.MAX_LENGTH))
    })
    private NamePart lastName;

    @NotNull
    @Embedded
    @AttributeOverrides(value = {
	@AttributeOverride(name = "value", column = @Column(name = "EmailAddress", nullable = false, length = EmailAddress.MAX_LENGTH))
    })
    private EmailAddress emailAddress;

    @OneToOne(fetch = FetchType.LAZY, optional = true, mappedBy = "person")
    private DiscipleshipTeacher discipleshipTeacher;

    public boolean hasFirstName() {
	return !StringWrapperExtensions.isNullOrWhitespace(firstName);
    }

    public boolean hasMiddleName() {
	return !StringWrapperExtensions.isNullOrWhitespace(middleName);
    }

    public boolean hasLastName() {
	return !StringWrapperExtensions.isNullOrWhitespace(lastName);
    }

    public boolean hasEmailAddress() {
	return !StringWrapperExtensions.isNullOrWhitespace(emailAddress);
    }
}
