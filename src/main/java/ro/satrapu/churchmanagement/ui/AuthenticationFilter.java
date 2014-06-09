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

import java.io.IOException;
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
import ro.satrapu.churchmanagement.security.CurrentUser;

/**
 * A {@link Filter} implementation which ensures that only authenticated users
 * may access any page, except the index.
 *
 * @author satrapu
 */
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = "/secured/*")
public class AuthenticationFilter implements Filter {

    @Inject
    CurrentUser currentUser;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //nothing to init
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
