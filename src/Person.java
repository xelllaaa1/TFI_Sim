import java.util.ArrayList;

public class Person
{
    private String name, surname;
    private int age;
    private LeapCard leapCard;

    public Person(String name, String surname, int age)
    {
        this.name = name;
        this.surname = surname;
        this.age = age;

        this.leapCard = new LeapCard(this);
    }

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public int getAge()
    {
        return age;
    }

    public LeapCard getLeapCard()
    {
        return leapCard;
    }

    public double travel(Bus bus)
    {
        final double TRIP_FAILED = -1.0;

        if(leapCard.validateTrip() != LeapCard.Validation.VALID)
        {
            return TRIP_FAILED;
        }

        ArrayList<Stop> stops = Utility.routes.get(leapCard.getRoute());
        bus.waitForBus();

        double trip_fare = leapCard.calculateFare(stops);

        leapCard.setTripFare(trip_fare);

        leapCard.spendBalance(trip_fare);

        return leapCard.calculateTime(leapCard.countStopsTillDestination(stops, leapCard.getStart(), leapCard.getDestination()));
    }
}
