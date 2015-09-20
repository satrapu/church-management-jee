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

/**
 * Authenticates users.
 *
 * @author satrapu
 */
public interface UserAuthenticator {

    /**
     * Checks whether the given details can be used to fetch an existing user.
     *
     * @param authenticationDetails Represents the details of an authentication operation.
     * @return
     */
    AuthenticatedUser authenticate(AuthenticationDetails authenticationDetails);

    /**
     * Checks whether this implementation can produce an {@link AuthenticationDetails} instance used for authenticating a hard-coded user.
     *
     * @return
     */
    boolean hasHardCodedValue();

    /**
     * Creates an {@link AuthenticationDetails} instance used for authenticating a hard-coded user.
     *
     * @return An {@link AuthenticationDetails} instance if this {@link UserAuthenticator} implementation supports hard-coded users; null,
     * otherwise.
     */
    AuthenticationDetails getHardCodedValue();
}
