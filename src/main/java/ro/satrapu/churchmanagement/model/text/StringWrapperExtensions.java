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
package ro.satrapu.churchmanagement.model.text;

import ro.satrapu.churchmanagement.model.StringWrapper;

/**
 * Contains extension methods applicable to {@link StringWrapper} instances.
 *
 * @author satrapu
 */
public class StringWrapperExtensions {

    public static boolean isNullOrEmpty(StringWrapper stringWrapper) {
	if (stringWrapper == null) {
	    return true;
	}

	return StringExtensions.isNullOrEmpty(stringWrapper.getValue());
    }

    public static boolean isNullOrWhitespace(StringWrapper stringWrapper) {
	if (stringWrapper == null) {
	    return true;
	}

	return StringExtensions.isNullOrWhitespace(stringWrapper.getValue());
    }
}
