import java.util.*;

public class Menu
{
    // Create objects for future use.
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public Person initialiseUser()
    {
        String name, surname;
        int age;

        // Request data
        System.out.print("Enter your name: ");
        name = scanner.nextLine();
    
        System.out.print("Enter your surname: ");
        surname = scanner.nextLine();

        System.out.print("Enter your age: ");
        age = scanner.nextInt();

        scanner.nextLine();

        System.out.printf("\nWelcome to Dublin, %s, your new Leap card has been activated, good luck!", name);
        return new Person(name, surname, null, null, null, age);
    }

    public void catchBus(LeapCard leap)
    {
        System.out.print("What bus are we thinking?: ");
        String bus_route = scanner.nextLine();
        
        // Get all the information needed.
        ArrayList<String> bus_stops = leap.getStopsNames("resources/routes.txt", bus_route);
        
        System.out.print("Where exactly are we?... : ");
        String start = scanner.nextLine();

        System.out.print("Where are we going?: ");
        String destination = scanner.nextLine();

        // Create a bus coming from nowhere.
        Bus bus = new Bus();
        bus.goodOrBadBus();
    }



    public void menu(Person user, LeapCard leap)
    {
        boolean in_use = true;

        // Greet the user and print their details.
        System.out.println("\n\nYou are standing alone on a feels-like almost desolate Dublin Bus stop on the edge of the city...");
        leap.printDetails();

        while(in_use)
        {
            catchBus(leap);
            in_use = false;
        }
    }

    public static void main(String[] args)
    {
        Menu main_menu = new Menu();
        
        Person user = main_menu.initialiseUser();

        main_menu.menu(user, user.getLeapCard());
        // Initialising details
        /* ArrayList<String>stops = user.getLeapCard().getStopsNames("resources/routes.txt", t_route);
        double fare = user.getLeapCard().calculateFare(stops, user);
        double time = user.getLeapCard().calculateTime(user.getLeapCard().countStopsTillDestination(stops, t_start, t_destination));
        
        user.getLeapCard().printDetails();
        System.out.println("It will take " + time + " minutes to go from " + t_start + " to " + t_destination + ".");
        System.out.println("\nYou will be charged for " + fare + " euro.");
        
        // Catching a bus
        System.out.println("\n\n\nStops: " + stops);
        user.getLeapCard().printStopsEnRoute(stops, stops.indexOf(t_start), stops.indexOf(t_destination)); */
        
        scanner.close();
    }
}
