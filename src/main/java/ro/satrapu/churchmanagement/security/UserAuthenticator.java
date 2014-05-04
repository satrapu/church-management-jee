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
package ro.satrapu.churchmanagement.security;

import javax.inject.Inject;
import org.slf4j.Logger;
import ro.satrapu.churchmanagement.logging.LoggerInstance;

/**
 * Authenticates users.
 *
 * @author satrapu
 */
public class UserAuthenticator {

    @Inject
    @LoggerInstance
    Logger logger;

    /**
     * Checks whether the given {
     *
     * @paramref loginInfo} identifies an existing user.
     * @param loginInfo
     * @return
     */
    public boolean authenticate(LoginInfo loginInfo) {
        if (loginInfo == null) {
            throw new IllegalArgumentException("Login info is  required");
        }

        if (loginInfo.getUserName() == null || loginInfo.getUserName().length() == 0) {
            throw new IllegalArgumentException("User name is  required");
        }

        if (loginInfo.getPassword() == null || loginInfo.getPassword().length() == 0) {
            throw new IllegalArgumentException("Password is  required");
        }

        return "satrapu".equals(loginInfo.getUserName()) && "123456".equals(loginInfo.getPassword());
    }
}
