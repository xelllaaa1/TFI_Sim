import java.util.ArrayList;
import java.time.LocalTime;

public class LeapCard
{
    private String cardNumber;
    private double balance = 10;
    private String type = "Adult";
    private int current_time;
    private Person owner;

    private String start, destination, route;
    private double trip_fare;

    enum Traffic {HEAVY, MODERATE, LIGHT, NONE}
    enum Validation {BAD_START, BAD_DESTINATION, BAD_ROUTE, BACKWARD_DIRECTION, ZERO_STOPS, VALID}

    Traffic traffic;

    private ArrayList<String> history = new ArrayList<String>();

    public LeapCard(Person owner)
    {
        cardNumber = String.format("%08d", Utility.random.nextInt(100000000));
        history.add("Card #" + cardNumber + " activated.");
        
        this.owner = owner;
        
        if(owner.getAge() < 16) type = "Child";
        else if(owner.getAge() <= 25) type = "Young Adult / Student";

        current_time = LocalTime.now().getHour();
    }

    // Getters and setters for instance variables
    public String getCardNumber()
    {
        return cardNumber;
    }

    public double getBalance()
    {
        return balance;
    }
    
    public String getType()
    {
        return type;
    }

    public ArrayList<String> getHistory()
    {
        return history;
    }

    public int getTime()
    {
        return current_time;
    }

    public Traffic getTraffic()
    {
        return traffic;
    }

    public String getStart()
    {
        return start;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getRoute()
    {
        return route;
    }

    public double getTripFare()
    {
        return trip_fare;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setStart(String start)
    {
        this.start = start;
    }

    public void setDestination(String destination)
    {
        this.destination = destination;
    }

    public void setRoute(String route)
    {
        this.route = route;
    }

    public void setTripFare(double trip_fare)
    {
        this.trip_fare = trip_fare;
    }

    // Useful functions
    public void printLeapCardDetails()
    {
        System.out.println("Name: " + owner.getName() + " " + owner.getSurname());
        System.out.println("Balance: " + balance);
        System.out.println("Card Type: " + type);
        System.out.println("Card Number: " + cardNumber + "\n");
    }

    public void addBalance(double amount)
    {
        if(amount > 0)
            balance += amount;

        else
        {
            System.out.println("Amount has to be positive.");
            balance += 0;
        }
    }

    public void spendBalance(double amount)
    {
        if(amount > 0)
            balance -= amount;

        else
        {
            System.out.println("Amount has to be positive.");
        }
    }

    public void printHistory()
    {
        System.out.println("\nCard History:");
        for(int i = 0; i < history.size(); i++)
        {
            System.out.println(history.get(i));
        }
    }

    public int countStopsTillDestination(ArrayList<Stop> stops, String start, String destination)
    {
        if(stops == null) return -1;
        if(stops.isEmpty()) return -1;

        int from = Utility.getStopPosition(stops, start);
        int to = Utility.getStopPosition(stops, destination);

        if(from == -1 || to == -1 || to < from) return -1;

        return to - from;
    }

    public double calculateFare(ArrayList<Stop> stops)
    {
        String start = getStart();
        String destination = getDestination();
        String type = getType();

        if(start.equals(destination)) return 0.0;
        /* DEBUG System.out.println("\n\n\nStops count > 0\n"); */

        // Variables for fare calculation
        final int short_trip = 5, medium_trip = 20, long_trip = 45;
        double fare = 0.0;

        // count = stops travelled
        int count = countStopsTillDestination(stops, start, destination);

        // Handling different fare rates according to card type
        switch(type)
        {
            case "Adult" -> {
                if(count <= short_trip) fare = 1.0;
                else if(count <= medium_trip) fare = 1.5;
                else if(count <= long_trip) fare = 2.5;
                else fare = 4.15;
            }

            case "Young Adult / Student" -> {
                if(count <= short_trip) fare = 0.75;
                else if(count <= medium_trip) fare = 1.0;
                else if(count <= long_trip) fare = 1.7;
                else fare = 3.0;
            }

            case "Child" -> {
                if(count <= short_trip) fare = 0.5;
                else if(count <= medium_trip) fare = 0.8;
                else if(count <= long_trip) fare = 1.5;
                else fare = 2.3;
            }

            default -> {return 2.5;}
        }

        /*Debug System.out.println("DEBUG: Fare = " + fare); */
        return fare;
    }

    public double calculateTime(int stop_count)
    {
        int hour = getTime();
        double time_per_stop = 1.0;

        if(hour >= 0 && hour <= 7)
            this.traffic = Traffic.NONE;
        else if((hour >= 7 && hour <= 10) || (hour >= 16 && hour <= 19))
            this.traffic = Traffic.HEAVY;
        else if((hour >= 10 && hour <= 11) || (hour >= 14 && hour <= 16) || (hour >= 19 && hour <= 21))
            this.traffic = Traffic.MODERATE;
        else if((hour >= 11 && hour <= 14) || (hour >= 21 && hour <= 23)) this.traffic = Traffic.LIGHT;

        switch(this.getTraffic())
        {
            case NONE ->
            {
                time_per_stop = 0.25;
            }

            case LIGHT ->
            {
                time_per_stop = 0.5;
            }

            case MODERATE ->
            {
                time_per_stop = 1.25;
            }

            case HEAVY ->
            {
                time_per_stop = 2.5;
            }
        }

        return time_per_stop * stop_count;
    }

    public Validation validateTrip()
    {
        ArrayList<Stop> stops = Utility.routes.get(route);

        if(stops == null)
            return Validation.BAD_ROUTE;

        else if(Utility.getStopPosition(stops, start) == -1)
            return Validation.BAD_START;

        else if(Utility.getStopPosition(stops, destination) == -1)
            return Validation.BAD_DESTINATION;

        int stopCount = countStopsTillDestination(stops, start, destination);

        if(stopCount == -1) return Validation.BACKWARD_DIRECTION;
        else if(stopCount == 0) return Validation.ZERO_STOPS;

        return Validation.VALID;
    }
}
