public class Bus
{
    enum State {FULL, NFULL, NIS};
    State state;
    int people_on_board;

    public Bus()
    {
        resetBus();
    }

    public State getState()
    {
        return state;
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
                System.out.println("\nThank god!");
                break;
            }
            
            resetBus();
            
            System.out.println("\nBetter luck with your next bus.. Here's Dublin Bus for ya!");
            try{Thread.sleep(3000);} catch(InterruptedException e ) {e.printStackTrace();}
        }

        return;
    }
}
