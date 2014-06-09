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

import javax.inject.Inject;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;

/**
 * Authenticates users.
 *
 * @author satrapu
 */
public class Authenticator {

    @Inject
    @LoggerInstance
    Logger logger;

    /**
     * Checks whether the given details can be used to fetch an existing user.
     *
     * @param authenticationDetails Represents the details of an authentication
     * operation.
     * @return
     */
    public AuthenticatedUser authenticate(AuthenticationDetails authenticationDetails) {
        if (authenticationDetails == null) {
            throw new IllegalArgumentException("Authentication details are required");
        }

        String userName = authenticationDetails.getUserName();
        String password = authenticationDetails.getPassword();

        if (userName == null || userName.length() == 0) {
            throw new IllegalArgumentException("User name is required");
        }

        if (password == null || password.length() == 0) {
            throw new IllegalArgumentException("Password is required");
        }

        boolean isAuthenticated = "satrapu".equals(userName) && "123456".equals(password);

        if (!isAuthenticated) {
            throw new RuntimeException("User name and password do not match");
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setName("Bogdan Marian");
        return authenticatedUser;
    }
}
