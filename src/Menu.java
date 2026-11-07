import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class Menu
{
    private static Predicate<String> notBlank = s -> !s.isBlank();
    private static Predicate<String> lengthBounds = s -> s.length() >= 2 && s.length() <= 20;
    private static Predicate<String> validSymbols = s -> s.matches("[\\p{L}' -]+");
    private static Predicate<String> validAge = s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) <= 99) && (Integer.parseInt(s) >= 5);
    private static Predicate<String> validRoute = s -> s.matches("[A-Za-z0-9]+") && (s.length() <= 3);
    private static Predicate<String> validStop = s -> s.matches("[A-Za-z0-9 '.-]+") && (s.length() >= 3) && (s.length() <= 30);
    private static Predicate<String> validTopUp = s -> s.matches("\\d+(\\.[\\d]{1,2})?") && Double.parseDouble(s) >= 5 && Double.parseDouble(s) <= 100;

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

        leap.setRoute(askUntilValid("\nEnter route: ", validRoute, "[Error] Route ID must be 1-3 characters: A-Z letters, digits 0-9").toUpperCase());
        
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
                System.err.println("\n[Error] Route you entered does not exist!\n");
            }

            case BAD_START ->
            {
                System.err.println("\n[Error] Start stop you entered does not exist!\n");
            }

            case BAD_DESTINATION ->
            {
                System.err.println("\n[Error] Destination stop you entered does not exist!\n");
            }

            case ZERO_STOPS ->
            {
                System.err.println("\n[Error] You cannot travel zero stops!\n");
            }
        }
    }

    public boolean askYesNo(String prompt)
    {
        System.out.printf("\n%s\n", prompt);
        System.out.println("1. Yes\n2. No");
        int choice = Integer.parseInt(askUntilValid("Enter your choice: ", s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) >= 1) && (Integer.parseInt(s) <= 2), "[Error] Choice must be 1 (\"Yes\") or 2 (\"No\")"));

       return choice == 1;
    }

    public String askUntilValid(String prompt, Predicate<String> isValid, String errorMessage)
    {
        String input = "";
        boolean isGood;
        
        do
        {
            System.out.print(prompt);
            
            try
            {
                input = Utility.scanner.nextLine().strip();
            }
            catch(NoSuchElementException ex)
            {
                System.err.print("\n[Error] Input ended, exiting...\n");
                System.exit(1);
            }

            isGood = isValid.test(input);

            if(!isGood)
            {
                System.out.println(errorMessage + "\n");
            }
        }
        while(!isGood);

        return input;
    }

    public void topUp(LeapCard leap)
    {
        double amount = Double.parseDouble(askUntilValid("Enter amount: ", validTopUp, "[Error] Amount must be at least 5 and at most 100 with up to 2 decimals"));
        leap.addBalance(amount);
        System.out.printf("Topped-up with %.2f!\nNew balance: %.2f\n", amount, leap.getBalance());
    }

    public void menu(Person user)
    {
        boolean inUse = true;
        LeapCard leap = user.getLeapCard();

        // Greet the user and print their details.
        System.out.println("\n\nYou are standing alone on a feels-like almost desolate Dublin Bus stop on the edge of the city...");
        leap.printLeapCardDetails();

        while(inUse)
        {
            askTripDetails(user);
            LeapCard.Validation validity = leap.validateTrip();

            if(validity != LeapCard.Validation.VALID)
            {
                invalidTripHandler(validity);
            }
            else
            {
                Bus bus = new Bus();
                bus.waitForBus();

                while(!(leap.enoughMoneyForTrip(Utility.routes.get(leap.getRoute()))))
                {
                    if(!askYesNo("Your balance is too low for this trip. Would you like to top-up?"))
                    {
                        break;
                    }

                    topUp(leap);
                }

                if(leap.enoughMoneyForTrip(Utility.routes.get(leap.getRoute())))
                {
                    double time_spent = user.travel();

                    System.out.println("\nFare: " + leap.getTripFare());
                    System.out.println("Current balance: " + leap.getBalance());
                    System.out.println("Distance travelled: " + ((double) leap.getTripDistance() / 1000) + " km");
                    System.out.println("Time spent: ~ " + (int)time_spent + " minutes");
                }
                else
                {
                    System.err.println("[Error] Insufficient balance, top up your card's balance");
                }
            }

            if(!askYesNo("One more trip?"))
            {
                inUse = false;
            }
        }
    }
}
