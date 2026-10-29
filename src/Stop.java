public class Stop
{
    private String name;
    private int distanceToNext;
    
    public Stop(String name, int distanceToNext)
    {
        this.name = name;
        this.distanceToNext = distanceToNext;
    }

    public String getName()
    {
        return name;
    }

    public int getDistanceToNext()
    {
        return distanceToNext;
    }

    @Override
    public String toString()
    {
        return name + " " + "(" + distanceToNext + "m)";
    }
}
