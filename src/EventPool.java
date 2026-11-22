import java.util.List;

public class EventPool
{
    private static final int EVENT_CHANCE = 20;
    private static final List<BusEvent> EVENTS = List.of(
        new BusEvent("Two drunk men are fighting over the last beer, the driver has stopped the bus and entered the fight!", 5),
        new BusEvent("There is a traffic accident ahead, the route is diverted!", 10),
        new BusEvent("The shift of the current driver ended, everybody waits for the new driver to take his place.", 5),
        new BusEvent("Someone is asking the driver whether the bus is going to the Blah-blah Street in the current day and age, really?", 2),
        new BusEvent("A disabled person is boarding the bus, everybody waits for the access ramp.", 1)
    );

    public static BusEvent roll()
    {
        if(Utility.random.nextInt(100) < EVENT_CHANCE)
        {
            return EVENTS.get(Utility.random.nextInt(0, EVENTS.size()));
        }
        
        return null;
    }
}
