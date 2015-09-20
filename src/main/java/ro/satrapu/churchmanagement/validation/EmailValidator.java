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
package ro.satrapu.churchmanagement.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Checks whether a value of type {@link String} represents a valid e-mail address or not.
 *
 * @author satrapu
 */
public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final String EMAIL_PATTERN_VALUE = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
            + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
            + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN_VALUE, Pattern.CASE_INSENSITIVE & Pattern.UNICODE_CASE);

    @Override
    public void initialize(Email constraintAnnotation) {
        //nothing to init
    }

    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext context) {
        boolean isEmailValid = true;

        if (emailAddress != null && !emailAddress.isEmpty()) {
            isEmailValid = emailPattern.matcher(emailAddress).matches();
        }

        return isEmailValid;
    }
}
