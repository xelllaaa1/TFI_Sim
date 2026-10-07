public class Person
{
    private String name, surname, current_route, start, destination;
    private int age;
    private LeapCard leapCard;

    public Person(String name, String surname, String current_route, String start, String destination, int age)
    {
        this.name = name;
        this.surname = surname;
        this.age = age;
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

    public int getAge()
    {
        return age;
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

    public void getDetails()
    {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Age: " + age);
        System.out.println("Route: " + current_route);
        System.out.println("Start: " + start);
        System.out.println("Destination: " + destination);
    }
}
