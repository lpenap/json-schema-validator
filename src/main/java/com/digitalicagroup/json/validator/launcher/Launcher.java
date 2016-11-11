package com.digitalicagroup.json.validator.launcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.digitalicagroup.json.Util;
import com.digitalicagroup.json.validator.APIParlevelEnvironment;

public class Launcher {

	public static void main(String[] args) throws IOException {
		
		Util util = new Util();

		// large sync
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("token", "c00653abc503361950f72251befedd00d29ba8bf02a58c4aacc5e8a57d88261a");
		String json = util.requestJson("https://" + APIParlevelEnvironment.company_name + ".parlevelvms.com/api/driver/"
				+ APIParlevelEnvironment.driver_id + "/large-sync", "GET", headers);
//		System.out.println(json);
		if (!util.validate("/schema-large-sync.json", json)) {
			System.out.println("#: INVALID");
		}
		
		if (!util.validate("/schema-large-sync.json", util.readFile("/fixtures/LargeSync.json"))) {
			System.out.println("#: INVALID");
		}
	}

}
