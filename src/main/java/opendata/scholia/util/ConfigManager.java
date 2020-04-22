package opendata.scholia.util;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.PropertiesConfiguration;


public class ConfigManager {
    private static final String PROPERTIES_FILE_NAME = "configuration.properties";
    static private ConfigManager _instance = null;
    private  PropertiesConfiguration config;
    
    public  PropertiesConfiguration getConfig() {
		return config;
	}
	
	protected ConfigManager() {
        config = new PropertiesConfiguration();
        Configurations configs = new Configurations();
		try
		{
		    Configuration config = configs.properties(new File(PROPERTIES_FILE_NAME));
		}
		catch (ConfigurationException cex)
		{
		    // Something went wrong
		}
        
  
    }
	
    static public ConfigManager instance(){
        if (_instance == null) {
            _instance = new ConfigManager();
        }
        return _instance;
    }

}