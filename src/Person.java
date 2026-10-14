public class Person
{
    private String name, surname;
    private int age;
    private LeapCard leapCard;

    public Person(String name, String surname, String current_route, String start, String destination, int age)
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

    public void getDetails()
    {
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Age: " + age);
    }
}
