import java.util.*;

public class Menu
{
    public static void main(String[] args)
    {
        String t_name, t_surname, t_route, t_start, t_destination;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Running...");
        System.out.println("Enter your name: ");
        t_name = scanner.nextLine();
    
        System.out.println("Enter your surname: ");
        t_surname = scanner.nextLine();
    
        System.out.println("Enter route: ");
        t_route = scanner.nextLine();

        System.out.println("Enter the starting point of your journey: ");
        t_start = scanner.nextLine();

        System.out.println("Enter your final destination: ");
        t_destination = scanner.nextLine();

        Person activePerson = new Person(t_name, t_surname, t_route, t_start, t_destination);
        activePerson.getLeapCard().printDetails();

        scanner.close();
    }
}
