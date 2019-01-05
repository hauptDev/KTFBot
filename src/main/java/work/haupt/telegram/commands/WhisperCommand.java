package work.haupt.telegram.commands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.util.ChatMessage;
import work.haupt.util.MessageContainer;

public class WhisperCommand extends BotCommand {

    public static final String LOGTAG = "WHISPERCOMMAND";

    public WhisperCommand() {
        this(Commands.whisperCommand, "Sends a private message to a user. Usage: /msg <User>");
    }
    /**
     * Construct a command
     *
     * @param commandIdentifier the unique identifier of this command (e.g. the command string to
     *                          enter into chat)
     * @param description       the description of this command
     */
    public WhisperCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        if(arguments.length < 1)
        {
            BotLogger.warn(
                    LOGTAG,
                    String.format("The user '%s (%s)' has tried to whisper without specifying another user to whisper to.",
                    user. getUserName(), user.getId()));
            message.setText("You have not specified a user to whisper to. Usage: /msg <User> <Message>");
        }
        else if(arguments.length < 2)
        {
            BotLogger.warn(
                    LOGTAG,
                    String.format("The user '%s (%s)' has tried to whisper to the user '%s' without entering a message.",
                    user.getUserName(), user.getId(), arguments[0]));
            message.setText("You tried to whisper to a user without posting a message. Usage: /msg <User> <Message>");
        }
        else {
            BotLogger.info(
                    LOGTAG,
                    String.format("The user '%s (%s)' tries to whisper the following message to the user '%s': '%s'",
                    user.getUserName(), user.getId(), arguments[0], arguments[1]));
            MessageContainer.botMessageQueue.offer(
                    String.format("%s %s %s", getCommandIdentifier(), arguments[0], arguments[1]));
        }
    }
}
