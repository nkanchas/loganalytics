package com.analytics.loganalytics;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.io.BufferedReader;

import java.io.InputStreamReader;

public class ProcessBuilder {
	
	/**
	 * Returns top response codes based  on log
	 * @param logpath
	 * @return
	 */
	public static String getTopResponseCodes(String logpath){
		String[] cmd = { "/bin/sh", "-c", 
				"cat " + logpath + " | awk '{print $9}' | sort | uniq -c | sort -nr" };

		JsonObject jo = new JsonObject();
		List<String> keys = new ArrayList<>(Arrays.asList("hits", "http-code"));

		jo.put("User-Agents",  ProcessBuilder.executeTopCommand(cmd, keys));

		return jo.toString();
	}

	/** gets all user -agents based on most usage count
	 * 
	 * @param logpath
	 * @return
	 */
	public static String getAllUniqueAgents(String logpath) {
		String[] cmd = { "/bin/sh", "-c", "cat " + logpath + " | awk -F\\\" '{ print $6 }' |  sort | uniq -c | sort -nr | more -n 10" };

		JsonObject jo = new JsonObject();
		List<String> keys = new ArrayList<>(Arrays.asList("hits", "user-agent"));

		jo.put("User-Agents", ProcessBuilder.executeTopCommand(cmd, keys));

		return jo.toString();

	}

	/** gets count for HTTP method access
	 * 
	 * @param httpMethod
	 * @param logpath
	 * @return
	 */
	public static String getHTTMethodCount(String httpMethod, String logpath) {
		String[] cmd = { "/bin/sh", "-c", "awk -F\\\" '($2 ~ \"" + httpMethod + "\")' " + logpath + " | wc -l" };

		JsonObject jo = new JsonObject();
		jo.put("count", ProcessBuilder.executeCountCommand(cmd));

		return jo.toString();
	}

	/** get top user agents
	 * 
	 * @param number
	 * @param logPath
	 * @return
	 */
	public static String getTopUserAgents(int number, String logPath) {
		String[] cmd = { "/bin/sh", "-c",
				"cat " + logPath + " | awk -F\\\" '{ print $6 }' | sort | uniq -c | sort -frn | head -n " + number };
		ProcessBuilder obj = new ProcessBuilder();
		List<String> keys = new ArrayList<>(Arrays.asList("hits", "user-agent"));
		JsonObject jo = new JsonObject();
		jo.put("Top-UserAgents", ProcessBuilder.executeTopCommand(cmd, keys));

		return jo.toString();
	}

	/**
	 * 
	 * @param number
	 * @param logPath
	 * @return
	 */
	public static String getTopUrls(int number, String logPath) {
		String[] cmd = { "/bin/sh", "-c",
				"cut -d'\"' -f4 " + logPath + " | grep -v '^-$' | sort | uniq -c | sort -rg | head -n" + number };

		List<String> keys = new ArrayList<>(Arrays.asList("hits", "url"));

		JsonObject jo = new JsonObject();
		jo.put("TopUrls", ProcessBuilder.executeTopCommand(cmd, keys));

		return jo.toString();
	}

	private static String executeCountCommand(String[] cmd) {
		Process p;
		String count = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				count = line.trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	private static JsonArray executeTopCommand(String[] command, List<String> keys) {

		JsonArray jarray = new JsonArray();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(">>> line " + line);
				StringTokenizer stk = new StringTokenizer(line, " ");
				try {
					JsonObject jsonHits = new JsonObject().put(keys.get(0), stk.nextToken()).put(keys.get(1),
							stk.nextToken());

					jarray.add(jsonHits);
				} catch (NoSuchElementException ex) {

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jarray;

	}

}
