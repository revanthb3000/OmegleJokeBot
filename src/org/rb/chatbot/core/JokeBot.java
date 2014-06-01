package org.rb.chatbot.core;

import java.util.ArrayList;

import org.rb.chatbot.preprocessing.ExtractJokes;

public class JokeBot {

	/**
	 * This is the core function of the project. Starts firefox webdriver, goes
	 * to the website and initiates a chat. Runs in an infinite loop.
	 * 
	 * @param isOwnerPresent
	 * @throws Exception
	 */
	public static void startJokeBotChat(Boolean isOwnerPresent, ArrayList<String> topics) throws Exception {
		ArrayList<String> jokesDataset = ExtractJokes.getJokesList();
		WebHandler webHandler = new WebHandler(ConstantTextStrings.WEBSITE_URL);
		webHandler.startBrowser();
		while (true) {
			String fileName = "convs/" + UtilityFunctions.getCurrentTimeStamp() + ".txt";
			String newMessage = "";
			int numOfJokes = 0;
			ArrayList<String> jokes = new ArrayList<String>(jokesDataset);

			webHandler.startNewChat(topics);
			webHandler.waitForChatStart();

			try {
				webHandler.sendMessage(ConstantTextStrings.BOT_WELCOME_MESSAGE);

				// This loop is the course of the whole chat.
				while (true) {
					webHandler.waitForNewMessage();
					newMessage = webHandler.getNewMessage();

					if (newMessage.toLowerCase().contains("stop")) {
						Boolean shouldRestart = stopJokeBot(webHandler, isOwnerPresent);
						if (!shouldRestart){
							break;
						}
					} else if ((webHandler.getTranscript().contains(ConstantTextStrings.BOT_WAITED_TOO_LONG) || webHandler.hasDisconnected())) {
						break;
					}

					webHandler.sendMessage(getRandomJoke(jokes));
					numOfJokes++;
					if (numOfJokes % 20 == 0)
						webHandler.sendMessage(ConstantTextStrings.BOT_STOP_REMINDER);
				}

				if (!webHandler.hasDisconnected())
					webHandler.disconnect();

				UtilityFunctions.writeToFile(webHandler.getTranscript(),fileName);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * This basically handles the situation when the user tells the bot to stop.
	 * 
	 * @param webHandler
	 * @param isOwnerPresent
	 * @return
	 * @throws InterruptedException
	 */
	public static Boolean stopJokeBot(WebHandler webHandler,
			Boolean isOwnerPresent) throws InterruptedException {
		Boolean shouldRestart = false;
		String chatTranscript = "";
		if (isOwnerPresent) {
			chatTranscript = webHandler.getTranscript();
			UtilityFunctions.playSound();
			webHandler.sendMessage(ConstantTextStrings.BOT_GOODBYE_OWNER_PRESENT);
			String newMessages = "";
			
			//Chat ends here. The owner can now speak and new messages are recorded.
			while (!webHandler.hasDisconnected()) {
				newMessages = webHandler.getTranscript().replace(chatTranscript, "").trim();
				if (newMessages.toLowerCase().contains("restart")) {
					shouldRestart = true;
					break;
				}
				Thread.sleep(20000);
			}
		} else /*Owner is not preseent. */ {
			webHandler.sendMessage(ConstantTextStrings.BOT_GOODBYE_OWNER_NOT_PRESENT);
			webHandler.sendMessage(ConstantTextStrings.BOT_TWITTER);
			webHandler.sendMessage(ConstantTextStrings.BOT_OTHER_SOCIAL_MEDIA);
			webHandler.sendMessage(ConstantTextStrings.BOT_RESTART_INSTRUCTIONS);
			chatTranscript = webHandler.getTranscript();
			int cnt = 0;
			while (true) {
				String newMessages = webHandler.getTranscript().replace(chatTranscript, "").trim();
				if (newMessages.toLowerCase().contains("restart")) {
					shouldRestart = true;
					break;
				}
				cnt += 5000;
				Thread.sleep(5000);
				if ((cnt >= 240000) || (webHandler.hasDisconnected())) {
					shouldRestart = false;
					break;
				}
			}
		}
		return shouldRestart;
	}

	/**
	 * Picks up a random joke from the jokes ArrayList, shoots it and removes it
	 * from the list to ensure that it's not repeated again.
	 * 
	 * @param jokes
	 * @return
	 */
	public static String getRandomJoke(ArrayList<String> jokes) {
		if (jokes.size() == 0) {
			return ConstantTextStrings.BOT_JOKES_EXHAUSTED;
		} else {
			int jokeId = UtilityFunctions.getRandomNumber(0, jokes.size() - 1);
			String joke = jokes.get(jokeId);
			jokes.remove(jokeId);
			return joke;
		}
	}

}
