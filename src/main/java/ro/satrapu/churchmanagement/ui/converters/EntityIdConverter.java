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
package ro.satrapu.churchmanagement.ui.converters;

import ro.satrapu.churchmanagement.ui.messages.Messages;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

/**
 * @author satrapu
 */
@FacesConverter(value = "entityIdConverter")
public class EntityIdConverter implements Converter {
    private Messages messages;

    /**
     * This default constructor is needed by CDI.
     */
    public EntityIdConverter() {
    }

    @Inject
    public EntityIdConverter(@NotNull Messages messages) {
        this.messages = messages;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Long longValue = Long.parseLong(value);
            return longValue;
        } catch (NumberFormatException ex) {
            String details = MessageFormat.format(messages.getMessageFor("converters.conversionFailed.entityIdConverter"), value);
            FacesMessage conversionFailedMessage = new FacesMessage(messages.getMessageFor("converters.conversionFailed.title"), details);

            throw new ConverterException(conversionFailedMessage, ex);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
