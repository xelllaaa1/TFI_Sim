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

        // Catching a bus
        System.out.println("\n\n\nStops: " + activePerson.getLeapCard().getStops("resources/routes.txt", t_destination, t_route));
        double result = activePerson.getLeapCard().calculateFare(activePerson.getLeapCard().getStops("resources/routes.txt", t_destination, t_route), activePerson);
        System.out.println(result);
        scanner.close();
    }
}
