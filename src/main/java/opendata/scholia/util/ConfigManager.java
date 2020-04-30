package opendata.scholia.util;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.PropertiesConfiguration;



public class ConfigManager {
    private String propertiesFileName = "";
    private static ConfigManager _instance = null;
    private  PropertiesConfiguration config;
    
    public  PropertiesConfiguration getConfig() {
		return config;
	}
	
	protected ConfigManager() {
        config = new PropertiesConfiguration();
        
        propertiesFileName = System.getenv("SCHOLIA_CONFIG");
        
        Configurations configs = new Configurations();
		try
		{
			FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
				    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
				    .configure(new Parameters().properties()
				        .setFileName(propertiesFileName)
				        .setThrowExceptionOnMissing(true)
//				        .setListDelimiterHandler(new DefaultListDelimiterHandler(';'))
				        .setIncludesAllowed(false));
			 config = builder.getConfiguration();
//		     Configuration config = configs.properties(new File(PROPERTIES_FILE_NAME));
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