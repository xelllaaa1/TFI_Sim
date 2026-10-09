import java.util.*;
import java.io.*;
import java.time.*;

public class LeapCard
{
    private String cardNumber;
    private double balance = 0;
    private String type = "Adult";
    private int current_time;
    private Person owner;

    enum Traffic {HEAVY, MODERATE, LIGHT, NONE}
    Traffic traffic;

    private ArrayList<String> history = new ArrayList<String>();

    public LeapCard(Person owner)
    {
        Random random = new Random();
        cardNumber = String.format("%08d", random.nextInt(100000000));
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

    public void setType(String type)
    {
        this.type = type;
    }

    // Useful functions
    public void printDetails()
    {
        System.out.println("\nName: " + owner.getName() + " " + owner.getSurname());
        System.out.println("Current route: " + owner.getRoute());
        System.out.println("Start: " + owner.getStart());
        System.out.println("Destination: " + owner.getDestination());
        System.out.println("\nBalance: " + balance);
        System.out.println("Card Type: " + type);
        System.out.println("Card Number: " + cardNumber);
    }

    public void printStopsEnRoute(ArrayList<String> stops, int start, int destination)
    {
        for(int i = start; i <= destination; i++) System.out.println("Bus Stop #" + (i + 1) + ": " + stops.get(i));
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

    public ArrayList<String> getStopsNames(String path, String route)
    {
        try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String currRoute;
            int start = 0;
            
            while((currRoute = reader.readLine()) != null)
            {
                start = currRoute.indexOf(':');

                if(currRoute.substring(0, start).equals(route))
                {
                    break;
                }
            }

            if(currRoute == null)
            {
                System.out.println("Could not find route " + route);
                return null;
            }

            ArrayList<String> stops = new ArrayList<String>(Arrays.asList(currRoute.substring(start + 1).split(",")));

            for(int i = 0; i < stops.size(); i++)
            {
                stops.set(i, stops.get(i).strip());
            }

            return stops;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public int countStopsTillDestination(ArrayList<String> stops, String start, String destination)
    {
        int from = stops.indexOf(start);
        int to = stops.indexOf(destination);

        if(from == -1 || to == -1 || to < from) return -1;

        return to - from;
    }

    public double calculateFare(ArrayList<String> stops, Person person)
    {
        String start = person.getStart();
        String destination = person.getDestination();
        String type = person.getLeapCard().getType();

        if(start.equals(destination)) return 0.0;
        /* DEBUG System.out.println("\n\n\nStops count > 0\n"); */

        // Variables for fare calculation
        final int short_trip = 5, medium_trip = 20, long_trip = 45;
        double fare = 0.0;
        int count = 0;

        // Count stops
        int i = 0;
        while(!stops.get(i).equals(destination))
        {
            count += 1;
            
            //DEBUG
            System.out.println("\"" + stops.get(i) + "\" = " + "\"" + destination +"\"?");
            System.out.println("Count = " + count);
            System.out.println("Current stop = "+ stops.get(i));
            
            i++;
        }
        System.out.println("DEBUG: Final count = " + count);

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
}
