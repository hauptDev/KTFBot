package work.haupt.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.chat.ChatConnector;
import work.haupt.util.BotConfig;

public class StartCommand extends BotCommand {

    private static final String LOGTAG = "STARTCOMMAND";

    public StartCommand() {
        this(Commands.startCommand, "Starts the message feed from the bot.");
    }
    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    private StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if(arguments != null && arguments.length == 3)
        {
            try
            {
                if(!arguments[0].equals("ktfbot2019"))
                {
                    BotLogger.warn(LOGTAG, String.format("%s tried to login with a wrong password: %s", user.getUserName(), arguments[0]));
                }

                BotConfig.USER_ID = Integer.parseInt(arguments[1]);
                BotConfig.USERNAME = arguments[2];
            }
            catch(NumberFormatException exception)
            {
                BotLogger.error(LOGTAG, exception);
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());

        if(BotConfig.USER_ID != user.getId())
            message.setText("Please enter your ID (/id) in the configuration to unlock this command.");
        else
            message.setText("Welcome! You've been approved. I will now try to connect to the chat and show you the latest messages. You can get a command list with /help.");
            Thread th = new Thread(new ChatConnector());
            th.start();
        try
        {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
