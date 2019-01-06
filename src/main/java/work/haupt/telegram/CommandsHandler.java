package work.haupt.telegram;

import org.aeonbits.owner.ConfigFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.logging.BotLogger;
import work.haupt.telegram.commands.*;
import work.haupt.util.BotConfig;
import work.haupt.util.MessageContainer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandsHandler extends TelegramLongPollingCommandBot {

    public static final String LOGTAG = "COMMANDSHANDLER";

    private static BotConfig config = ConfigFactory.create(BotConfig.class);
    private MessageHandler messageHandler = new MessageHandler();

    public CommandsHandler(String botUsername)
    {
        super(botUsername);
        register(new HelpCommand());
        register(new ActionCommand());
        register(new WhisperCommand());
        register(new StartCommand());
        register(new StopCommand());
        register(new UsersCommand());
        register(new IgnoreCommand());
        register(new WhisperActionCommand());
        register(new ChannelCommand());
        register(new IdCommand());

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText() + "' is not known by this bot. Type /help for a list of commands.");
            try {
                absSender.execute(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
        });

        startMessageProcessingTimer();
    }

    public void startMessageProcessingTimer()
    {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            try {
                if (MessageContainer.chatMessageQueue.peek() != null) {
                    SendMessage message = new SendMessage();
                    message.setChatId((long) config.USER_ID);
                    message.setText(MessageContainer.chatMessageQueue.poll().getMessage());
                    execute(message);
                }
            } catch (TelegramApiException exception) {
                BotLogger.error(LOGTAG, exception);
            }
        }, 2000, 300, TimeUnit.MILLISECONDS);
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        messageHandler.processMessage(update);
    }

    @Override
    public String getBotToken() {
        return config.COMMANDS_TOKEN;
    }
}
