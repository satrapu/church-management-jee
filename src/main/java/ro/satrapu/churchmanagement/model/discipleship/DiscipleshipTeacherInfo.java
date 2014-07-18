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
package ro.satrapu.churchmanagement.model.discipleship;

import lombok.Data;
import ro.satrapu.churchmanagement.model.EmailAddress;
import ro.satrapu.churchmanagement.model.NamePart;

/**
 *
 * @author satrapu
 */
@Data
public class DiscipleshipTeacherInfo {

    private Long id;
    private NamePart firstName;
    private NamePart middleName;
    private NamePart lastName;
    private EmailAddress emailAddress;
    private boolean isTeacher;
}
