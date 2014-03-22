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
package ro.satrapu.churchmanagement.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Satrapu
 */
@Entity
@Table(name = "Persons")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Person extends ManagedEntity {

    @NotNull
    @Size(min = 2, max = 400)
    @Column(nullable = false, length = 400, name = "FirstName")
    private String firstName;

    @Null
    @Size(min = 2, max = 400)
    @Column(nullable = true, length = 400, name = "MiddleName")
    private String middleName;

    @NotNull
    @Size(min = 2, max = 400)
    @Column(nullable = false, length = 400, name = "LastName")
    private String lastName;
}
