/* Author: Tyler Mooney
 * Date: October 3rd, 2020
 * Professor: Khan
 * Referenced libraries: gson-2.6.2.jar
 * 
 * Purpose: After being created after a phrase is picked up by the ChatBot,
 * takes in a String, determines if this String is suppose to be a zip or city,
 * then 
 * 
 * Notes:
 * API key is private, you'll have to get your own
 * I should probably set up some kind of value that can be returned when there
 * is an error. That way the bot can output the user didn't have correct input,
 * or maybe I can put that in the ChatBot.java class
 * -1 might not be the best value error value, but the chances of the temperature
 * being exactly -1 is unlikely.
 * TODO set up a better high and low temperature, so it shows a high and low for the day
 * 
 * Useful Links:
 * https://www.baeldung.com/java-http-request
 * https://www.tutorialspoint.com/how-to-handle-malformedurlexception-in-java
 * https://stackoverflow.com/questions/3280353/how-to-import-a-jar-in-eclipse
 * https://openweathermap.org/current#current_JSON
 * https://javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/JsonObject.html
 * https://www.oracle.com/technical-resources/articles/java/json.html
 * https://www.rapidtables.com/convert/temperature/how-kelvin-to-fahrenheit.html
 */

import java.net.HttpURLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Weather 
{
	//default constructor
	public Weather() 
	{
		
	}
	
	// Main method of class
	public static double[] mainWeather(String input) throws IOException
	{
		//Variable initialization
		String api_key = "";
		JsonObject myObj = new JsonObject();
		int zip = 0;
		double temp = 0, tempMax = 0, tempMin = 0;
		String city = "";

        // check if input is zip code or city
        try
        {
            zip = Integer.parseInt(input);
        }
        catch(Exception e)
        {
            city = input;
        }

        //Creating the api link based on if city or zip
        if(city == "")
        {
        	//Creating api link based on zip
        	String endpoint = String.format("http://api.openweathermap.org/data/2.5/forecast?zip=%d,us&appid=%s",zip, api_key);
            myObj = getWeather(endpoint);
            
            //Checking if getWeather was successful, if not, returning error values
            if(myObj == null)
            {
            	double [] array = {-1, -1, -1};
            	return array;
            }
            
            //Acquiring temperature values
            temp = getTemperature(myObj);
            tempMax = getTemperatureMax(myObj);
            tempMin = getTemperatureMin(myObj);
        } 
        else 
        {
            //Creating api link based on city
            String endpoint = String.format("http://api.openweathermap.org/data/2.5/forecast?q=%s,us&appid=%s", city, api_key);
            myObj = getWeather(endpoint);
            
            //Checking if getWeather was successful, if not, returning error values
            if(myObj == null)
            {
            	double [] array = {-1, -1, -1};
            	return array;
            }
            
            //Acquiring temperature values
            temp = getTemperature(myObj);
            tempMax = getTemperatureMax(myObj);
            tempMin = getTemperatureMin(myObj);
        }
        
        // returning double array, so it can be used by Bot
        double [] tempArray = {temp, tempMax, tempMin};
        return tempArray;
	}
	
	// Acquires the JsonObject
	public static JsonObject getWeather(String endpoint) throws IOException{
		
		// Create URL object
        URL url = new URL(endpoint);
        
        // Open a connection(?) on the URL(?) and cast the response(??)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        
        // Setting the header
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");

        // Make the request and read the response
        try 
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) 
            {
                content.append(inputLine);
            }
            in.close();
            
            // Create the Json Object, and parse it
            JsonParser parser = new JsonParser();
            JsonObject res2 = parser.parse(content.toString()).getAsJsonObject();
            
            //Close the connection
            connection.disconnect();
            
            return res2;
        }
        catch(IOException e) 
        {
            // Handle Error Exception, print out error code
            int status = connection.getResponseCode();
            System.out.println(String.format("Something went wrong, STATUS CODE: %d", status));
            
            //Close the connection
            connection.disconnect();
            return null;
        }
	}
	
	// Acquire current Temperature
	public static double getTemperature(JsonObject myObj) 
	{
		// Parsing the Json for a specific data field
		double value = ((JsonObject) ((JsonObject) myObj.getAsJsonArray("list").get(0)).get("main")).get("temp").getAsDouble();
		
		//Converting from kelvins to Fahrenheit
    	value = value * 9/5 - 459.67;
    	value = Math.round(value * 10) / 10.0;
        
		return value;
	}
	
	// Acquire current Max Temperature
	public static double getTemperatureMax(JsonObject myObj) 
	{
		// Parsing the Json for a specific data field
		double value = ((JsonObject) ((JsonObject) myObj.getAsJsonArray("list").get(0)).get("main")).get("temp_max").getAsDouble();
		
		//Converting from kelvins to Fahrenheit
    	value = value * 9/5 - 459.67;
    	value = Math.round(value * 10) / 10.0;
        
		return value;
	}

	// Acquire current Minimum Temperature
	public static double getTemperatureMin(JsonObject myObj) 
	{
		// Parsing the Json for a specific data field
		double value = ((JsonObject) ((JsonObject) myObj.getAsJsonArray("list").get(0)).get("main")).get("temp_min").getAsDouble();
	
		//Converting from kelvins to Fahrenheit
		value = value * 9/5 - 459.67;
		value = Math.round(value * 10) / 10.0;
    
		return value;
	}
}
