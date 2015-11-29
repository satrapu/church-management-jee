/*
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

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ro.satrapu.churchmanagement.model.EmailAddress;
import ro.satrapu.churchmanagement.model.NamePart;
import ro.satrapu.churchmanagement.validation.Email;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents a person attending a church, either as a member or not.
 *
 * @author satrapu
 */
@Entity
@Table(name = "Persons")
@Data
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    @Setter(AccessLevel.PRIVATE)
    private Integer id;

    @Version
    @Column(name = "Version")
    @Setter(AccessLevel.PRIVATE)
    private Integer version;

    @NotNull
    @Column(name = "FirstName", nullable = false, length = NamePart.MAX_LENGTH)
    private String firstName;

    @Column(name = "MiddleName", nullable = true, length = NamePart.MAX_LENGTH)
    private String middleName;

    @NotNull
    @Column(name = "LastName", nullable = false, length = NamePart.MAX_LENGTH)
    private String lastName;

    @NotNull
    @Column(name = "EmailAddress", nullable = false, length = EmailAddress.MAX_LENGTH, unique = true)
    @Email
    private String emailAddress;
}
