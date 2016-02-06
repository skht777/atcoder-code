/**
 * 
 */
package com.skht777.atcoder;

import org.json.JSONObject;

/**
 * @author skht777
 *
 */
public class Submission {

	private String
	id,
	number,
	dateTime, 
	language, 
	status;
	
	/**
	 * 
	 */
	public Submission(JSONObject obj) {
		id = obj.getString("id");
		number = obj.getString("number");
		dateTime = obj.getString("datetime").replaceFirst(" \\+\\d+", "");
		language = obj.getString("language");
		status = obj.getString("status");
	}

	public String getId() {
		return id;
	}
	
	public String getNumber() {
		return number;
	}

	public String getDateTime() {
		return dateTime;
	}

	public String getLanguage() {
		return language;
	}

	public String getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return dateTime + " " + language + " " + status;
	}

}
