/* Author: Tyler Mooney
 * Date: October 3rd, 2020
 * Professor: Khan
 * Referenced Libraries: pircbot.jar
 * 
 * Purpose: When this bot is created, it will read in the lines a user
 * inputs into the chat, and respond accordingly
 * 
 * Notes:
 * The base of this code is taken from API and Pircbot.pdf slides
 * TODO Add some more basic responses, so it seems like the bot is actually talking to you
 * TODO Remove the part where the bot repeats the input for the weather command
 */

import java.io.IOException;

import org.jibble.pircbot.PircBot;

//This class is the main logic of your pircbot, this is where you will implement any functionality
class Bot extends PircBot
{
	// Constructor
	public Bot()
	{
		this.setName("cropBot"); //this is the name the bot will use to join the IRC server
	}
	
	//every time a message is sent, this method will be called and this information will be passed on
	//this is how you read a message from the channel
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		//converting the message to lower case in order to make checks non case sensitive
		String input = message.toLowerCase();
		
		//The conditionals below check for any keywords, then responds accordingly
		
		// Invokes the Weather api, when the keyword is said and returns the temperature
		if (input.contains("weather")) 
		{
			//Variable initialization
			double [] array = new double[3];
			String tempString = input.substring(8, input.length());
			sendMessage(channel, tempString);
			
			try 
			{
				array = Weather.mainWeather(tempString);
				if(array[0] == -1 && array[1] == -1 && array[2] == -1)
					sendMessage(channel, "Error!!!");
				else
					sendMessage(channel, "The weather's going to be " + array[0]
							+ " with a high of " + array[1]
							+ " and a low of " + array[2] + '.');
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		// Invokes the Marvel api, when the keyword is said returns the description for the character inputted
		if (input.contains("marvel")) 
		{
			//this is how you send a message back to the channel
			String tempString = input.substring(7, input.length());
			String description = Marvel.mainMarvel(tempString);
			
			sendMessage(channel, "The description for " + tempString + " is " + '"' + description + '"');
		}

		// A simple greeting response
		if (input.contains("hi") || input.contains("hello"))
		{
			//this is how you send a message back to the channel
			sendMessage(channel, "Hey " + sender + "!");
		}
		
		// Message that tells the ChatBot to leave
		if(input.contains("goodbye"))
		{
			sendMessage(channel, "Goodbye " + sender + "!");
			try
			{
				wait();
			}
			catch(Exception e){
			}
			disconnect();
			dispose();
		}
	}
}