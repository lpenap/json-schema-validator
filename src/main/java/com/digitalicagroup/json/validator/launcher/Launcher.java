package com.digitalicagroup.json.validator.launcher;

import java.io.IOException;

import com.digitalicagroup.json.validator.JSONValidator;
import com.digitalicagroup.json.validator.properties.InvalidPropertiesException;
import com.digitalicagroup.json.validator.properties.UnsupportedPropertiesVersionException;
import com.digitalicagroup.json.validator.properties.ValidatorProperties;

public class Launcher {

	public static void main(String[] args)
			throws IOException, UnsupportedPropertiesVersionException, InvalidPropertiesException {

		System.out.println("Loading Validator...");
		JSONValidator validator = new JSONValidator(ValidatorProperties.loadQueue());

		System.out.println("Running validator...");
		validator.executeQueue();

		System.out.println("Done");
	}

}
