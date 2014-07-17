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

import java.io.Serializable;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

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
    @Setter(AccessLevel.PRIVATE)
    private boolean authenticated;
    private String name;

    public CurrentUser() {
	this.authenticated = false;
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

    public boolean isDiscipleshipPairMenuVisible() {
	return authenticated;
    }

    public boolean isDiscipleshipPairAddMenuVisible() {
	return authenticated;
    }

    public boolean isDiscipleshipPairListMenuVisible() {
	return authenticated;
    }

    public boolean isDiscipleshipTeacherListMenuVisible() {
	return authenticated;
    }

    public boolean isDiscipleshipDiscipleListMenuVisible() {
	return authenticated;
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

    @PreDestroy
    public void destroy() {
	authenticated = false;
	name = null;
    }
}
