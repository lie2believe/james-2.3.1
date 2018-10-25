/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.services;

import org.apache.mailet.Matcher;

import javax.mail.MessagingException;

public interface MatcherLoader {

    /**
     * The component role used by components implementing this service
     */
    String ROLE = "org.apache.james.services.MatcherLoader";

    /**
     * Get a new Matcher with the specified name acting
     * in the specified context.
     *
     * @param matchName the name of the matcher to be loaded
     * @param context the MailetContext to be passed to the new
     *                matcher
     * @throws MessagingException if an error occurs
     */
    public Matcher getMatcher(String matchName) throws MessagingException;

}
