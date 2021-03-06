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

package org.apache.james;

/**
 * Assorted Constants for use in all James blocks
 * The Software Version, Software Name and Build Date are set by ant during
 * the build process.
 *
 *
 * @version This is $Revision: 494012 $
 */
public class Constants {

    /**
     * The version of James.
     */
    public static final String SOFTWARE_VERSION = "@@VERSION@@";

    /**
     * The name of the software (i.e. James).
     */
    public static final String SOFTWARE_NAME = "@@NAME@@";

    /**
     * Context key used to store the default domain name
     * serviced by this James instance in the context.
     */
    public static final String DEFAULT_DOMAIN = "DEFAULT_DOMAIN";

    /**
     * Context key used to store the Mailet/SMTP "hello name" for this
     * James instance in the context.
     */
    public static final String HELLO_NAME = "HELLO_NAME";

    /**
     * Context key used to store the postmaster address for
     * this James instance in the context.
     */
    public static final String POSTMASTER = "POSTMASTER";

    /**
     * Key used to store the component manager for
     * this James instance in a way accessible by
     * Avalon aware Mailets.
     */
    public static final String AVALON_COMPONENT_MANAGER = "AVALON_COMP_MGR";

    /**
     * Context key used to store the enableAliases configuration for the default
     * LocalUsers Repository.
     */
    public static final String DEFAULT_ENABLE_ALIASES = "JAMES_DEFAULT_ENABLE_ALIASES";

    /**
     * Context key used to store the enableForwarding configuration for the default
     * LocalUsers Repository.
     */
    public static final String DEFAULT_ENABLE_FORWARDING = "JAMES_DEFAULT_ENABLE_FORWARDING";

    /**
     * Context key used to store the ignoreCase configuration for the 
     * UserRepository
     */
    public static final String DEFAULT_IGNORE_USERNAME_CASE = "JAMES_DEFAULT_IGNORE_USERNAME_CASE";

}
