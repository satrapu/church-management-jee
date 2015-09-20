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
package ro.satrapu.churchmanagement.security;

import ro.satrapu.churchmanagement.ui.Urls;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A {@link Filter} implementation which ensures that not authenticated users may access only the login page.
 * Authenticated ones may access any page.
 *
 * @author satrapu
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/secured/*")
public class AuthenticationFilter implements Filter {
    private CurrentUser currentUser;

    @Inject
    public AuthenticationFilter(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //nothing to initialize
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (currentUser != null && currentUser.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String contextPath = httpRequest.getContextPath();
        String newRequestURI = contextPath + Urls.Unsecured.LOGIN;

        httpResponse.sendRedirect(newRequestURI);
    }

    @Override
    public void destroy() {
        //nothing to destroy
    }
}
