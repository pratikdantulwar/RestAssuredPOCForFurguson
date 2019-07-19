package com.RestAPITesting.RestUtilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class FrameworkConfigConstants {

	/**
	 * @author Mukund Z
	 * This code is used to read the property file
	 * @return property file object
	 */
	public static Properties propertyReader() {
		FileReader reader;
		Properties prop = new Properties();
		try {
			reader = new FileReader("framework.properties");
			prop.load(reader);
		} catch (IOException e) {
			Logger.getLogger(com.RestAPITesting.RestUtilities.FrameworkConfigConstants.class.getName()).log(Level.ERROR,
					"The properties file could not be loaded", e);
		}
		return prop;
	}

}
