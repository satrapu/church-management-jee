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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * Produces {@link org.slf4j.Logger} instances.
 *
 * @author satrapu
 */
public class LoggerProducer {
    /**
     * Creates a new {@link Logger} instance for the given {@link InjectionPoint} instance.
     *
     * @param injectionPoint The injection point where to inject a {@link Logger} instance
     * @return A {@link Logger} instance.
     */
    @Produces
    public static Logger getLoggerFor(InjectionPoint injectionPoint) {
        Class<?> declaringClass = injectionPoint.getMember().getDeclaringClass();
        Logger result = getLoggerFor(declaringClass);
        return result;
    }

    /**
     * Creates a new {@link Logger} instance for the given {@link Class} instance.
     *
     * @param clazz The class for which to produce a {@link Logger} instance
     * @return A {@link Logger} instance.
     */
    public static Logger getLoggerFor(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
