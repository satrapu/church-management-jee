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

/**
 * Contains extension methods applicable to {@link String} instances.
 *
 * @author satrapu
 */
public class StringExtensions {

    /**
     * Checks whether the given {@code value} parameter is null or empty.
     *
     * @param value The value to check.
     * @return True, if the {@code value} is null or empty; false, otherwise.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Checks whether the given {@code value} parameter is null, empty or whitespace-only string.
     *
     * @param value The value to check.
     * @return True, if the {@code value} is null, empty or whitespace-only string; false, otherwise.
     */
    public static boolean isNullOrWhitespace(String value) {
        return isNullOrEmpty(value) || value.trim().isEmpty();
    }

    /**
     * Joins the given {@code values} into one using an empty space as the delimiter.
     *
     * @param values The values to join.
     * @return A new string representing the joined {@code values} delimited by an empty string.
     */
    public static String join(String... values) {
        return joinWithDelimiter(" ", values);
    }

    /**
     * Joins the given {@code values} into one using the given {@code delimiter} value as the delimiter.
     *
     * @param delimiter The delimiter to be added between to successful values.
     * @param values    The values to join.
     * @return A new string representing the joined {@code values} delimited by the {@code delimiter} value.
     */
    public static String joinWithDelimiter(String delimiter, String... values) {
        if (delimiter == null) {
            throw new IllegalArgumentException("The delimiter is not allowed to be null");
        }

        if (values == null || values.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String value : values) {
            if (!isNullOrWhitespace(value)) {
                if (sb.length() > 0) {
                    sb.append(delimiter);
                }

                sb.append(value);
            }
        }

        return sb.toString();
    }
}
