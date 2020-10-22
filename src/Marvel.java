/* Author: Tyler Mooney
 * Date: October 3rd, 2020
 * Professor: Khan
 * Referenced libraries: gson-2.6.2.jar
 * 
 * Purpose: Acquire a string from Bot.java, puts the string into the api link,
 * Sends a GET request, acquire JSON, then parse JSON to get the description
 * for the character. and then return the description as a string.
 * 
 * Notes:
 * You can't let people have the api key or hash because it's private
 * base api link: http://gateway.marvel.com/v1/public/
 * basic timestamp: ts=1
 * The database is still in the works, so a lot of information hasn't been put in for a lot characters
 * Some might not even have a single element to them
 * I had to use an api that had a key to it because for some reason I kept getting error code 403
 * if I used an api that require a key to use
 * TODO Create a check to see whether or not the character exists or if there's no description for them yet
 * TODO You need to create an error response
 * 
 * Links:
 * https://www.programmableweb.com/api/marvel-comics
 * https://developer.marvel.com/
 * https://developer.marvel.com/documentation/authorization
 * https://developer.marvel.com/documentation/getting_started
 * https://developer.marvel.com/documentation/attribution
 * https://developer.marvel.com/documentation/apiresults
 * https://developer.marvel.com/documentation/entity_types
 * https://developer.marvel.com/docs#!/public/getCreatorCollection_get_0
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Marvel 
{
	//Default constructor
	public Marvel() 
	{
		
	}
	
	// Main method of class
	public static String mainMarvel(String input) 
	{
		
		//Variable initialization
		String endpoint = String.format("http://gateway.marvel.com/v1/public/characters?name=%s&ts=1&apikey=&hash=", input);
		
		try
		{
			// Creating URL
			URL url = new URL(endpoint);
			
			// Setting GET request
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			
			// Setting Headers
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");
			
			
			try
			{
				// Acquiring JSON
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while((inputLine = in.readLine()) != null)
				{
					content.append(inputLine);
				}
				in.close();
				
				// Parsing Json to get description of character inputted
				JsonParser parser = new JsonParser();
				JsonObject res2 = parser.parse(content.toString()).getAsJsonObject();
				return ((JsonObject) ((JsonObject) res2.get("data")).getAsJsonArray("results").get(0)).get("description").getAsString();
			}
			catch(IOException e)
			{
				// Handle Error Exception, print out error code
	            int status = connection.getResponseCode();
	            System.out.println(String.format("Something went wrong, STATUS CODE: %d", status));
	            
	            //Close the connection
	            connection.disconnect();
			}
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		
		// Returns an empty string if something went wrong
		return "";
	}
}
