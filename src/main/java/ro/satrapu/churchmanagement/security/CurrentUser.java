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
package ro.satrapu.churchmanagement.security;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import ro.satrapu.churchmanagement.ui.Urls;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents the current user.
 *
 * @author satrapu
 */
@Named
@SessionScoped
@Data
public class CurrentUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Urls urls;
    private String name;

    @Setter(AccessLevel.PRIVATE)
    private boolean authenticated;

    /**
     * This constructor is needed by CDI.
     */
    public CurrentUser() {
    }

    @Inject
    public CurrentUser(@NotNull Urls urls) {
        this.urls = urls;
    }

    public boolean isPersonsMenuVisible() {
        return authenticated;
    }

    public boolean isPersonListMenuVisible() {
        return authenticated;
    }

    public boolean isPersonAddMenuVisible() {
        return authenticated;
    }

    public boolean isDiscipleshipMenuVisible() {
        return authenticated;
    }

    public boolean isDiscipleshipStatusMenuVisible() {
        return authenticated;
    }

    public String getHomePageUrl() {
        return urls.addContextPath(Urls.Secured.HOME);
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        if (authenticatedUser == null) {
            throw new IllegalArgumentException("Authenticated user cannot be null");
        }

        String localName = authenticatedUser.getName();

        if (localName == null || localName.isEmpty()) {
            throw new IllegalArgumentException("The name of an authenticated user cannot be null or empty string");
        }

        this.name = localName;
        this.authenticated = true;
    }

    @PostConstruct
    public void initialize() {
        this.authenticated = false;
    }

    @PreDestroy
    public void destroy() {
        authenticated = false;
        name = null;
    }
}
