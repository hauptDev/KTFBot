package work.haupt.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;

public class UsersCommand extends BotCommand {

    public static final String LOGTAG = "USERSCOMMAND";

    public UsersCommand() {
        this(Commands.userListCommand, "Displays a list of chat users.");
    }

    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    private UsersCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    /**
     * @param absSender
     * @param user
     * @param chat
     * @param arguments
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        BotLogger.info(LOGTAG, String.format("%s (%s) requests the user list of the chat.", user.getUserName(), user.getId()));
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText("This command is currently disabled.");
        try
        {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
