package com.penapereira.json.validator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penapereira.json.validator.properties.InvalidPropertiesException;
import com.penapereira.json.validator.properties.UnsupportedPropertiesVersionException;
import com.penapereira.json.validator.properties.ValidatorProperties;

public class Launcher {

	public static void main(String[] args) throws IOException {
		Logger log = LoggerFactory.getLogger(Launcher.class);
		log.info("Loading Validator...");
		JSONValidator validator = null;
		try {
			validator = new JSONValidator(ValidatorProperties.loadQueue());
			validator.executeQueue();
		} catch (UnsupportedPropertiesVersionException e) {
			log.error("Unsupported version of properties json. Aborting.");
		} catch (InvalidPropertiesException e) {
			log.error("Invalid properties json. Aborting.");
		}

		log.info("Done.");
	}

}
