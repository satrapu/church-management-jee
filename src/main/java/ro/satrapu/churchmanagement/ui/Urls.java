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
package ro.satrapu.churchmanagement.ui;

/**
 * Contains the URLs pointing to any page inside this application.
 *
 * @author satrapu
 */
public class Urls {

    public static final String PATH_SEPARATOR = "/";

    /**
     * Ensures that the JSF redirect query string parameter is added to the given URL.
     *
     * @param url
     * @return
     */
    public static String addRedirectQueryStringParameter(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }

        //add query string parameter needed to instruct JSF runtime to perform a redirect when using this URL
        StringBuilder redirectUrl = new StringBuilder(url);

        if (!url.contains("?")) {
            redirectUrl.append("?");
        } else {
            redirectUrl.append("&");
        }

        redirectUrl.append("faces-redirect=true");
        return redirectUrl.toString();
    }

    public interface Unsecured {

        public static final String LOGIN = PATH_SEPARATOR + "login.xhtml";
    }

    public interface Secured {

        public static final String SECURED_PREFIX = "secured";
        public static final String HOME = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + "home.xhtml";

        public interface Persons {

            public static final String PREFIX = "persons";
            public static final String LIST = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "list.xhtml";
            public static final String EDIT = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "edit.xhtml";
            public static final String REMOVE = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "remove.xhtml";
        }
    }
}
