package org.rb.chatbot.preprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is used to extract stuff from the Jokes Dataset file.
 * @author RB
 *
 */
public class ExtractJokes {

	/**
	 * The delimiter I used in my dataset file.
	 */
	private static final String JOKE_DELIMITER = "-------";
	
	/**
	 * Name of the dataset file
	 */
	private static final String FILE_NAME = "jokesDataSet.txt";
	
	/**
	 * Does what it says. Extracts the jokes and puts 'em in an ArrayList for your usage.
	 * @return ArrayList of Strings. Each element is a joke.
	 * @throws IOException
	 */
	public static ArrayList<String> getJokesList() throws IOException{
		FileReader fileReader = new FileReader(FILE_NAME);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		ArrayList<String> jokesList = new ArrayList<String>();
		String line = "";
		String joke = "";
		while((line=bufferedReader.readLine())!=null){
			if(line.trim().equals(JOKE_DELIMITER)){
				jokesList.add(joke);
				joke = "";
			}
			else{
				joke += " " + line;
			}
		}
		bufferedReader.close();
		fileReader.close();
		return jokesList;
	}

}
