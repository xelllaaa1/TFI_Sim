import java.util.function.Predicate;

public class Menu
{
    private static Predicate<String> notBlank = s -> !s.isBlank();
    private static Predicate<String> lengthBounds = s -> s.length() >= 2 && s.length() <= 20;
    private static Predicate<String> validSymbols = s -> s.matches("[\\p{L}' -]+");
    private static Predicate<String> validAge = s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) <= 99) && (Integer.parseInt(s) >= 5);
    private static Predicate<String> validRoute = s -> s.matches("[A-Za-z0-9]+") && (s.length() <= 3);
    private static Predicate<String> validStop = s -> s.matches("[A-Za-z0-9 '.-]+") && (s.length() >= 3) && (s.length() <= 30);

    public Person initialiseUser()
    {
        String name, surname;
        int age;

        // Request data
        name = askUntilValid("Enter your name: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Name must be 2-20 characters: letters, spaces, hyphens or apostrophes only");  
        surname = askUntilValid("Enter your surname: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Surname must be 2-20 characters: letters, spaces, hyphens or apostrophes only");
        age = Integer.parseInt(askUntilValid("Enter your age: ", validAge, "[Error] Age must be a number between 5 and 99"));

        System.out.printf("\nWelcome to Dublin, %s!\nYour Leap card has been successfully activated.\n", name);
        return new Person(name, surname, age);
    }

    public void askTripDetails(Person user)
    {
        LeapCard leap = user.getLeapCard();

        leap.setRoute(askUntilValid("Enter route: ", validRoute, "[Error] Route ID must be 1-3 characters: A-Z letters, digits 0-9").toUpperCase());
        
        leap.setStart(askUntilValid("Enter starting stop: ", validStop, "[Error] Stop name must be 3-30 characters: A-Z letters, digits 0-9, apostrophes, spaces, hyphens, full stops"));

        leap.setDestination(askUntilValid("Enter destination stop: ", validStop, "[Error] Stop name must be 3-30 characters: A-Z letters, digits 0-9, apostrophes, spaces, hyphens, full stops"));
    }

    public void invalidTripHandler(LeapCard.Validation validity)
    {
        switch(validity)
        {
            case VALID ->
            {
                System.out.println("\nTrip details confirmed. Off we go!");
            }

            case BAD_ROUTE ->
            {
                System.out.println("\nRoute you entered does not exist!\n");
            }

            case BAD_START ->
            {
                System.out.println("\nStart stop you entered does not exist!\n");
            }

            case BAD_DESTINATION ->
            {
                System.out.println("\nDestination stop you entered does not exist!\n");
            }

            case ZERO_STOPS ->
            {
                System.out.println("\nYou cannot travel zero stops!\n");
            }
        }
    }

    public boolean continueTravel()
    {
        System.out.println("\nOne more trip?");
        System.out.println("1. Yes\n2. No");
        int choice = Integer.parseInt(askUntilValid("Enter your choice: ", s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) >= 1) && (Integer.parseInt(s) <= 2), "[Error] Choice must be 1 (\"Yes\") or 2(\"No\")"));

        switch(choice)
        {
            case 1 ->
            {
                return true;
            }
            
            case 2 ->
            {
                return false;
            }

            default ->
            {
                return false;
            }
        }
    }

    public String askUntilValid(String prompt, Predicate<String> isValid, String errorMessage)
    {
        String input;
        boolean isGood;
        
        do
        {
            System.out.print(prompt);
            input = Utility.scanner.nextLine().strip();
            isGood = isValid.test(input);

            if(!isGood)
            {
                System.out.println(errorMessage + "\n");
            }
        }
        while(!isGood);

        return input;
    }

    public void menu(Person user)
    {
        boolean in_use = true;
        LeapCard leap = user.getLeapCard();

        // Greet the user and print their details.
        System.out.println("\n\nYou are standing alone on a feels-like almost desolate Dublin Bus stop on the edge of the city...");
        leap.printLeapCardDetails();

        while(in_use)
        {
            askTripDetails(user);
            LeapCard.Validation validity = leap.validateTrip();

            if(validity == LeapCard.Validation.VALID)
            {
                Bus bus = new Bus();
                double time_spent = user.travel(bus);

                System.out.println("\nFare: " + leap.getTripFare());
                System.out.println("Current balance: " + leap.getBalance());
                System.out.println("Time spent: " + time_spent);
            }
            else
            {
                invalidTripHandler(validity);
            }

            if(!continueTravel())
            {
                in_use = false;
            }
        }
    }
}
