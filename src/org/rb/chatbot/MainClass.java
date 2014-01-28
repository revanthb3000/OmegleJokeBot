package org.rb.chatbot;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.rb.preprocessing.ExtractJokes;

/**
 * This is the glue that sticks the webhandler functions together.
 * 
 * @author RB
 * 
 */
public class MainClass {

	/**
	 * A starting point for the code. Nothing much in here. Move along
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Boolean isOwnerPresent = true;
		letsChat(isOwnerPresent);
	}

	/**
	 * This is the core function of the project. Starts firefox webdriver, goes
	 * to the website and initiates a chat. Runs in an infinite loop.
	 * 
	 * @param isOwnerPresent
	 * @throws Exception
	 */
	public static void letsChat(Boolean isOwnerPresent) throws Exception {
		ArrayList<String> jokes = ExtractJokes.getJokesList();
		WebHandler webHandler = new WebHandler(ConstantTextStrings.WEBSITE_URL);
		webHandler.startBrowser();
		while (true) {
			try {
				String fileName = "convs/" + getCurrentTimeStamp() + ".txt";
				String newMessage = "";
				int numOfJokes = 0;

				webHandler.startNewChat();
				webHandler.waitForChatStart();
				webHandler.sendMessage(ConstantTextStrings.BOT_WELCOME_MESSAGE);

				//This loop is the whole course of the chat.
				while (true) {					
					webHandler.waitForNewMessage();
					newMessage = webHandler.getNewMessage();
					
					if (newMessage.toLowerCase().contains("stop")) {
						Boolean shouldRestart = false;
						if (isOwnerPresent) {
							webHandler.sendMessage(ConstantTextStrings.BOT_GOODBYE_OWNER_PRESENT);
						} else {
							webHandler.sendMessage(ConstantTextStrings.BOT_GOODBYE_OWNER_NOT_PRESENT);
							webHandler.sendMessage(ConstantTextStrings.BOT_TECH_STUFF);
							webHandler.sendMessage(ConstantTextStrings.BOT_RESTART_INSTRUCTIONS);
							String chatTranscript = webHandler.getTranscript();
							int cnt = 0;
							while (true) {
								String newMessages = webHandler.getTranscript().replace(chatTranscript, "").trim();
								if(newMessages.toLowerCase().contains("restart")){
									shouldRestart = true;
									break;
								}
								cnt += 5000;
								Thread.sleep(5000);
								if ((cnt == 240000) || (webHandler.hasDisconnected())) {
									break;
								}
							}
						}
						if(!shouldRestart){
							break;
						}
					} else if ((webHandler.getTranscript().contains("I'm leaving. Bye !") || webHandler.hasDisconnected())) {
						break;
					}
					
					if (jokes.size() == 0) {
						webHandler.sendMessage(ConstantTextStrings.BOT_JOKES_EXHAUSTED);
					}
					int jokeId = getRandomNumber(0, jokes.size() - 1);
					webHandler.sendMessage(jokes.get(jokeId));
					jokes.remove(jokeId);
					numOfJokes++;
					if (numOfJokes % 20 == 0)
						webHandler.sendMessage(ConstantTextStrings.BOT_STOP_REMINDER);
				}

				if (!isOwnerPresent) {
					if (!webHandler.hasDisconnected())
						webHandler.disconnect();
				} else {
					while (!webHandler.hasDisconnected())
						Thread.sleep(20000);
				}
				
				writeToFile(webHandler.getTranscript(), fileName);
				Thread.sleep(5000);
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}
	}
	
	/**
	 * Simple Utility Function that returns current date and time in yyyy-MM-dd-HH.mm.ss format.
	 * Useful in naming files since its really unique.
	 * @return Current TimeStamp as a string.
	 */
	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	/**
	 * Feed it some stuff and a filename and it'll do the job.
	 * @param matter Stuff to be written to the file
	 * @param fileName Name of the file
	 * @throws IOException
	 */
	public static void writeToFile(String matter, String fileName) throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.write(matter);
		fileWriter.close();
	}

	/**
	 * Get a random number that lies between min and max.
	 * @param min Minimum value of the output.
	 * @param max Maximum value of the output.
	 * @return
	 */
	public static int getRandomNumber(int min, int max) {
		int randomNumber = min + (int) (Math.random() * ((max - min) + 1));
		return randomNumber;
	}

}
