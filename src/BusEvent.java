public record BusEvent(String description, int extraMinutes, double cashChange)
{
    public BusEvent(String description, int extraMinutes)
    {
        this(description, extraMinutes, 0.0);   // reuse the main constructor, with no cash change
    }
}