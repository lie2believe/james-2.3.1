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

package org.apache.james.userrepository;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.james.ConfigurationInstance;
import org.apache.james.security.DigestUtil;
import org.apache.james.services.User;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Implementation of User Interface. Instances of this class do not allow
 * the the user name to be reset.
 *
 *
 * @version CVS $Revision: 494012 $
 */

public class DefaultUser implements User, Serializable {

    private static final long serialVersionUID = 5178048915868531270L;
    
    private String userName;
    private String hashedPassword;
    private String algorithm ;

    /**
     * Standard constructor.
     *
     * @param name the String name of this user
     * @param hashAlg the algorithm used to generate the hash of the password
     */
    public DefaultUser(String name, String hashAlg) {
        userName = name;
        algorithm = hashAlg;
        getConfigAlgorithm();
    }

    /**
     * Constructor for repositories that are construcing user objects from
     * separate fields, e.g. databases.
     *
     * @param name the String name of this user
     * @param passwordHash the String hash of this users current password
     * @param hashAlg the String algorithm used to generate the hash of the
     * password
     */
    public DefaultUser(String name, String passwordHash, String hashAlg) {
        userName = name;
        hashedPassword = passwordHash;
        algorithm = hashAlg;
        getConfigAlgorithm();
    }

    /**
     * Accessor for immutable name
     *
     * @return the String of this users name
     */
    public String getUserName() {
        return userName;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException{
    	System.out.println( DigestUtil.digestStringUtf8("test123", "MD5"));
    	System.out.println( DigestUtil.digestStringUtf8("test123", "MD5").equals("cc03e747a6afbbcbf8be7668acfebee5"));
    	System.out.println( DigestUtil.digestStringUtf8("test123", "SHA"));
    }
    /**
     *  Method to verify passwords. 
     *
     * @param pass the String that is claimed to be the password for this user
     * @return true if the hash of pass with the current algorithm matches
     * the stored hash.
     */
    public boolean verifyPassword(String pass) {
        try {
            String v = getHashAlgorithm();
            String hashGuess = hashedPassword ;
            if (v!=null || v.trim().length()>0){
                 hashGuess = DigestUtil.digestStringUtf8(pass, v);
            }
            
            return hashedPassword.equals(hashGuess);
        } catch (NoSuchAlgorithmException nsae) {
        throw new RuntimeException("Security error: " + nsae);
    }
    }

    /**
     * Sets new password from String. No checks made on guessability of
     * password.
     *
     * @param newPass the String that is the new password.
     * @return true if newPass successfuly hashed
     */
    public boolean setPassword(String newPass) {
        try {
            String v = getHashAlgorithm();
            hashedPassword = newPass;
            if (v!=null || v.trim().length()>0){
                hashedPassword = DigestUtil.digestStringUtf8(newPass, v);
            }
            return true;
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("Security error: " + nsae);
        }
    }

    /**
     * Method to access hash of password
     *
     * @return the String of the hashed Password
     */
    protected String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Method to access the hashing algorithm of the password.
     *
     * @return the name of the hashing algorithm used for this user's password
     */
    protected String getHashAlgorithm() {
        if ( v_alg!=null){
            return v_alg;
        }
        return algorithm;
    }

    private Configuration algConf = null;
    private boolean isConf = false;
    private String v_alg = null;
    protected String getConfigAlgorithm(){
        if ( !isConf ){
            algConf = ConfigurationInstance.getInstance().config.getChild("pwdalgorithm");
            isConf = true;
                
             if(algConf == null){
                 v_alg = null;
                 return null;
             }
            try {
                v_alg = algConf.getAttribute("value");
            } catch (ConfigurationException e) {
                // TODO Auto-generated catch block
                v_alg=null;
            }
            if (v_alg!=null){
                v_alg = v_alg.trim();
            }
        }
         return v_alg;
    }

}
