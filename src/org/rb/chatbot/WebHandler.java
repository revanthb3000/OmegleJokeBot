package org.rb.chatbot;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebHandler {
	
	/**
	 * The webdriver that runs the whole thing.
	 */
	private WebDriver driver;
	
	/**
	 * BaseUrl. Will be initialized in constructor
	 */
	private String baseUrl;
	
	/**
	 * This transcript will be used to find out if the human has replied.
	 */
	private String chatTranscript;
	
	/**
	 * What's been hit by the stranger ?
	 */
	private String newMessage;

	/**
	 * Kinda like an alarm that asks the stranger if he's still there.
	 */
	private int thresholdTime = 30000;
	
	public WebHandler(String url){
		this.baseUrl = url;
	}
	
	/**
	 * This function basically starts the browser to be used for the whole code.
	 */
	public void startBrowser(){
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	/**
	 * The END - Stops the browser.
	 */
	public void stopBrowser(){
		driver.quit();
	}
	
	/**
	 * Basically visits your baseUrl link and starts over
	 * @throws InterruptedException
	 */
	public void startNewChat() throws InterruptedException{
		chatTranscript = "";
		newMessage = "";
		driver.get(baseUrl);

		addTopic("jokes");
		addTopic("comedy");
		addTopic("funny");
		addTopic("sad");
		addTopic("help");
		addTopic("laugh");
		
		driver.findElement(By.id("textbtn")).click();
	}
	
	public void addTopic(String topic){
	    driver.findElement(By.cssSelector("input.newtopicinput")).sendKeys(topic + "\n");
	}

	/**
	 * Works only for an active chat. Given a string as an input, this function will send that message in that chat window.
	 * Do your waiting part outside this function.
	 * @param message
	 * @throws InterruptedException
	 */
	public void sendMessage(String message){
		driver.findElement(By.cssSelector("textarea.chatmsg")).sendKeys(message);
		driver.findElement(By.cssSelector("button.sendbtn")).click();
		chatTranscript = getTranscript();
	}
	
	/**
	 * This function tells us if a new message has arrived since the last time it was used.
	 * @return
	 */
	public boolean hasNewMessageArrived(){
		if(chatTranscript.equals(getTranscript())){
			return false;
		}
		newMessage = getTranscript();
		newMessage = newMessage.replace(chatTranscript, "").trim();
		System.out.println(newMessage);
		return true;
	}
	
	/**
	 * Returns the whole chat transcript.
	 * @return 
	 */
	public String getTranscript(){
		String divisionName = "logwrapper";
		WebElement chatTranscript = driver.findElement(By.className(divisionName));
		String transcript = chatTranscript.getText();
		transcript = transcript.replaceAll(ConstantTextStrings.STRANGER_TYPING, "").trim();
		return transcript;
	}
	
	/**
	 * This function spins you in a loop till a new message has been received.
	 * I'll check once every two seconds. Who wants to waste CPU cycles with busy waiting !
	 * @throws InterruptedException 
	 */
	public void waitForNewMessage() throws InterruptedException{
		int waitedTime = 0;
		int count = 0;
		while(!hasNewMessageArrived()){
			Thread.sleep(2000);
			waitedTime += 2000;
			if(waitedTime>=thresholdTime){
				sendMessage("You still there ?");
				waitedTime = 0;
				count++;
			}
			if(count == 3){
				sendMessage("I'm leaving. Bye !");
				break;
			}
		}
	}
	
	public void waitForChatStart() throws InterruptedException{
		while(!getTranscript().contains(ConstantTextStrings.INTRO_MESSAGE)){
			Thread.sleep(2000);
		}
	}
	
	public void disconnect(){
	    driver.findElement(By.cssSelector("button.disconnectbtn")).click();
	    driver.findElement(By.cssSelector("button.disconnectbtn")).click();
	}
	
	public Boolean hasDisconnected(){
		return (getTranscript().contains("Stranger has disconnected"))||(getTranscript().contains("You have disconnected"));
	}

	public String getNewMessage() {
		return newMessage;
	}
	
}
