package ro.satrapu.churchmanagement.ui.converters;

import ro.satrapu.churchmanagement.persistence.DiscipleshipStatus;
import ro.satrapu.churchmanagement.ui.messages.Messages;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

/**
 * Converts {@link ro.satrapu.churchmanagement.persistence.DiscipleshipStatus} enumeration values to and from {@link java.lang.String} values.
 * <p>
 * See <a href="http://stackoverflow.com/a/7531487">this</a> StackOverflow article to understand how to inject CDI beans into JSF converters.
 * </p>
 */
@Named
public class DiscipleshipStatusConverter implements Converter {
    private Messages messages;

    @Inject
    public DiscipleshipStatusConverter(@NotNull Messages messages) {
        this.messages = messages;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            DiscipleshipStatus enumerationValue = DiscipleshipStatus.valueOf(value);
            return enumerationValue;
        } catch (IllegalArgumentException ex) {
            String details = MessageFormat.format(messages.getMessageFor("converters.conversionFailed.discipleshipStatusConverter"), value);
            FacesMessage conversionFailedMessage = new FacesMessage(messages.getMessageFor("converters.conversionFailed.title"), details);

            throw new ConverterException(conversionFailedMessage, ex);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return null;
        }

        String result = null;
        DiscipleshipStatus discipleshipStatus = (DiscipleshipStatus) value;

        switch (discipleshipStatus) {
            case DISCIPLE_CANDIDATE:
                result = messages.getMessageFor("enumerations.DiscipleshipStatus.DISCIPLE_CANDIDATE");
                break;
            case NOT_INTERESTED:
                result = messages.getMessageFor("enumerations.DiscipleshipStatus.NOT_INTERESTED");
                break;
            case TEACHING_CANDIDATE:
                result = messages.getMessageFor("enumerations.DiscipleshipStatus.TEACHING_CANDIDATE");
                break;
        }

        return result;
    }
}
