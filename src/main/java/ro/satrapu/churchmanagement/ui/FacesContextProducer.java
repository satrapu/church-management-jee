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

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

/**
 * Produces {@link FacesContext} instances.
 *
 * @author satrapu
 */
public class FacesContextProducer {

    /**
     * Produces {@link FacesContext} instances.
     *
     * @return The {@link FacesContext} instance associated with the current
     * request.
     */
    @Produces
    @RequestScoped
    @FacesContextInstance
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }
}