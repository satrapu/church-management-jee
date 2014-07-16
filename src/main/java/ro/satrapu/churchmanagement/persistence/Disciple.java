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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a {@link Person} who is available to act in a church as a disciple.
 *
 * @author satrapu
 */
@Entity
@Table(name = "Discipleship_Disciples")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Disciple extends ManagedEntityBase {

    private static final long serialVersionUID = 1L;

    @MapsId
    @OneToOne
    @JoinColumn(name = "Id")
    private Person person;
}
