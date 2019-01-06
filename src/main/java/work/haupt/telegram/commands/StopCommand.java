package work.haupt.telegram.commands;

import org.aeonbits.owner.ConfigFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.util.BotConfig;

public class StopCommand extends BotCommand {

    private static final String LOGTAG = "STOPCOMMAND";
    private static BotConfig config = ConfigFactory.create(BotConfig.class);

    public StopCommand() {
        this(Commands.stopCommand, "Stops the message feed from the bot.");
    }
    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    private StopCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        if(config.USER_ID != user.getId())
            message.setText("Please enter your ID (/id) in the configuration to unlock this command.");
        else {
            config.USER_ID = 0;
            message.setText("Bye. Bye. I removed your ID from the configuration. Add it again so you can use the commands.");
        }
        try
        {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
