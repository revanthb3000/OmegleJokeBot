package org.rb.chatbot;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.rb.preprocessing.ExtractJokes;

public class MainClass {

	public static void main(String[] args) throws Exception {
		Boolean isOwnerPresent = true;
		letsChat(isOwnerPresent);
	}

	public static void letsChat(Boolean isOwnerPresent) throws Exception {
		ArrayList<String> jokes = ExtractJokes.getJokesList();
		String url = "http://www.omegle.com/";
		WebHandler webHandler = new WebHandler(url);
		webHandler.startBrowser();
		while (true) {
			
			String fileName = "convs/" + getCurrentTimeStamp() + ".txt";
			webHandler.startNewChat();
			webHandler.waitForChatStart();
			
			try{
				webHandler.sendMessage("Hi ! I have a confession to make: I'm a bot, not a spam bot, but a comedy bot. I'll be messaging jokes right now :-). I'll hit one joke for each message you send. TO STOP IT, just type \"stop\" and my human owner will talk if he is available. Okay ? :D ");	
			}
			catch(Exception e){
				System.out.println(e);
				continue;
			}
			
			int numOfJokes = 0;
			while (true) {
				webHandler.waitForNewMessage();
				String newMessage = webHandler.getNewMessage();
				if (newMessage.toLowerCase().contains("stop")) {
					try {
						if (isOwnerPresent) {
							webHandler.sendMessage("Thank you sticking around :) My main aim here is to bring a smile to people's faces. I hope I've done that :) My owner will speak now. Bye :(");
						} else {
							webHandler.sendMessage("Thank you sticking around :) My main aim here is to bring a smile to people's faces. I hope I've done that :) Anyway, my owner is not here right now (the bastard is sleeping !! ) :( and I will have to disconnect in about 120 seconds. Do leave any feedback(or contact info if you want) if you have any.");
							Thread.sleep(120000);
						}
					} catch (Exception e) {
						System.out.println(e);
					}
					break;
				} else if (webHandler.getTranscript().contains("I'm leaving. Bye !")) {
					break;
				} else if (newMessage.contains("Stranger has disconnected")) {
					break;
				}
				try {
					webHandler.sendMessage(jokes.get(getRandomNumber(0,jokes.size() - 1)));
					numOfJokes++;
					if(numOfJokes%20==0){
						webHandler.sendMessage("(Just hit 'stop' to get me to stop talking.)");
					}
				} catch (Exception e) {
					System.out.println(e);
					break;
				}
			}
			
			if (!isOwnerPresent) {
				try {
					if (!webHandler.hasDisconnected()) {
						webHandler.disconnect();
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			else{
				while(true){
					if(webHandler.hasDisconnected()){
						break;
					}
					Thread.sleep(20000);
				}
			}
			
			writeToFile(webHandler.getTranscript(), fileName);
			Thread.sleep(5000);
			
		}
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss");// dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public static void writeToFile(String matter, String fileName)
			throws IOException {
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.write(matter);
		fileWriter.close();
	}

	public static int getRandomNumber(int min, int max) {
		int randomNumber = min + (int) (Math.random() * ((max - min) + 1));
		return randomNumber;
	}

}
