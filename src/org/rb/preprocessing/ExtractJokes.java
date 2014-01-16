package org.rb.preprocessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ExtractJokes {

	private static String JOKE_DELIMITER = "-------";
	
	public static ArrayList<String> getJokesList() throws IOException{
		FileReader fileReader = new FileReader("jokesDataSet.txt");
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
