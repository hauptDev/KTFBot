package work.haupt;

import org.aeonbits.owner.ConfigFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.telegram.CommandsHandler;
import work.haupt.util.BotConfig;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

public class Startup {

    private static final String LOGTAG = "MAIN";

    private static BotConfig config = ConfigFactory.create(BotConfig.class);
    public static TelegramBotsApi telegramBotsApi;

    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
        BotLogger.setLevel(Level.ALL);
        BotLogger.registerLogger(new ConsoleHandler());

        try {
            ApiContextInitializer.init();
            telegramBotsApi = new TelegramBotsApi();
            try {
                telegramBotsApi.registerBot(new CommandsHandler(config.COMMANDS_USER));
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
