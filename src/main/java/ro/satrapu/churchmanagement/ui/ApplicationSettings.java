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
package ro.satrapu.churchmanagement.ui;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Contains application-wide settings.
 *
 * @author satrapu
 */
@Named
@ApplicationScoped
public class ApplicationSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    private FacesContext facesContext;
    private String titleSuffix;

    @Inject
    public ApplicationSettings(@NotNull FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    @PostConstruct
    public void initialize() {
        initializeTitleSuffix();
    }

    private void initializeTitleSuffix() {
        ProjectStage projectStage = facesContext.getApplication().getProjectStage();

        switch (projectStage) {
            case Production:
                titleSuffix = "";
                break;
            default:
                titleSuffix = MessageFormat.format(" :: {0}", projectStage.toString());
                break;
        }
    }

    public String getTitleSuffix() {
        return titleSuffix;
    }
}
