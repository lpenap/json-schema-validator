package com.digitalicagroup.json.validator.launcher;

import java.io.IOException;

import com.digitalicagroup.json.validator.JSONValidator;
import com.digitalicagroup.json.validator.properties.InvalidPropertiesException;
import com.digitalicagroup.json.validator.properties.UnsupportedPropertiesVersionException;
import com.digitalicagroup.json.validator.properties.ValidatorProperties;

public class Launcher {

	public static void main(String[] args) throws IOException {
		System.out.println("\n###############################################################################");
		System.out.println("Loading Validator...");
		JSONValidator validator = null;
		try {
			validator = new JSONValidator(ValidatorProperties.loadQueue());
			System.out.println("Running validator...");
			validator.executeQueue();
		} catch (UnsupportedPropertiesVersionException e) {
			System.out.println("Unsupported version of properties json. Aborting.");
		} catch (InvalidPropertiesException e) {
			System.out.println("Invalid properties json. Aborting.");
		}

		System.out.println("Done.");
		System.out.println("###############################################################################\n");
	}

}
