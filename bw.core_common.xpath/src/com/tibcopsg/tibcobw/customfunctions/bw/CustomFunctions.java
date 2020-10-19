package com.tibcopsg.tibcobw.customfunctions.bw;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import com.tibco.xml.cxf.common.annotations.XPathFunction;
import com.tibco.xml.cxf.common.annotations.XPathFunctionGroup;
import com.tibco.xml.cxf.common.annotations.XPathFunctionParameter;

@XPathFunctionGroup(category = "Custom Functions BW misc", prefix = "cf_bwmisc", namespace = "http://com.tibcopsg.tibcobw.customfunctions.bw", helpText = "Custom defined function for BW misc")
public class CustomFunctions {
	private static Random m_random;
	private static String localHostName = null;
	private static String fileSeparator = null;

	static {
		m_random = new Random();

		try {
			localHostName = java.net.InetAddress.getLocalHost().getHostName();
			fileSeparator = new Character(java.io.File.separatorChar)
					.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	public static final String HELP_STRINGS[][] = {
			{
					"getGUID",
					"same as getUUIDNoDashes : Generate globally unique 128bit UUID without dashes('-').",
					"Example",
					"getUUIDNoDashes returns dcd05d5f6b35465eb588e7c72e3112d9" },
			{
					"getGUIDwithTimeStamp",
					"same as getGUID : Generate globally unique 128bit UUID without dashes('-') plus TimeStamp with milliseconds",
					"Example",
					"getGUIDwithTimeStamp returns dcd05d5f6b35465eb588e7c72e3112d920160404120808532" },
			{ "getUUID",
					"Generate globally unique 128bit UUID with dashes('-').",
					"Example",
					"getUUID returns 360b03f4-61b9-4eec-b0d8-8ceb31cbd570" },
			{
					"getUUIDNoDashes",
					"Generate globally unique 128bit UUID without dashes('-').",
					"Example",
					"getUUIDNoDashes returns dcd05d5f6b35465eb588e7c72e3112d9" },
			{ "getRandom", "Generate a random float in [0..1]", "Example",
					"getRandom returns 0.78" },
			{
					"getLocalHostname",
					"Returns the local Host name where this BW engines runs. This function caches the hostname. Use this for all your BW project implementations!",
					"Example", "getLocalHostName() returns myhost1" },
			{
					"getFS",
					"Returns the system-dependent file separator character, represented as a string for convenience.This function caches the FileSeparator. Use this for all your BW project implementations!",
					"Example", "getSP() returns \"\\\"" },
			{ "getEnvString",
					"Returns the value of an environment system variable.",
					"Example",
					"getEnvString(\"PATH\") returns the value of the environment PATH variable." },
			{
					"getHashcode",
					"Returns a hashcode value for a key. The value will be between 1 and max (included). This function is immutable.",
					"Example", "getHashcode(\"test\", 2) returns 1 or 2." },
			{ "getTimestampNanoSeconds",
					"Returns the value of timestamp in NanoSeconds.",
					"Example", "getTimestampNanoSeconds()." } };

	/**
	 * same as getUUIDNoDashes : Function generates a 128 bit UUID, based on the
	 * java.util.UUID class, but replaces the dashes('-') A class that
	 * represents an immutable universally unique identifier (UUID). A UUID
	 * represents a 128-bit value. example: dcd05d5f6b35465eb588e7c72e3112d9
	 * 
	 * @return string : UUID without dashes
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getGUID() {
		return getUUIDNoDashes();
	}

	/**
	 * same as getGUIDwithTimeStamp : Function generates a 128 bit UUID +
	 * TimeStamp in milliseconds Based on the java.util.UUID class, but replaces
	 * the dashes('-') A class that represents an immutable universally unique
	 * identifier (UUID). A UUID represents a 128-bit value. example:
	 * dcd05d5f6b35465eb588e7c72e3112d9
	 * 
	 * @return string : UUID without dashes + TimeStamp with milliseconds
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getGUIDwithTimeStamp() {
		String valueTimeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS")
				.format(new Date());
		String UUIDD = getUUIDNoDashes();
		return UUIDD + valueTimeStamp;
	}

	/**
	 * Function generates a 128 bit UUID, based on the java.util.UUID class. A
	 * class that represents an immutable universally unique identifier (UUID).
	 * A UUID represents a 128-bit value. example:
	 * 360b03f4-61b9-4eec-b0d8-8ceb31cbd570
	 * 
	 * @return string : UUID without dashes
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getUUID() {
		try {

			return (java.util.UUID.randomUUID().toString());
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Function generates a 128 bit UUID, based on the java.util.UUID class, but
	 * replaces the dashes('-') A class that represents an immutable universally
	 * unique identifier (UUID). A UUID represents a 128-bit value. example:
	 * dcd05d5f6b35465eb588e7c72e3112d9
	 * 
	 * @return string : UUID without dashes
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getUUIDNoDashes() {
		// final int DASH_POS_01=8;
		// final int DASH_POS_02=13;
		// final int DASH_POS_03=18;
		// final int DASH_POS_04=23;

		try {
			String r1 = java.util.UUID.randomUUID().toString();
			char[] arr1 = r1.toCharArray();
			int length = r1.length();
			char[] arr2 = new char[length - 4];

			// example arr1 : 360b03f4-61b9-4eec-b0d8-8ceb31cbd570
			System.arraycopy(arr1, 0, arr2, 0, 8);
			System.arraycopy(arr1, 9, arr2, 8, 4);
			System.arraycopy(arr1, 14, arr2, 12, 4);
			System.arraycopy(arr1, 19, arr2, 16, 4);
			System.arraycopy(arr1, 24, arr2, 20, 12);

			r1 = new String(arr2);
			return (r1);
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Generate a random float in [0..1] Example : 0.78
	 * 
	 * @return float : random number
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static float getRandom() {
		try {
			return m_random.nextFloat();
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Returns the local Host name where this BW engines runs (cached version).
	 * Example : myhost1
	 * 
	 * @return
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getLocalHostname() {
		try {
			return localHostName;
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * "Returns the system-dependent file separator character, represented as a string for convenience.(cached)"
	 * , "Example", "getSP() returns \"\\\""},
	 * 
	 * @return string : File separator
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getFS() {
		try {
			return fileSeparator;
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Returns the value of an environment system variable. Example
	 * getEnvString("PATH") would return the value of the environment PATH
	 * variable.
	 * 
	 * @param envVar
	 *            name of the environment variable to get the value for
	 * @return
	 */
	@XPathFunction(helpText = "", parameters = { @XPathFunctionParameter(name = "envVar", optional = false, optionalValue = "") })
	public static String getEnvString(String envVar) {
		try {
			String res = java.lang.System.getenv(envVar);
			if (res == null)
				return "";
			else
				return res;
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Returns an hashcode
	 * 
	 * @param key
	 *            :
	 * @return int: hashcode, between 0 (including) and max (excluding)
	 */

	/**
	 * Returns a hashcode for a string between 1 and a max number
	 * 
	 * @param key
	 *            String to hashcode
	 * @param max
	 *            Max value of the hashcode (included)
	 * @return int Returned hashcode
	 */
	@XPathFunction(helpText = "", parameters = {
			@XPathFunctionParameter(name = "key", optional = false, optionalValue = ""),
			@XPathFunctionParameter(name = "max", optional = false, optionalValue = "") })
	public static int getHashcode(String key, int max) {
		if (max == 0) {
			throw new RuntimeException(
					"Unable to hash key, zero is a forbidden value");
		}

		try {
			int hashcode = key.hashCode();
			if (hashcode < 0) {
				hashcode = hashcode * (-1);
			}
			return (hashcode % max) + 1;
		} catch (Exception e) {
			throw new RuntimeException("Unable to hash string " + key + ", "
					+ e.toString() + ": " + e.getMessage());
		}
	}

	/**
	 * Function generates a 128 bit UUID, based on the java.util.UUID class. A
	 * class that represents an immutable universally unique identifier (UUID).
	 * A UUID represents a 128-bit value. example:
	 * 360b03f4-61b9-4eec-b0d8-8ceb31cbd570
	 * 
	 * @return : UUID without dashes
	 */
	@XPathFunction(helpText = "", parameters = {})
	public static String getTimestampNanoSeconds() {
		try {
			String valueTimeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS")
					.format(new Date());
			String valueNanoSeconds = String.valueOf(System.nanoTime());

			return (valueTimeStamp + valueNanoSeconds);
		} catch (Exception e) {
			throw new RuntimeException(e.toString() + ": " + e.getMessage());
		}
	}

	@XPathFunction(helpText = "", parameters = { @XPathFunctionParameter(name = "string", optional = false, optionalValue = "") })
	public static String rTrim(String string) {
		if (string != null) {
			return string.replaceAll("\\s+$", "");
		}
		return null;
	}

	@XPathFunction(helpText = "TimestampToXMLDate - Converts a timestamp into XML compliant (ISO8601) Date - Example - TimestampToXMLDate(1582339860) returns [2020-02-22T03:51:00.000+01:00].", parameters = { @XPathFunctionParameter(name = "MSdate", optional = false, optionalValue = "") })
	public static String TimestampToXMLDate(long MSdate) {
		try {
			String FORMATER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
			java.text.DateFormat format = new java.text.SimpleDateFormat(
					FORMATER);

			return (String) format.format(MSdate * 1000L);

		} catch (Exception ex) {
			throw new RuntimeException(
					"Exception while converting timestamp to XML date - "
							+ ex.toString());
		}

	}

	@XPathFunction(helpText = "XMLDateToTimestamp - Converts an XML compliant Date (ISO8601) into timestamp (since January 1, 1970, 00:00:00 GMT) - Example XMLDateToTimestampMS(\"2020-02-22T03:51:00.000+01:00\") returns [1582339860].", parameters = { @XPathFunctionParameter(name = "sDate", optional = false, optionalValue = "") })
	public static long XMLDateToTimestamp(String sDate) {
		try {

			String FORMATER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
			java.util.Date dDate = new java.text.SimpleDateFormat(FORMATER)
					.parse(sDate);
			return dDate.getTime() / 1000L;
		} catch (Exception ex) {
			throw new RuntimeException(
					"Exception while converting XML date to timestamp - "
							+ ex.toString());
		}
	}

	@XPathFunction(helpText = "XMLDateToNumberOfDays - Returns number of days (since January 1, 1970, 00:00:00 GMT) for the input date - Example XMLDateToNumberofDays(\"2020-02-21\") returns [18313].", parameters = { @XPathFunctionParameter(name = "sDate", optional = false, optionalValue = "") })
	public static long XMLDateToNumberOfDays(String sDate) {
		try {

			LocalDate dDate = LocalDate.parse(sDate);
			LocalDate then = LocalDate.of(1970, Month.JANUARY,1);
			long diff = ChronoUnit.DAYS.between(then, dDate);

			return diff;
			
		} catch (Exception ex) {
			throw new RuntimeException(
					"Exception while calculating number of days from XMLDate - "
							+ ex.toString());
		}
	}
	
	@XPathFunction(helpText = "NumberOfDaysToXMLDate - Returns an XML compliant Date (ISO8601) (since January 1, 1970, 00:00:00 GMT) from number of days in input - Example NumberofDaysToXMLDate(18314) returns [2020-02-21].", parameters = { @XPathFunctionParameter(name = "numberOfDays", optional = false, optionalValue = "") })
	public static String NumberOfDaysToXMLDate(long numberOfDays) {
		try {
			Date date = new SimpleDateFormat("D").parse(String.valueOf(numberOfDays));
			String FORMATER = "yyyy-MM-dd";
			java.text.DateFormat format = new java.text.SimpleDateFormat(
					FORMATER);

			return (String) format.format(date);
			
		} catch (Exception ex) {
			throw new RuntimeException(
					"Exception while calculating XMLDate from number of days - "
							+ ex.toString());
		}
	}
	
	@XPathFunction(
			helpText = "TimestampMSToXMLDate - Converts a time represented in milleseconds (since January 1, 1970, 00:00:00 GMT) into XML compliant (ISO8601) Date - Example - TimestampMSToXMLDate(\"1332768306418\") returns [2012-03-26T15:25:06.418+02:00].",
			parameters = { @XPathFunctionParameter(name = "MSdate", optional = false, optionalValue = "") })
	public static String TimestampMSToXMLDate(String MSdate)
	{
		// Using 
		try {
			BigInteger bd = new BigInteger(MSdate);
            String FORMATER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
            java.text.DateFormat format = new java.text.SimpleDateFormat(FORMATER);
            return format.format(bd);
        }
        catch (Exception ex) {
            throw new RuntimeException ("Exception while converting millisecond timestamp to XML Date - " + ex.toString());
        }		
	}
	
	@XPathFunction(helpText = "XMLDateToTimestampMS - Converts an XML compliant Date (ISO8601) into time in millisec (since January 1, 1970, 00:00:00 GMT) - Example XMLDateToTimestampMS(\"2012-03-26T15:25:06.418+02:00\") returns [1332768306418].",
			parameters = { @XPathFunctionParameter(name = "sDate", optional = false, optionalValue = "") })
	public static long XMLDateToTimestampMS(String sDate)
	{
		try {
			
			String FORMATER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
			java.util.Date dDate = new java.text.SimpleDateFormat(FORMATER).parse(sDate);
            return dDate.getTime();   
		}
        catch (Exception ex) {
            throw new RuntimeException ("Exception while converting XML date to timestamp in milliseconds - " + ex.toString());
        }		
	}
	
	public static void main(String[] args) {
		System.out.println("getGUID() = " + CustomFunctions.getGUID());
		System.out.println("getUUID() = " + CustomFunctions.getUUID());
		System.out.println("getRandom() = " + CustomFunctions.getRandom());
		System.out.println("getLocalHostname() = "
				+ CustomFunctions.getLocalHostname());
		System.out.println("getFS() = " + CustomFunctions.getFS());
		System.out.println("getEnvString(\"PATH\") = "
				+ CustomFunctions.getEnvString("PATH"));
		System.out.println("getHashcode(\"test\", 2) = "
				+ getHashcode("test", 2));

		System.out.println("getTimestampNanoSeconds() = "
				+ CustomFunctions.getTimestampNanoSeconds());
		System.out.println("TimestampToXMLDate(1592784000)"
				+ TimestampToXMLDate(1592784000));
		System.out.println("TimestampMSToXMLDate(\"1332768306418\")"
				+ TimestampMSToXMLDate("1332768306418"));
		System.out.println("XMLDateToTimestamp(2020-02-22T03:51:00.000+01:00)"
				+ XMLDateToTimestamp("2020-02-22T03:51:00.000+01:00"));
		System.out.println("XMLDateToTimestampMS(2012-03-26T15:25:06.418+02:00)"
				+ XMLDateToTimestampMS("2012-03-26T15:25:06.418+02:00"));
		System.out.println("XMLDateToNumberofDays(2020-02-21)"
				+ XMLDateToNumberOfDays("2020-02-21"));
		System.out.println("NumberOfDaysToXMLDate(18314)"
				+ NumberOfDaysToXMLDate(18314));
		
		
	}

}
