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

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Contains the URLs pointing to any page inside this application.
 *
 * @author satrapu
 */
public class Urls implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String PATH_SEPARATOR = "/";
    private FacesContext facesContext;

    @Inject
    public Urls(@NotNull FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     * Ensures that the JSF redirect query string parameter is added to the given URL.
     *
     * @param url The URL where the JSF redirect query string parameter will be added to.
     * @return A URL containing the JSF redirect query string parameter.
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

    /**
     * Ensures that the given {@code url} parameter contains the current context path.
     *
     * @param url
     * @return
     */
    public String addContextPath(String url) {
        StringBuilder processedUrl = new StringBuilder(facesContext.getExternalContext().getRequestContextPath());

        if (!url.startsWith(PATH_SEPARATOR)) {
            processedUrl.append(PATH_SEPARATOR);
        }

        processedUrl.append(url);
        return processedUrl.toString();
    }

    public interface Unsecured {
        String LOGIN = PATH_SEPARATOR + "login.xhtml";
    }

    public interface Secured {
        String SECURED_PREFIX = "secured";
        String HOME = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + "home.xhtml";

        public interface Persons {
            String PREFIX = "persons";
            String LIST = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "list.xhtml";
            String EDIT = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "edit.xhtml";
            String REMOVE = PATH_SEPARATOR + SECURED_PREFIX + PATH_SEPARATOR + PREFIX + PATH_SEPARATOR + "remove.xhtml";
        }
    }
}
