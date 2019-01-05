package work.haupt.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class IdCommand extends BotCommand {

    public static final String LOGTAG = "HELPCOMMAND";

    public IdCommand() {
        this(Commands.idCommand, "The specified user will be ignored. Enter again to undo the action. Usage: /ignore [user]");
    }
    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    private IdCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments)
    {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(chat.getId());
            message.setText(String.format("Your user ID is: %s", user.getId()));
            absSender.execute(message);
        }
        catch(TelegramApiException exception)
        {
            BotLogger.error(LOGTAG, exception);
        }
    }
}
