/**
 * 
 */
package org.apache.james;


import org.apache.avalon.framework.configuration.Configuration;
/**
 * @author Wong
 * save Configure 
 */
public class ConfigurationInstance {

    public static Configuration config= null;
    /**
     * 
     */
    private ConfigurationInstance() {
        // TODO Auto-generated constructor stub
    }
     private static class ConfigureInstanceHolder{
            private final static ConfigurationInstance instance=new ConfigurationInstance();
        }
    public static ConfigurationInstance getInstance(){
        return ConfigureInstanceHolder.instance;
    }

}
