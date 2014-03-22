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

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class used for displaying {@link FacesMessage} instances.
 *
 * @author Satrapu
 */
@Model
public class Messages {

    static final String MISSING_MESSAGE_KEY_PATTERN = "???{0}???";
    static final String BUNDLE_NAME = "msg";
    ResourceBundle bundle;
    private static final Logger logger = LoggerFactory.getLogger(Messages.class);

    @PostConstruct
    public void init() {
        logger.debug("Messages will be fetched using bundle name: {}", BUNDLE_NAME);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        bundle = facesContext.getApplication().getResourceBundle(facesContext, BUNDLE_NAME);
    }

    public void info(String messageKey) {
        logger.debug("Info message using key: {}", messageKey);
        add(FacesMessage.SEVERITY_INFO, messageKey);
    }

    public void warning(String messageKey) {
        logger.debug("Warning message using key: {}", messageKey);
        add(FacesMessage.SEVERITY_WARN, messageKey);
    }

    public void error(String messageKey) {
        logger.debug("Error message using key: {}", messageKey);
        add(FacesMessage.SEVERITY_ERROR, messageKey);
    }

    public void fatal(String messageKey) {
        logger.debug("Fatal message using key: {}", messageKey);
        add(FacesMessage.SEVERITY_FATAL, messageKey);
    }

    public void add(FacesMessage.Severity severity, String summaryMessageKey) {
        logger.debug("Adding FacesMessage using severity: {} and summary key: {}", severity, summaryMessageKey);
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSeverity(severity);
        facesMessage.setSummary(getValueFor(summaryMessageKey));
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public void add(FacesMessage.Severity severity, String summaryMessageKey, String detailMessageKey) {
        logger.debug("Adding FacesMessage using severity: {}, summary key: {} and detail key: {}",
                new Object[]{severity, summaryMessageKey, detailMessageKey});
        FacesMessage facesMessage = new FacesMessage();
        facesMessage.setSeverity(severity);
        facesMessage.setSummary(getValueFor(summaryMessageKey));
        facesMessage.setDetail(getValueFor(detailMessageKey));
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }

    public String getValueFor(String messageKey) {
        logger.debug("Trying to get message using key: {}", messageKey);
        String value;

        if (!bundle.containsKey(messageKey)) {
            value = MessageFormat.format(MISSING_MESSAGE_KEY_PATTERN, messageKey);
        } else {
            value = bundle.getString(messageKey);
        }

        logger.debug("Found message: {} for key: {}", value, messageKey);
        return value;
    }
}
