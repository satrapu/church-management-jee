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
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Represents an e-mail address.
 *
 * @author satrapu
 */
@Embeddable
@Data
@EqualsAndHashCode
@ToString
public class EmailAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    //see more details regarding email max length here: http://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
    @Size(min = 1, max = 254)
    @Column(length = 254, name = "EmailAddress")
    private String value;
}