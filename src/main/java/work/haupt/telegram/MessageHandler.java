package work.haupt.telegram;

import org.aeonbits.owner.ConfigFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.util.BotConfig;
import work.haupt.util.MessageContainer;

public class MessageHandler {

    private static BotConfig config = ConfigFactory.create(BotConfig.class);
    public static final String LOGTAG = "MESSAGEHANDLER";

    public void processMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            String username = update.getMessage().getFrom().getUserName();
            int user_id = update.getMessage().getFrom().getId();
            String message_text = update.getMessage().getText();

            if(config.USER_ID != user_id || config.USER_ID == 0)
            {
                BotLogger.warn(LOGTAG, String.format(":: INVALID PERMISSION :: User %s (%s) wrote: %s.", username, user_id, message_text));
                return;
            }

            MessageContainer.botMessageQueue.offer(message_text);
        }
    }
}
