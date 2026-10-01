public class Person
{
    private String name, surname, current_route, start, destination;
    private LeapCard leapCard;

    public Person(String name, String surname, String current_route, String start, String destination)
    {
        this.name = name;
        this.surname = surname;
        this.current_route = current_route;
        this.start = start;
        this.destination = destination;

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

    public String getRoute()
    {
        return current_route;
    }
    
    public String getStart()
    {
        return start;
    }

    public String getDestination()
    {
        return destination;
    }


    public LeapCard getLeapCard()
    {
        return leapCard;
    }

    public void setRoute(String route)
    {
        current_route = route;
    }
}
