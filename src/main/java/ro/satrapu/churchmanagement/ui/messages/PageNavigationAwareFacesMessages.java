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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Jesse Wilson jesse[AT]odel.on.ca
 * @secondaryAuthor Lincoln Baxter III lincoln[AT]ocpsoft.com
 * @see <a href="http://stackoverflow.com/questions/11955848/jsf-2-1-redirect-preserving-error-message">StackOverflow</a>.
 * Enables messages to be rendered on different pages from which they were set.
 * <br/>
 * After each phase where messages may be added, this moves the messages from
 * the page-scoped FacesContext to the session-scoped session map.
 * <br/>
 * Before messages are rendered, this moves the messages from the session-scoped
 * session map back to the page-scoped FacesContext.
 * <br/>
 * Only global messages, not associated with a particular component, are moved.
 * Component messages cannot be rendered on pages other than the one on which
 * they were added.
 * <br/>
 * To enable multi-page messages support, add a <code>lifecycle</code> block to
 * your faces-config.xml file. That block should contain a single
 * <code>phase-listener</code> block containing the fully-qualified class name
 * of this file.
 */
public class PageNavigationAwareFacesMessages implements PhaseListener {
    private static final long serialVersionUID = 1L;
    private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    /*
     * Check to see if we are "naturally" in the RENDER_RESPONSE phase. If we
     * have arrived here and the response is already complete, then the page is
     * not going to show up: don't display messages yet.
     */
    @Override
    public void beforePhase(final PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        saveMessages(facesContext);

        if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            if (!facesContext.getResponseComplete()) {
                restoreMessages(facesContext);
            }
        }
    }

    /*
     * Save messages into the session after every phase.
     */
    @Override
    public void afterPhase(final PhaseEvent event) {
        if (!PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
            FacesContext facesContext = event.getFacesContext();
            saveMessages(facesContext);
        }
    }

    private int saveMessages(final FacesContext facesContext) {
        List<FacesMessage> messages = new ArrayList<>();

        for (Iterator<FacesMessage> messageIterator = facesContext.getMessages(null); messageIterator.hasNext(); ) {
            messages.add(messageIterator.next());
            messageIterator.remove();
        }

        if (messages.isEmpty()) {
            return 0;
        }

        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap.get(sessionToken);

        if (existingMessages != null) {
            existingMessages.addAll(messages);
        } else {
            sessionMap.put(sessionToken, messages);
        }

        return messages.size();
    }

    private int restoreMessages(final FacesContext facesContext) {
        Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        List<FacesMessage> messages = (List<FacesMessage>) sessionMap.remove(sessionToken);

        if (messages == null) {
            return 0;
        }

        int restoredCount = messages.size();

        for (Object element : messages) {
            facesContext.addMessage(null, (FacesMessage) element);
        }

        return restoredCount;
    }
}
