package com.tibcopsg.tibcobw.customfunctions.bw;


import com.tibco.xml.cxf.common.annotations.*;

@XPathFunctionGroup(category = "Custom Functions Time", prefix = "cf_time", namespace = "http://com.tibcopsg.tibcobw.customfunctions.time", helpText = "Custom defined function for time")
public class FTime
{
	
	// In case you need static initialization
	//static {
		// 
	//}

	@XPathFunction(
			helpText = "TimestampMSToXMLDate - Converts a time represented in milleseconds (since January 1, 1970, 00:00:00 GMT) into XML compliant (ISO8601) Date - Example - TimestampMSToXMLDate(1332768306418) returns [2012-03-26T15:25:06.418+02:00].",
			parameters = { @XPathFunctionParameter(name = "MSdate", optional = false, optionalValue = "") })
	public static String TimestampMSToXMLDate(long MSdate)
	{
		// Using 
		try {
            String FORMATER = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
            java.text.DateFormat format = new java.text.SimpleDateFormat(FORMATER);
            return format.format(MSdate);
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
	
	public static void main(String[] args)
	{
		try {
			
			// Conversions XML date and date millisec
			long datems = System.currentTimeMillis(); 
			String sDate = TimestampMSToXMLDate(datems);
			System.out.println("TimestampMSToXMLDate(" + Long.toString(datems) + ") returns [" + sDate + "].");
			//System.out.println("XMLDateToTimestampMS(" + sDate + ") returns [" + XMLDateToTimestampMS(sDate) + "].");
			System.out.println("XMLDateToTimestampMS(" + sDate + ") returns [" + XMLDateToTimestampMS(sDate) + "].");
			
			//2019-05-27T21:30:21.493+0530
			
		}catch (Exception e)
		{
			System.out.println("Failure: "  + e.toString() + " - " + e.getMessage());
		}
	}
}
