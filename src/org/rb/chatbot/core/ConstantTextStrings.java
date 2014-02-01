package org.rb.chatbot.core;

/**
 * This class basically holds constant strings that we'll use.
 * @author RB
 *
 */
public class ConstantTextStrings {
	
	/**
	 * The Stranger is typing message !
	 */
	public static final String STRANGER_TYPING = "Stranger is typing...";
	
	/**
	 * The Say Hi ! Message
	 */
	public static final String INTRO_MESSAGE = "You're now chatting with a random stranger";

	/**
	 * The URL
	 */
	public static final String WEBSITE_URL = "http://www.omegle.com/";
		
	/**
	 * The first thing the bot says once the chat starts.
	 */
	public static final String BOT_WELCOME_MESSAGE = "Hi ! I have a confession to make: I'm a bot, not a spam bot, but a comedy bot. I'll be messaging jokes right now :-). I'll hit one joke for each message you send. TO STOP IT, just type \"stop\" and my human owner will talk if he is available. Okay ? :D ";
	
	/**
	 * The message the bot hits when the human doesn't reply for a long time.
	 */
	public static final String BOT_WAITED_TOO_LONG = "I'm leaving. Bye !";

	/**
	 * The goodbye message when I'm around and spying.
	 */
	public static final String BOT_GOODBYE_OWNER_PRESENT = "Thank you sticking around :) My main aim here is to bring a smile to people's faces. I hope I've done that :) My owner will speak now. Bye :(";
	
	/**
	 * The goodbye message when I'm not around.
	 */
	public static final String BOT_GOODBYE_OWNER_NOT_PRESENT = "Thank you sticking around :) My main aim here is to bring a smile to people's faces. I hope I've done that :) Anyway, my owner is not here right now (the bastard is sleeping !! ) :( and I will have to disconnect in about 240 seconds. Do leave any feedback(or contact info if you want) if you have any. You can also follow my owner(cheap publicity if you ask me) on twitter - @revanthb3000";
	
	/**
	 * Technical Info about the bot !
	 */
	public static final String BOT_TECH_STUFF = "(Technical Info: The bot was written in Java and Selenium was used to control the web browser. Source code's on GitHub)";
	
	/**
	 * Tells the user how to restart the bot.
	 */
	public static final String BOT_RESTART_INSTRUCTIONS = "If you want to restart this bot, just hit 'Restart' and I'll continue.";
	
	/**
	 * Just a reminder to tell the user that he can stop the bot.
	 */
	public static final String BOT_STOP_REMINDER = "(Just hit 'stop' to get me to stop talking.)";
	
	/**
	 * The rare case. All jokes have been exhausted.
	 */
	public static final String BOT_JOKES_EXHAUSTED = "All jokes exhausted ! Well done, human !! Hit \"stop\" to get me to stop.";
}
