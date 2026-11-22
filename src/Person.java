import java.util.ArrayList;

public class Person
{
    private String name, surname;
    private int age;
    private LeapCard leapCard;
    private double cash;

    public Person(String name, String surname, int age, double cash)
    {
        this.name = name;
        this.surname = surname;
        this.age = age;

        this.leapCard = new LeapCard(this);
        this.cash = cash;
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

    public double getCash()
    {
        return cash;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public void printPersonDetails()
    {
        System.out.printf("  %-10s %s\n", "Name:", name);
        System.out.printf("  %-10s %s\n", "Surname:", surname);
        System.out.printf("  %-10s %d\n", "Age:", age);
    }

    public void changeCash(double amount)
    {
        cash = Utility.roundToTwoPlaces(Math.max(0.0, cash + amount));
    }

    public int travel(Bus bus)
    {
        ArrayList<Stop> stops = Utility.routes.get(leapCard.getRoute());

        double trip_fare = leapCard.calculateFare(stops);
        int trip_distance = leapCard.calculateDistance(stops, leapCard.getStart(), leapCard.getDestination());

        leapCard.setTripFare(trip_fare);
        leapCard.setTripDistance(trip_distance);
        leapCard.spendBalance(trip_fare);

        int minutes = leapCard.calculateTime(trip_distance);
        BusEvent event = bus.getEvent();
        if(bus.eventHappened())
        {
            minutes += event.extraMinutes();
            changeCash(event.cashChange());
        }

        return minutes;
    }
}
