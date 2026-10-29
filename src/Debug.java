import java.util.ArrayList;
import java.util.HashMap;

public class Debug
{
    public static void printBusInfo(Bus bus)
    {
        System.out.println("\nPeople on board: " + bus.people_on_board);
        System.out.println("State: " + bus.state + "\n");
    }

    public static void printPersonDetails(Person user)
    {
        System.out.println("Name: " + user.getName());
        System.out.println("Surname: " + user.getSurname());
        System.out.println("Age: " + user.getAge());
    }

    public static void main(String[] args)
    {
        HashMap<String, ArrayList<Stop>> routes = Utility.loadRoutes("resources/routes.txt");
    }
}
