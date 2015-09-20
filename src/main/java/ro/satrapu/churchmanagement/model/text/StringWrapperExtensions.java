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

    /**
     * Joins the given {@code values} into one using an empty space as the delimiter.
     *
     * @param values The values to join.
     * @return A new {@link StringWrapper} representing the joined {@code values} delimited by an empty string.
     */
    public static StringWrapper join(StringWrapper... values) {
        return joinWithDelimiter(" ", values);
    }

    /**
     * Joins the given {@code values} into one using the given {@code delimiter} value as the delimiter.
     *
     * @param delimiter The delimiter to be added between to successful values.
     * @param values    The values to join.
     * @return A new {@link StringWrapper} representing the joined {@code values} delimited by the {@code delimiter} value.
     */
    public static StringWrapper joinWithDelimiter(String delimiter, StringWrapper... values) {
        if (delimiter == null) {
            throw new IllegalArgumentException("The delimiter is not allowed to be null");
        }

        if (values == null || values.length == 0) {
            return new StringWrapper() {

                @Override
                public String getValue() {
                    return "";
                }
            };
        }

        StringBuilder sb = new StringBuilder();

        for (StringWrapper stringWrapper : values) {
            if (!isNullOrWhitespace(stringWrapper)) {
                if (sb.length() > 0) {
                    sb.append(delimiter);
                }

                sb.append(stringWrapper.getValue());
            }
        }

        final String result = sb.toString();

        return new StringWrapper() {

            @Override
            public String getValue() {
                return result;
            }
        };
    }
}
