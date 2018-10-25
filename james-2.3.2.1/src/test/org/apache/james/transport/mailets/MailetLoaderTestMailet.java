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


package org.apache.james.transport.mailets;

import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailetConfig;

import javax.mail.MessagingException;

public class MailetLoaderTestMailet extends GenericMailet {
    private boolean m_initWasCalled = false;
    private MailetConfig m_config;

    public void service(Mail mail) throws MessagingException {
        throw new Error("should not be called by loader");
    }

    public void init(MailetConfig newConfig) throws MessagingException {
        super.init(newConfig);
        m_config = newConfig;
        m_initWasCalled = true;
    }
    
    public boolean assertInitCalled() {
        return m_initWasCalled;
    }

}
