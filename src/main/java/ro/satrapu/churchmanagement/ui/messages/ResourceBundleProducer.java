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

import java.text.MessageFormat;
import java.util.ResourceBundle;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.satrapu.churchmanagement.ui.FacesContextInstance;

/**
 * Produces {@link java.util.ResourceBundle} instances.
 *
 * @author satrapu
 */
public class ResourceBundleProducer {

    private static final Logger logger = LoggerFactory.getLogger(ResourceBundleProducer.class);
    private static final String RESOURCE_BUNDLE_NAME = "msg";

    @Inject
    @FacesContextInstance
    FacesContext facesContext;

    /**
     * Produces a {@link ResourceBundle} instance as configured inside the faces.xml file.
     *
     * @return A {@link ResourceBundle} instance.
     * @throws RuntimeException If a {@link ResourceBundle} instance was not found.
     */
    @Produces
    @ResourceBundleInstance
    public ResourceBundle getResourceBundle() {
	logger.debug("Messages will be fetched using bundle name: {} ...", RESOURCE_BUNDLE_NAME);
	ResourceBundle resourceBundle = facesContext.getApplication().getResourceBundle(facesContext, RESOURCE_BUNDLE_NAME);

	if (resourceBundle == null) {
	    String errorMessage = MessageFormat.format("Could not find bundle using name: {0}.", RESOURCE_BUNDLE_NAME);
	    logger.error(errorMessage);
	    throw new RuntimeException(errorMessage);
	}

	logger.debug("Found bundle with {} pairs", resourceBundle.keySet().size());
	return resourceBundle;
    }
}
