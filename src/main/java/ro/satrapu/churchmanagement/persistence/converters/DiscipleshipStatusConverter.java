/*
 * Copyright 2015 crossprogramming.
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
package ro.satrapu.churchmanagement.persistence.converters;

import javax.persistence.AttributeConverter;
import ro.satrapu.churchmanagement.persistence.DiscipleshipStatus;

/**
 *
 * @author satrapu
 */
public class DiscipleshipStatusConverter implements AttributeConverter<DiscipleshipStatus, String> {

    @Override
    public String convertToDatabaseColumn(DiscipleshipStatus enumValue) {
	switch (enumValue) {
	    case DISCIPLESHIP_CANDIDATE:
		return "D";
	    case NOT_INTERESTED:
		return "N";
	    case TEACHING_CANDIDATE:
		return "T";
	    default:
		throw new IllegalArgumentException("Unknown " + enumValue);
	}
    }

    @Override
    public DiscipleshipStatus convertToEntityAttribute(String databaseValue) {
	switch (databaseValue) {
	    case "D":
		return DiscipleshipStatus.DISCIPLESHIP_CANDIDATE;
	    case "N":
		return DiscipleshipStatus.NOT_INTERESTED;
	    case "T":
		return DiscipleshipStatus.TEACHING_CANDIDATE;
	    default:
		throw new IllegalArgumentException("Unknown " + databaseValue);
	}
    }
}
