package work.haupt.util;

public class ChatMessage {

    private String time;
    private String username;
    private String message;

    public ChatMessage(String time, String username, String message)
    {
        this.time = time;
        this.username = username;
        this.message = message;
    }

    public ChatMessage(String text) {
        this.message = text;
    }

    public ChatMessage() {

    }

    public String getTime()
    {
        return time;
    }

    public String getUsername()
    {
        return username;
    }

    public String getMessage()
    {
        return message;
    }
}
