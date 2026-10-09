import java.util.*;

public class Menu
{
    public static void main(String[] args)
    {
        String t_name, t_surname, t_route, t_start, t_destination;
        int t_age;
        Scanner scanner = new Scanner(System.in);

        // Request data
        System.out.println("Enter your name: ");
        t_name = scanner.nextLine();
    
        System.out.println("Enter your surname: ");
        t_surname = scanner.nextLine();

        System.out.println("Enter your age: ");
        t_age = scanner.nextInt();

        scanner.nextLine();
    
        System.out.println("Enter route: ");
        t_route = scanner.nextLine();
        
        System.out.println("Enter the starting point of your journey: ");
        t_start = scanner.nextLine();
        
        System.out.println("Enter your final destination: ");
        t_destination = scanner.nextLine();
        
        Person activePerson = new Person(t_name, t_surname, t_route, t_start, t_destination, t_age);
        activePerson.getDetails();

        // Initialising details
        ArrayList<String>stops = activePerson.getLeapCard().getStopsNames("resources/routes.txt", t_route);
        double fare = activePerson.getLeapCard().calculateFare(stops, activePerson);
        double time = activePerson.getLeapCard().calculateTime(activePerson.getLeapCard().countStopsTillDestination(stops, t_start, t_destination));
        
        activePerson.getLeapCard().printDetails();
        System.out.println("It will take " + time + " minutes to go from " + t_start + " to " + t_destination + ".");
        System.out.println("\nYou will be charged for " + fare + " euro.");
        
        // Catching a bus
        System.out.println("\n\n\nStops: " + stops);
        activePerson.getLeapCard().printStopsEnRoute(stops, stops.indexOf(t_start), stops.indexOf(t_destination));
        
        scanner.close();
    }
}
