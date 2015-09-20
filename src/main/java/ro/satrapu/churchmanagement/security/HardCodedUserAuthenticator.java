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

import org.slf4j.Logger;

import javax.enterprise.inject.Alternative;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * An {@link UserAuthenticator} implementation which authenticates users against a hard-coded pair of user named and password.
 *
 * @author satrapu
 */
@Alternative
public class HardCodedUserAuthenticator implements UserAuthenticator {

    private static final String HARD_CODED_USER_NAME = "satrapu";

    /**
     * This hard-coded password was generated using <a href="http://passwordsgenerator.net/">Secure Password Generator tool</a>.
     */
    private static final String HARD_CODED_PASSWORD = "QZ{vVyj*:9C5P5fBm+tQGch}mM\\,s;q>ZE7F!k6;Z@#RJpR{hbAN&GJz~4vvPF#Je{q<Q&afT]5}-@Sa[qMa;)_Bj!sb6susv>W^^{?R#t*XD4hym%CQ9_VhNTvA;*J^";

    private Logger logger;
    private FacesContext facesContext;

    @Inject
    public HardCodedUserAuthenticator(@NotNull FacesContext facesContext,
                                      @NotNull Logger logger) {
        this.facesContext = facesContext;
        this.logger = logger;
    }

    @Override
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

        boolean isAuthenticated = getHardCodedValue().getUserName().equals(userName) && getHardCodedValue().getPassword().equals(password);

        if (!isAuthenticated) {
            throw new RuntimeException("User name and password do not match");
        }

        AuthenticatedUser authenticatedUser = new AuthenticatedUser();
        authenticatedUser.setName("Bogdan Marian");
        return authenticatedUser;
    }

    @Override
    public AuthenticationDetails getHardCodedValue() {
        AuthenticationDetails authenticationDetails = new AuthenticationDetails();
        authenticationDetails.setUserName(HARD_CODED_USER_NAME);
        authenticationDetails.setPassword(HARD_CODED_PASSWORD);

        return authenticationDetails;
    }

    @Override
    public boolean hasHardCodedValue() {
        switch (facesContext.getApplication().getProjectStage()) {
            case Development:
            case SystemTest:
            case UnitTest:
                return true;
            default:
                return false;
        }
    }
}
