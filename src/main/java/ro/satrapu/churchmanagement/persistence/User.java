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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a Church Management application user.
 *
 * @author satrapu
 */
@Entity
@Table(name = "Users", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"EmailAddress"}, name = "UK_Users_EmailAddress")
})
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer id;

    @Version
    @Column(name = "Version")
    private Integer version;

    /**
     * Creates a new instance of {@link User} class.
     */
    public User() {
	super();

	firstName = new NamePart();
	middleName = new NamePart();
	lastName = new NamePart();
	emailAddress = new EmailAddress();
    }

    @NotNull
    @Size(min = 1, max = 400)
    @Column(nullable = false, length = 400, name = "UserName")
    private String userName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100, name = "Password")
    private String password;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(nullable = false, length = 100, name = "Salt")
    private String salt;

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
}
