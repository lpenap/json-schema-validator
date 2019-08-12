package com.penapereira.json.validator.util;

import java.io.IOException;
import java.util.Map;

import com.penapereira.json.validator.exception.ResourceNotFoundException;

public interface UtilInterface {

	String requestJson(String endpoint, String method, Map<String, String> headers) throws IOException, ResourceNotFoundException;

	String getFileContents(String path) throws ResourceNotFoundException;

}