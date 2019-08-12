package com.penapereira.json.validator;

import java.io.IOException;

import org.everit.json.schema.ValidationException;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.penapereira.json.validator.exception.ResourceNotFoundException;
import com.penapereira.json.validator.exception.UnsupportedPropertiesVersionException;
import com.penapereira.json.validator.properties.ValidatorPreferences;

public class CliLauncher {
	private static final Logger log = LoggerFactory.getLogger(CliLauncher.class);

	public static void main(String[] args) throws IOException {
		log.info("Starting json-schema-validator");
		JSONValidator validator = null;
		try {
			setPropertiesJson(args.length > 0 ? args[0] : null);
			validator = new JSONValidator(ValidatorPreferences.instance().getQueue());
			validator.executeQueue();
		} catch (UnsupportedPropertiesVersionException e) {
			log.error("Unsupported version of preferences json.");
		} catch (JSONException e) {
			log.error("Error parsing preferences Json.");
		} catch (ValidationException e) {
			log.error("Preferences Json did not match expected json schema.");
		} catch (ResourceNotFoundException e) {
			log.error("Preferences Json not found.");
		}
		log.info("Done json-schema-validator");
	}

	private static void setPropertiesJson(String path) {
		if (path != null) {
			log.info(String.format("  Using %s as preferences file.", path));
			ValidatorPreferences.instance().setPropertiesFile(path);
		} else {
			log.info("  Preferences file was not specified, running bundled examples.");
		}
	}

}
