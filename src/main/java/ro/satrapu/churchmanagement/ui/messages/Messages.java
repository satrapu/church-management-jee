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
package ro.satrapu.churchmanagement.ui.messages;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.satrapu.churchmanagement.ui.FacesContextInstance;

/**
 * Displays localized {@link FacesMessage} instances.
 *
 * @author satrapu
 */
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Messages.class);
    private static final String MESSAGE_KEY_NOT_FOUND = "???{0}???";

    @Inject
    @ResourceBundleInstance
    transient ResourceBundle resourceBundle;

    @Inject
    @FacesContextInstance
    transient FacesContext facesContext;

    public void info(String messageKey, Object... arguments) {
	logger.debug("Adding info message using key for summary: {} ...", messageKey);
	add(FacesMessage.SEVERITY_INFO, messageKey, arguments);
    }

    public void warning(String messageKey, Object... arguments) {
	logger.debug("Adding warning message using key for summary: {} ...", messageKey);
	add(FacesMessage.SEVERITY_WARN, messageKey, arguments);
    }

    public void error(String messageKey, Object... arguments) {
	logger.debug("Adding error message using key for summary: {} ...", messageKey);
	add(FacesMessage.SEVERITY_ERROR, messageKey, arguments);
    }

    public void fatal(String messageKey, Object... arguments) {
	logger.debug("Adding fatal message using key for summary: {} ...", messageKey);
	add(FacesMessage.SEVERITY_FATAL, messageKey, arguments);
    }

    public void add(FacesMessage.Severity severity, String summaryMessageKey, Object... arguments) {
	logger.debug("Adding FacesMessage using severity: {} and key for summary: {} ...", severity, summaryMessageKey);

	FacesMessage facesMessage = new FacesMessage();
	facesMessage.setSeverity(severity);
	facesMessage.setSummary(getMessageFor(summaryMessageKey, arguments));

	facesContext.addMessage(null, facesMessage);
    }

    public void add(FacesMessage.Severity severity, String summaryMessageKey, String detailMessageKey, Object... arguments) {
	logger.debug("Adding FacesMessage using severity: {}, key for summary: {} and key for detail: {} ...",
		severity, summaryMessageKey, detailMessageKey);

	FacesMessage facesMessage = new FacesMessage();
	facesMessage.setSeverity(severity);
	facesMessage.setSummary(getMessageFor(summaryMessageKey, arguments));
	facesMessage.setDetail(getMessageFor(detailMessageKey, arguments));

	add(facesMessage);
    }

    public void add(FacesMessage facesMessage) {
	logger.debug("Adding FacesMessage: {} ...", facesMessage);
	facesContext.addMessage(null, facesMessage);
    }

    public String getMessageFor(String key, Object... arguments) {
	logger.debug("Trying to get message using key: {} ...", key);
	String value;

	if (!resourceBundle.containsKey(key)) {
	    value = MessageFormat.format(MESSAGE_KEY_NOT_FOUND, key);
	} else {
	    value = MessageFormat.format(resourceBundle.getString(key), arguments);
	}

	logger.debug("Found message: {}", value);
	return value;
    }
}
