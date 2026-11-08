import java.time.LocalDateTime;

public class HistoryEntry
{
    private final LocalDateTime timeStamp;
    private final String description;

    public HistoryEntry(String description)
    {
        timeStamp = LocalDateTime.now();
        this.description = description;
    }

    public LocalDateTime getTimeStamp()
    {
        return timeStamp;
    }

    public String getDescription()
    {
        return description;
    }
}
