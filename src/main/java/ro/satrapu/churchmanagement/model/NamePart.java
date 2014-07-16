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
package ro.satrapu.churchmanagement.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents a part of a name, like first name or last name.
 *
 * @author satrapu
 */
@Embeddable
@Data
@EqualsAndHashCode
@ToString
public class NamePart implements Serializable, StringWrapper {

    private static final long serialVersionUID = 1L;
    public static final int MAX_LENGTH = 400;
    public static final int MIN_LENGTH = 1;

    @Size(min = MIN_LENGTH, max = MAX_LENGTH)
    private String value;

    /**
     * Default constructor.
     */
    public NamePart() {
    }

    /**
     * Convenience constructor.
     *
     * @param value
     */
    public NamePart(String value) {
	this.value = value;
    }
}
