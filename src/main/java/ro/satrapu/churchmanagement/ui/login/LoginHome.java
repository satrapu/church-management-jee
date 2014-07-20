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

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;
import ro.satrapu.churchmanagement.security.AuthenticatedUser;
import ro.satrapu.churchmanagement.security.AuthenticationDetails;
import ro.satrapu.churchmanagement.security.UserAuthenticator;
import ro.satrapu.churchmanagement.security.CurrentUser;
import ro.satrapu.churchmanagement.ui.FacesContextInstance;
import ro.satrapu.churchmanagement.ui.messages.Messages;
import ro.satrapu.churchmanagement.ui.Urls;

/**
 * Handles user login/logout actions.
 *
 * @author satrapu
 */
@Model
public class LoginHome {

    @Inject
    @LoggerInstance
    Logger logger;

    @Inject
    UserAuthenticator userAuthenticator;

    @Inject
    Messages messages;

    @Inject
    @FacesContextInstance
    FacesContext facesContext;

    @Inject
    CurrentUser currentUser;

    private AuthenticationDetails authenticationDetails;

    @PostConstruct
    public void init() {
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
	    messages.info("pages.login.actions.login.success");
	    return Urls.addRedirectQueryStringParameter(Urls.Secured.HOME);
	} else {
	    messages.error("pages.login.actions.login.failure");
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
	messages.info("pages.login.actions.logout.success");
	return Urls.addRedirectQueryStringParameter(Urls.Unsecured.LOGIN);
    }

    /**
     * Handles login form reset.
     */
    public void reset() {
	resetLoginInfo();
	messages.info("pages.login.actions.reset");
    }

    public AuthenticationDetails getAuthenticationDetails() {
	return authenticationDetails;
    }

    private void resetLoginInfo() {
	authenticationDetails.setUserName(null);
	authenticationDetails.setPassword(null);
    }
}
