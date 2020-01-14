package com.iotfitness.assistedtraining.actuator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class CaloriesActuator {

	public CaloriesActuator() {

		CoapServer server = new CoapServer(5689);

		server.add(new SetCaloriesCount());

		server.start();

	}

	public static class SetCaloriesCount extends CoapResource {
		public SetCaloriesCount() {

			super("setCaloriesCount");

			getAttributes().setTitle("Set Calories Count");
		}

		@Override
		public void handlePOST(CoapExchange exchange) {

			exchange.respond(ResponseCode.CONTENT, "{\"message\":\"POST_REQUEST_SUCCESS\"}",
					MediaTypeRegistry.APPLICATION_JSON);

			JSONObject json = new JSONObject(exchange.getRequestText());
			String data = json.get("CaloriesCount").toString();

			BufferedWriter bw = null;

			try {
				bw = new BufferedWriter(new FileWriter(new File("Calories.txt")));
				bw.write(data);
			}

			catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}

			finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
