package com.androidtsubu.jdbeats.util;

public class Validator {

	/**
	 * 
	 * @param value
	 * @param defValue
	 * @return
	 */
	public static String validate(String value, int defValue) {
		if (value == null) {
			return String.valueOf(defValue);
		}

		try {
			Integer.valueOf(value);
			return value;
		} catch (Exception e) {
			return String.valueOf(defValue);
		}
	}
	
	public static String validate(String value, double defValue) {
		if (value == null) {
			return String.valueOf(defValue);
		}

		try {
			Double.valueOf(value);
			return value;
		} catch (Exception e) {
			return String.valueOf(defValue);
		}
	}
	
}
