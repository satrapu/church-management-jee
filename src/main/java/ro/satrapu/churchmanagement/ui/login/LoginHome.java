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
package ro.satrapu.churchmanagement.ui.login;

import org.slf4j.Logger;
import ro.satrapu.churchmanagement.security.AuthenticatedUser;
import ro.satrapu.churchmanagement.security.AuthenticationDetails;
import ro.satrapu.churchmanagement.security.CurrentUser;
import ro.satrapu.churchmanagement.security.UserAuthenticator;
import ro.satrapu.churchmanagement.ui.Urls;
import ro.satrapu.churchmanagement.ui.messages.Messages;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Handles user login/logout actions.
 *
 * @author satrapu
 */
@Model
public class LoginHome {
    private CurrentUser currentUser;
    private UserAuthenticator userAuthenticator;
    private AuthenticationDetails authenticationDetails;
    private Messages messages;
    private FacesContext facesContext;
    private Logger logger;

    @Inject
    public LoginHome(CurrentUser currentUser,
                     @NotNull UserAuthenticator userAuthenticator,
                     @NotNull Messages messages,
                     @NotNull FacesContext facesContext,
                     @NotNull Logger logger) {
        this.currentUser = currentUser;
        this.userAuthenticator = userAuthenticator;
        this.messages = messages;
        this.facesContext = facesContext;
        this.logger = logger;
    }

    @PostConstruct
    public void initialize() {
        if (userAuthenticator.hasHardCodedValue()) {
            authenticationDetails = userAuthenticator.getHardCodedValue();
        } else {
            authenticationDetails = new AuthenticationDetails();
        }
    }

    /**
     * Handles user login action.
     *
     * @return
     */
    public String login() {
        AuthenticatedUser authenticatedUser = null;
        boolean authenticationFailed = false;

        try {
            authenticatedUser = userAuthenticator.authenticate(authenticationDetails);
        } catch (Throwable e) {
            logger.warn("Unable to authenticate user", e);
            authenticationFailed = true;
        }

        if (!authenticationFailed && authenticatedUser != null) {
            currentUser.setAuthenticatedUser(authenticatedUser);
            messages.addInfo("pages.login.actions.login.success");
            return Urls.addRedirectQueryStringParameter(Urls.Secured.HOME);
        } else {
            messages.addError("pages.login.actions.login.failure");
            return null;
        }
    }

    /**
     * Handles user logout action.
     *
     * @return
     */
    public String logout() {
        resetLoginInfo();
        currentUser.destroy();
        facesContext.getExternalContext().invalidateSession();
        messages.addInfo("pages.login.actions.logout.success");

        return Urls.addRedirectQueryStringParameter(Urls.Unsecured.LOGIN);
    }

    /**
     * Handles login form reset.
     */
    public void reset() {
        resetLoginInfo();
        messages.addInfo("pages.login.actions.reset");
    }

    public AuthenticationDetails getAuthenticationDetails() {
        return authenticationDetails;
    }

    private void resetLoginInfo() {
        authenticationDetails.setUserName(null);
        authenticationDetails.setPassword(null);
    }
}
