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
package ro.satrapu.churchmanagement.ui.converters;

import java.text.MessageFormat;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import ro.satrapu.churchmanagement.ui.Messages;

/**
 *
 * @author satrapu
 */
@FacesConverter(value = "entityIdConverter")
public class EntityIdConverter implements Converter {

    @Inject
    Messages messages;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            //return Long.parseLong(value);
            return Long.parseLong("sdsasd");
        } catch (NumberFormatException ex) {
            String detail = MessageFormat.format(messages.getValueFor("converters.entityIdConverter.conversionFailed"), value);
            FacesMessage conversionFailedMessage = new FacesMessage(messages.getValueFor("converters.conversionFailed"), detail);
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
