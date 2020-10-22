/* Author: Tyler Mooney
 * Date: October 3rd, 2020
 * Professor: Khan
 * Referenced Libraries:
 * 
 * Purpose: When the program is run, a bot will join the specified text channel
 * at https://webchat.freenode.net/, and respond to specific prompts/messages
 * made by the user. There should be 2 APIs with which the bot can reference
 * 
 * Notes:
 * The base of this code is taken from API and Pircbot.pdf slides
 */

//This is the class that “sets up” your chatbot,
public class ChatBot
{
	public static void main(String[] args) throws Exception
	{
		//
		Bot ChatBot = new Bot();
		
		ChatBot.setVerbose(true);
		ChatBot.connect("irc.freenode.net"); //tells it where to connect to - this is the same as the web interface I linked in the last slide
		ChatBot.joinChannel("#cropBot"); // Name of channel is you want to connect to - in this case it’s called “#testChannel”
			//this is the default message it will send when your pircbot first goes live
		ChatBot.sendMessage("#cropBot", "Hey! Enter any message and I'll respond!");
			//That’s it for setting up you bot! After this, you can implemented custom logic that will look similar to the next slide
	}
}