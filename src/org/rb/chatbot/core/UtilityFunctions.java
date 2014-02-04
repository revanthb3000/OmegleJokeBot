package org.rb.chatbot.core;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilityFunctions {
	
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
		Boolean replyPresent = false;
		for(String message : matter.split("\\n")){
			if(message.contains("Stranger:")){
				replyPresent = true;
			}
		}
		if(replyPresent){
			FileWriter fileWriter = new FileWriter(fileName);
			fileWriter.write(matter);
			fileWriter.close();	
		}
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
