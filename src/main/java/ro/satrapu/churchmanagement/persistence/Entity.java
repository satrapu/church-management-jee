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
package ro.satrapu.churchmanagement.persistence;

import java.io.Serializable;

/**
 *
 * @author satrapu
 */
public interface Entity extends Serializable {

    /**
     * Gets the entity identifier.
     *
     * @return A non-null value if the entity was already persisted; null,
     * otherwise.
     */
    public Serializable getId();

    /**
     * Gets the entity version.
     *
     * @return A positive value, if the entity was persisted; 0, otherwise.
     */
    public Long getVersion();
}
