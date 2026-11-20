import java.util.List;

public class Bus
{
    final static int EVENT_CHANCE = 20;
    Integer eventHappenedIndex = null;
    private static final List<BusEvent> EVENTS = List.of(
        new BusEvent("Two drunk men are fighting over the last beer, the driver has stopped the bus and entered the fight!", 5),
        new BusEvent("There is a traffic accident ahead, the route is diverted!", 10),
        new BusEvent("The shift of the current driver ended, everybody waits for the new driver to take his place.", 5),
        new BusEvent("Someone is asking the driver whether the bus is going to the Blah-blah Street in the current day and age, really?", 2),
        new BusEvent("A disabled person is boarding the bus, everybody waits for the access ramp.", 1)
    );

    enum State {FULL, NFULL, NIS};
    State state;
    int people_on_board;

    public Bus()
    {
        resetBus();
        randomEvent();
    }

    public State getState()
    {
        return state;
    }

    public BusEvent getEvent()
    {
        
        return EVENTS.get(eventHappenedIndex);
    }

    public boolean eventHappened()
    {
        return eventHappenedIndex != null;
    }

    public void randomEvent()
    {
        if(Utility.random.nextInt(100) < EVENT_CHANCE)
        {
            // id corresponds to index of an event from the pool list, like 2 for shifts ending
            eventHappenedIndex = Utility.random.nextInt(0, EVENTS.size());
        }
    }

    public static void printBusInfo(Bus bus)
    {
        System.out.println("\nPeople on board: " + bus.people_on_board);
        System.out.println("State: " + bus.state + "\n");
    }

    public void resetBus()
    {
        int service = Utility.random.nextInt(0,8);
        people_on_board = Utility.random.nextInt(0, 96);

        // Determine bus service and fullness.
        if(service == 1)
        {
            state = State.NIS;
            people_on_board = 0;
        }

        else if(people_on_board < 90)
        {
            state = State.NFULL;
        }

        else state = State.FULL;
    }

    public void waitForBus()
    {
        Bus.State bus_state = getState();

        // Handle bus state
        if(bus_state == Bus.State.NFULL)
        {
            System.out.println("\nThe bus is here, finally!");
            return;
        }

        while(bus_state != Bus.State.NFULL)
        {
            bus_state = getState();

            if(bus_state == State.FULL)
            {
                System.out.println("\nDarn it! This bus is full!");
            }
            else if(bus_state == Bus.State.NIS)
            {
                System.out.println("\nReally?! Not in service?! God dang it...");
            }
            else
            {
                System.out.println("\nThank god, its here!");
                break;
            }
            
            resetBus();
            
            System.out.println("\nBetter luck with your next bus.. Here's Dublin Bus for ya!");
            System.out.println("\n...");
            try{Thread.sleep(3000);} catch(InterruptedException e ) {e.printStackTrace();}
        }

        return;
    }
}
