import java.util.*;
import java.lang.Math;
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
    private int trip_distance;
    enum Validation {BAD_START, BAD_DESTINATION, BAD_ROUTE, ZERO_STOPS, NOT_ENOUGH_MONEY, VALID}
    
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

    public int getTripDistance()
    {
        return trip_distance;
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

    public void setTripDistance(int trip_distance)
    {
        this.trip_distance = trip_distance;
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
        {
            balance = Utility.roundToTwoPlaces(balance + amount);
        }
        else
        {
            System.err.println("[Error] Amount has to be positive!");
        }
    }

    public void spendBalance(double amount)
    {
        if(amount > 0)
        {
            balance = Utility.roundToTwoPlaces(balance - amount);
        }
        else
        {
            System.err.println("[Error] Amount has to be positive!");
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

    public int calculateDistance(ArrayList<Stop> stops, String start, String destination)
    {
        if(stops == null) 
        {
            System.err.print("[Error] Stops are null\n");
            return -1;
        }
        else if(stops.isEmpty())
        {
            System.err.print("[Error] Route stop list is empty\n");
            return -1;
        }
        
        int distance = 0;
        
        int start_pos = Utility.getStopPosition(stops, start);
        if(start_pos == -1)
        {
            System.err.printf("[Error] Stop %s not found\n", start);
            return -1;
        }

        int destination_pos = Utility.getStopPosition(stops, destination);
        if(destination_pos == -1)
        {
            System.err.printf("[Error] Stop %s not found\n", destination);
            return -1;
        }

        for(int i = Math.min(start_pos, destination_pos); i < Math.max(start_pos, destination_pos); i++)
        {
            distance += stops.get(i).getDistanceToNext();
        }
        return distance;
    }

    public double calculateFare(ArrayList<Stop> stops)
    {
        String type = getType();

        // Variables for fare calculation
        double fare, rate = 0.0;

        int distance = calculateDistance(stops, start, destination);

        // Handling different fare rates according to card type
        switch(type)
        {
            case "Adult" ->
            {
                rate = 0.35;
            }

            case "Child" ->
            {
                rate = 0.10;
            }

            case "Young Adult / Student" ->
            {
                rate = 0.17;
            }
        }

        fare = ((double) distance / 1000) * rate;
        fare = Utility.roundToTwoPlaces(fare);

        return fare;
    }

    public int calculateTime(int distance)
    {
        enum Traffic {HEAVY, MODERATE, LIGHT, NONE};
        Traffic traffic = Traffic.NONE;
        int hour = current_time;
        double time_per_km = 0.0;
        int time_spent = 0; // in minutes

        if(hour >= 0 && hour <= 7)
        {
            traffic = Traffic.NONE;
        }

        else if((hour >= 10 && hour <= 14) || (hour >= 20 && hour <= 23))
        {
            traffic = Traffic.LIGHT;
        }

        else if((hour >= 18 && hour <= 19))
        {
            traffic = Traffic.MODERATE;
        }

        else if((hour >= 8 && hour <= 9) || (hour >= 15 && hour <= 17))
        {
            traffic = Traffic.HEAVY;
        }

        switch(traffic)
        {
            case NONE ->
            {
                time_per_km = 5;
            }

            case LIGHT ->
            {
                time_per_km = 6.5;
            }

            case MODERATE ->
            {
                time_per_km = 8.5;
            }

            case HEAVY ->
            {
                time_per_km = 12;
            }
        }

        time_spent = (int) Math.round(((double) distance / 1000) * time_per_km);
        return time_spent;
    }

    public Validation validateTrip()
    {
        ArrayList<Stop> stops = Utility.routes.get(route);

        if(stops == null)
        {
            return Validation.BAD_ROUTE;
        }

        else if(Utility.getStopPosition(stops, start) == -1)
        {
            return Validation.BAD_START;
        }

        else if(Utility.getStopPosition(stops, destination) == -1)
        {
            return Validation.BAD_DESTINATION;
        }

        int distance = calculateDistance(stops, start, destination);
        if(distance == 0)
        {
            return Validation.ZERO_STOPS;
        }

        else if(Utility.roundToTwoPlaces(balance - calculateFare(stops)) < 0)
        {
            return Validation.NOT_ENOUGH_MONEY;
        }

        return Validation.VALID;
    }
}
