import java.util.ArrayList;

public class Debug
{
        public static void main(String[] args)
        {
                Person john = new Person("John", "Doe", 30);
                
                john.getLeapCard().printLeapCardDetails();
                john.printPersonDetails();

                john.getLeapCard().setRoute("123123123");
                john.getLeapCard().setStart("5");
                john.getLeapCard().setDestination("-1");
                ArrayList<String> stops = john.getLeapCard().getStopsNames("resources/routes.txt");
                String start = john.getLeapCard().getStart();
                String destination = john.getLeapCard().getDestination();

                int result = john.getLeapCard().countStopsTillDestination(stops, start, destination);
                LeapCard.Validation validation = john.getLeapCard().validateTrip();

                System.out.println("Total stops between " + start + " and " + destination + " = " + result);
                System.out.println(validation.name());

                Bus bus = new Bus();
                double time_spent = john.travel(bus);
                
                System.out.println("Fare for this ride: " + john.getLeapCard().getTripFare());
                System.out.println("New balance: " + john.getLeapCard().getBalance());
                System.out.println("Total time spent: " + time_spent);
        }
}
