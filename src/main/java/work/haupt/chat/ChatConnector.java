package work.haupt.chat;

import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.util.BotConfig;
import work.haupt.util.ChatMessage;
import work.haupt.util.MessageContainer;

import java.util.List;
import java.util.logging.Logger;

public class ChatConnector implements Runnable {

    // Declare and initiate our logger to log on different log levels.
    public static final String LOGTAG = "CHATCONNECTOR";
    private static BotConfig config = ConfigFactory.create(BotConfig.class);
    // Declare and initiate a few static strings that we need and don't want to put somewhere in the logic
    private static final String URL = "http://kt-forum.de/chat/";
    private static final String USERNAME = config.USERNAME;
    private static final String USERNAMEFIELD = "userNameField";
    private static final String PASSWORD = config.PASSWORD;
    private static final String PASSWORDFIELD = "passwordField";
    private static final String LOGINBUTTON = "loginButton";
    private static final String LOGOUTBUTTON = "logoutButton";
    private static final String SUBMITBUTTON = "submitButton";
    private static final String CHATLIST = "chatList";
    private static final String INPUTFIELD = "inputField";
    private static final String MESSAGE_XPATH = "//*[starts-with(@id, 'ajaxChat_m_')]";
    private static final String ID = "id";

    // Initiate the webdriver => HtmlUnit is a headless driver that won't display any UI, make sure the parameter
    // is set to true to activate javascript so we can make use of ajax
    private HtmlUnitDriver driver = new HtmlUnitDriver(true);

    // We have to store the message id of the last message received so we don't send old messages again and again
    private long lastMessageId;

    // Declare some variables that we need who are primarily used local but we don't want to declare them every 1000ms
    private List<WebElement> elements;
    private WebElement element;


    @Override
    public void run() {
        login();
        tryFetchLastMessages();
        while(!Thread.interrupted())
        {
            try {
                fetchUserInput();
                tryFetchNewMessages();
                Thread.sleep(500);
            } catch (InterruptedException e) {
                logout();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void logout() {
        try {
            driver.findElementById(LOGOUTBUTTON).click();
        } catch(NoSuchElementException exception)
        {
            BotLogger.error(LOGTAG, exception);
        }
    }

    /**
     * This method tries to open the login page to the chat. If there's a field for the username we have an indicator
     * to be on the right page.
     * Try to fill out the usernamefield and passwordfield and afterwards click the loginbutton to complete the login
     * process.
     * If we can't find any of the webelements we need for our login process, we get an exception that will be logged.
     */
    private void login() {
        driver.get(URL);
        try {
            if (driver.findElementById(USERNAMEFIELD).isDisplayed()) {
                BotLogger.info(LOGTAG,
                        String.format("Try to fill out the login form with user [%s] and password [%s].",
                                USERNAME, PASSWORD.replace("[A-Za-z0-9]*", "*")));
                driver.findElementById(USERNAMEFIELD).sendKeys(USERNAME);
                driver.findElementById(PASSWORDFIELD).sendKeys(PASSWORD);
                driver.findElementById(LOGINBUTTON).click();
            }
        }
        catch(NoSuchElementException exception)
        {
            BotLogger.warning(LOGTAG, exception);
        }
    }

    private void tryFetchLastMessages() {
        element = driver.findElementById(CHATLIST);
        elements = driver.findElements(By.xpath(MESSAGE_XPATH));

        elements.stream()
                .filter((WebElement chatMessage) -> (!chatMessage.getText().contains(USERNAME)))
                .forEach((WebElement chatMessage) ->
                {
                    if(getMessageId(chatMessage) > lastMessageId)
                        lastMessageId = getMessageId(chatMessage);
                    // improve this
                    MessageContainer.chatMessageQueue.offer(new ChatMessage(chatMessage.getText()));
                });
    }

    /**
     * Little helper method to get the message id of the webelement of an chatmessage
     * @param chatMessage
     * @return
     */
    private int getMessageId(WebElement chatMessage)
    {
        return Integer.parseInt(chatMessage.getAttribute(ID).replace("ajaxChat_m_", ""));
    }

    /**
     * This class tries to find the chatlist container were all chat messages are displayed. After that the chat messages
     * gets filtered if they're older than the last message id recognized.
     * Then messages of the user will be removed too.
     * Foreach of the restliche Messages an Event will be thrown that can be catched by ChatEventReceivers
     */
    private void tryFetchNewMessages()
    {
        try {
            element = driver.findElement(By.id("ajaxChat_m_" + (lastMessageId +1)));
            lastMessageId = getMessageId(element);
            if(element.getText().contains(config.USERNAME + ": "))
                return;
            MessageContainer.chatMessageQueue.offer(new ChatMessage(element.getText()));
        } catch(NoSuchElementException exception)
        {
            BotLogger.error(LOGTAG, exception);
        } finally {
            element = null;
        }
    }

    private void fetchUserInput() {
        if(MessageContainer.botMessageQueue.peek() != null)
        {
            driver.findElementById(INPUTFIELD).sendKeys(MessageContainer.botMessageQueue.poll());
            driver.findElementById(SUBMITBUTTON).click();
        }
    }
}
