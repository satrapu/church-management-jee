/*
 * Copyright 2014 Satrapu.
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

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;
import ro.satrapu.churchmanagement.security.LoginInfo;
import ro.satrapu.churchmanagement.security.UserAuthenticator;

/**
 * Handles user login/logout actions.
 *
 * @author satrapu
 */
@Named
@SessionScoped
public class LoginHome implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String URL_INDEX = "/index?faces-redirect=true";

    @Inject
    @LoggerInstance
    Logger logger;

    @Inject
    transient UserAuthenticator userAuthenticator;

    @Inject
    transient Messages messages;

    @Inject
    @FacesContextInstance
    transient FacesContext facesContext;

    private LoginInfo instance;
    private boolean userAuthenticated;

    @PostConstruct
    public void init() {
        instance = new LoginInfo();
        instance.setUserName("satrapu");
        instance.setPassword("123456");
    }

    /**
     * Handles user login action.
     *
     * @return
     */
    public String login() {
        userAuthenticated = userAuthenticator.authenticate(instance);

        if (userAuthenticated) {
            messages.info("pages.login.actions.login.success");
            return URL_INDEX;
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
        facesContext.getExternalContext().invalidateSession();
        resetLoginInfo();
        userAuthenticated = false;
        messages.info("pages.login.actions.logout.success");
        return URL_INDEX;
    }

    /**
     * Handles login form reset.
     */
    public void reset() {
        resetLoginInfo();
        messages.info("pages.login.actions.reset");
    }

    public LoginInfo getInstance() {
        return instance;
    }

    public boolean isUserAuthenticated() {
        return userAuthenticated;
    }

    private void resetLoginInfo() {
        instance.setUserName(null);
        instance.setPassword(null);
    }
}
