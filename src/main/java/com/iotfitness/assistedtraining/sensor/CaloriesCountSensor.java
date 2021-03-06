package com.iotfitness.assistedtraining.sensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.stereotype.Component;

/* This class generates the Calories Count Sensor 
	and implements CoAP Server. */

@Component
public class CaloriesCountSensor {

	/*
	 * This constructor initiates CoAP Server with assigned Port and adds CoAP
	 * Resource to it.
	 */

	public CaloriesCountSensor() {

		CoapServer server = new CoapServer(5686);

		server.add(new GetCaloriesCount());

		server.start();

	}

	/*
	 * This class extends the CoAP Resource and handles the CoAP Resource methods.
	 */

	public static class GetCaloriesCount extends CoapResource {

		// This constructor adds new CoAP Resource.

		public GetCaloriesCount() {

			super("getCaloriesCount");

			getAttributes().setTitle("Get Calories Count");
		}

		@Override
		public void handleGET(CoapExchange exchange) {

			StringBuilder Calories = new StringBuilder();
			BufferedWriter bw = null;
			BufferedReader br = null;

			if (!(new File("Calories.txt")).exists()) {
				try {
					bw = new BufferedWriter(new FileWriter(new File("Calories.txt")));
					bw.write("0");
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

			try {
				br = Files.newBufferedReader(Paths.get("Calories.txt").toAbsolutePath());

				String line;
				while ((line = br.readLine()) != null) {
					Calories.append(line);
				}
			}

			catch (IOException e) {
				System.err.format("IOException: %s%n", e);
			}

			finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			exchange.respond(ResponseCode.CONTENT, "{\"CaloriesCount\":" + Calories + "}",
					MediaTypeRegistry.APPLICATION_JSON);

		}
	}
}
