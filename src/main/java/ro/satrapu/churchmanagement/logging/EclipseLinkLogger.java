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
package ro.satrapu.churchmanagement.logging;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;

/**
 * Logs EclipseLink messages via an {@link Logger} instance.
 *
 * @author satrapu
 */
public class EclipseLinkLogger extends AbstractSessionLog implements SessionLog {

    Logger logger = org.slf4j.LoggerFactory.getLogger(EclipseLinkLogger.class);

    @Override
    public void log(SessionLogEntry sessionLogEntry) {
	switch (sessionLogEntry.getLevel()) {
	    case SessionLog.OFF:
		break;
	    case SessionLog.FINEST:
	    case SessionLog.FINER:
		logger.trace(sessionLogEntry.getMessage());
		break;
	    case SessionLog.FINE:
	    case SessionLog.CONFIG:
		logger.debug(sessionLogEntry.getMessage());
		break;
	    case SessionLog.WARNING:
		logger.warn(sessionLogEntry.getMessage());
		break;
	    case SessionLog.SEVERE:
		logger.error(sessionLogEntry.getMessage());
		break;
	    default:
		logger.info(sessionLogEntry.getMessage());
		break;
	}
    }
}
