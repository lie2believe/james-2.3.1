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
package org.apache.james.transport;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.james.core.MailImpl;
import org.apache.james.core.MimeMessageCopyOnWriteProxy;
import org.apache.james.core.MimeMessageInputStreamSource;
import org.apache.james.test.mock.james.MockSpoolRepository;
import org.apache.james.test.mock.mailet.MockMailContext;
import org.apache.james.test.mock.mailet.MockMailetConfig;
import org.apache.james.transport.mailets.debug.DumpSystemErr;
import org.apache.james.transport.matchers.All;
import org.apache.james.transport.matchers.RecipientIs;
import org.apache.mailet.GenericMailet;
import org.apache.mailet.Mail;
import org.apache.mailet.MailAddress;
import org.apache.mailet.Mailet;
import org.apache.mailet.MailetConfig;
import org.apache.mailet.MailetContext;
import org.apache.mailet.Matcher;
import org.apache.mailet.MatcherConfig;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.SharedByteArrayInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * Currently here as a proof of JAMES-421.
 * Fixing code will follow
 */
public class LinearProcessorTest extends TestCase {
    Collection c;
    LinearProcessor t;
    MimeMessage mw = null;
    String content = "Subject: test\r\nContent-Transfer-Encoding: plain";
    String sep = "\r\n\r\n";
    String body = "original body\r\n.\r\n";
    MailetContext       mockContext = new MockMailContext();

    public static int counter = 0;
    
    
    
    private class CheckerMailet extends GenericMailet {
        
        public ArrayList receivedMails = new ArrayList();

        public void service(Mail mail) throws MessagingException {
            Mail m2 = new MailImpl(mail,mail.getName());
            m2.setState(mail.getState());
            receivedMails.add(m2);
        }
        
    }
    
    private MailetConfig mockMailetConfig = new MockMailetConfig("Dummy",mockContext);
    
    private CheckerMailet checkerMailet;
    
    private class MyMailet extends GenericMailet {

        public void service(Mail mail) throws MessagingException {
      try {
        MimeMessage message = mail.getMessage () ;
        //Set the header name and value (supplied at init time).
        String newText =  "new text "+counter++;
        System.err.println("Setting body to "+newText);
        message.addHeader("x-Header", newText);
        message.setText(newText);
        message.setSubject(newText);
        message.saveChanges();
      } catch (javax.mail.MessagingException me) {
         log (me.getMessage());
      }
        }
    }

    public LinearProcessorTest(String arg0) throws Exception {
        super(arg0);

        MimeMessageInputStreamSource mmis = null;
        try {
            mmis = new MimeMessageInputStreamSource("test", new SharedByteArrayInputStream((content+sep+body).getBytes()));
        } catch (MessagingException e) {
        }
        mw = new MimeMessageCopyOnWriteProxy(mmis);
        setUp();
  }

    private class DummyMatcherConfig implements MatcherConfig {
        private String condition;

        public DummyMatcherConfig(String config) {
            this.condition = config;
        }
        public String getCondition() {
            return condition;
        }
        
        public MailetContext getMailetContext() {
            return mockContext;
        }

        public String getMatcherName() {
            return "All";
        }
    }
    
    public void testCopyOnWrite() throws IOException, MessagingException {
        t.setSpool(new MockSpoolRepository());
        Matcher recipientIs = new RecipientIs();
        recipientIs.init(new DummyMatcherConfig("rec1@domain.com"));
        
        Matcher all = new All();
        all.init(new DummyMatcherConfig(""));
        
        Mailet changeBody = new MyMailet();
        Mailet changeBody2 = new MyMailet();

        changeBody.init(mockMailetConfig);
        changeBody2.init(mockMailetConfig);
        
        Mailet dumpSystemErr = new DumpSystemErr();
        changeBody.init(mockMailetConfig);
        
        checkerMailet = new CheckerMailet();
        t.add(recipientIs,changeBody);
        t.add(all,changeBody);
        t.add(all,dumpSystemErr);
        t.add(all,checkerMailet);
        t.closeProcessorLists();

        Collection recipients = new ArrayList();
        recipients.add(new MailAddress("rec1","domain.com"));
        recipients.add(new MailAddress("rec2","domain.com"));
        try {
            MailImpl m = new MailImpl("mail1",new MailAddress("sender","domain.com"),recipients,mw);
            t.service(m);
            ArrayList a = checkerMailet.receivedMails;
            assertEquals(2,a.size());
            MimeMessage m1 = ((Mail) a.get(0)).getMessage();
            MimeMessage m2 = ((Mail) a.get(1)).getMessage();
            assertNotSame(m1,m2);
            assertEquals(m1.getSubject(),"new text 1");
            assertEquals(m2.getSubject(),"new text 2");
            m.dispose();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testStateChange() throws IOException, MessagingException {
        t.setSpool(new MockSpoolRepository() {
            public void store(Mail mc) throws MessagingException {
                assertEquals("MYSTATE",mc.getState());
                super.store(mc);
            }
        });
        
        Matcher recipientIs = new RecipientIs();
        recipientIs.init(new DummyMatcherConfig("rec1@domain.com"));
        
        Matcher all = new All();
        all.init(new DummyMatcherConfig(""));
        
        Mailet dumpSystemErr = new DumpSystemErr();
        
        checkerMailet = new CheckerMailet();
        t.add(recipientIs,dumpSystemErr);
        t.add(all,dumpSystemErr);
        t.add(all,checkerMailet);
        t.closeProcessorLists();

        Collection recipients = new ArrayList();
        recipients.add(new MailAddress("rec1","domain.com"));
        recipients.add(new MailAddress("rec2","domain.com"));
        try {
            MailImpl m = new MailImpl("mail1",new MailAddress("sender","domain.com"),recipients,mw);
            m.setState("MYSTATE");
            t.service(m);
            ArrayList a = checkerMailet.receivedMails;
            assertEquals(2,a.size());
            MimeMessage m1 = ((Mail) a.get(0)).getMessage();
            MimeMessage m2 = ((Mail) a.get(1)).getMessage();
            assertNotSame(m1,m2);
            assertEquals("MYSTATE",((Mail) a.get(0)).getState());
            assertEquals("MYSTATE",((Mail) a.get(1)).getState());
            m.dispose();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setUp() throws Exception {
        super.setUp();
        t = new LinearProcessor();
        Logger l = new ConsoleLogger();
        t.enableLogging(l);
        t.initialize();
        
    }
    
    public void tearDown() throws Exception {
        t.dispose();
        super.tearDown();
    }
    
}
