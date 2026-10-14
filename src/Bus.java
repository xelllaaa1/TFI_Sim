public class Bus
{
    enum State {FULL, NFULL, NIS};
    State state;
    int people_on_board;

    public Bus()
    {
        int service = Menu.random.nextInt(0,8);
        people_on_board = Menu.random.nextInt(0, 96);

        // Determine bus service and fullness.
        if(service == 1) state = State.NIS;

        else
        {
            if(people_on_board < 40) state = State.NFULL;
            else state = State.FULL;
        }
    }

    public void resetBus()
    {
        int service = Menu.random.nextInt(0,8);
        people_on_board = Menu.random.nextInt(0, 96);

        // Determine bus service and fullness.
        if(service == 1) state = State.NIS;

        else
        {
            if(people_on_board < 30 || people_on_board > 60) state = State.NFULL;
            else state = State.FULL;
        }
    }

    public void printBusInfo()
    {
        System.out.println("\nPeople on board: " + people_on_board);
        System.out.println("State: " + state + "\n");
    }

    public void goodOrBadBus()
    {
        Bus.State bus_state = getState();

        // Handle bus state
        if(bus_state == Bus.State.NFULL)
        {
            System.out.println("This bus is ok.");
            return;
        }

        while(bus_state != Bus.State.NFULL)
        {
            printBusInfo();
            bus_state = getState();

            if(bus_state == State.FULL)
            {
                System.out.println("Darn it! This bus is full!");
            }
            else if(bus_state == Bus.State.NIS)
            {
                System.out.println("Really?! Not in service?! God dang it...");
            }
            else
            {
                System.out.println("Thank god!");
                break;
            }
            
            resetBus();
            
            System.out.println("Better luck with your next bus.. Here's Dublin Bus for ya!");
            try{Thread.sleep(3000);} catch(InterruptedException e ) {e.printStackTrace();}
        }

        return;
    }

    public State getState()
    {
        return state;
    }
}
