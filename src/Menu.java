import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.time.format.DateTimeFormatter;

public class Menu
{
    private static Predicate<String> notBlank = s -> !s.isBlank();
    private static Predicate<String> lengthBounds = s -> s.length() >= 2 && s.length() <= 20;
    private static Predicate<String> validSymbols = s -> s.matches("[\\p{L}' -]+");
    private static Predicate<String> validAge = s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) <= 99) && (Integer.parseInt(s) >= 5);
    private static Predicate<String> validRoute = s -> s.matches("[A-Za-z0-9]+") && (s.length() <= 3);
    private static Predicate<String> validStop = s -> s.matches("[A-Za-z0-9 '.-]+") && (s.length() >= 3) && (s.length() <= 30);
    private static Predicate<String> validTopUp = s -> s.matches("\\d+(\\.[\\d]{1,2})?") && Double.parseDouble(s) >= 5 && Double.parseDouble(s) <= 100;

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // The word the player types to back out of entering trip details.
    private static final String CANCEL_WORD = "cancel";
    private static Predicate<String> isCancel = s -> s.equalsIgnoreCase(CANCEL_WORD);

    // Width of the headers and separator lines, in characters.
    private static final int WIDTH = 46;

    // Prints a centred title between two lines, e.g. the title of a menu.
    private void printHeader(String title)
    {
        String line = "=".repeat(WIDTH);
        int padding = Math.max(0, (WIDTH - title.length()) / 2);

        System.out.println("\n" + line);
        System.out.println(" ".repeat(padding) + title);
        System.out.println(line);
    }

    // Prints one numbered menu option, indented the same way everywhere.
    private void printOption(int number, String text)
    {
        System.out.printf("  %d. %s\n", number, text);
    }

    // Prints the card holder, card number, card type and balance, e.g. above the main menu options.
    private void printCardInfo(Person user)
    {
        LeapCard leap = user.getLeapCard();

        System.out.printf("  %-10s %s %s\n", "Holder:", user.getName(), user.getSurname());
        System.out.printf("  %-10s #%s (%s)\n", "Card:", leap.getCardNumber(), leap.getType());
        System.out.printf("  %-10s EUR %.2f\n", "Balance:", leap.getBalance());
        System.out.println("-".repeat(WIDTH));
    }

    // Turns minutes into a friendlier text: 45 -> "45 min", 133 -> "2 h 13 min".
    private String formatMinutes(int minutes)
    {
        if(minutes < 60)
        {
            return minutes + " min";
        }

        return (minutes / 60) + " h " + String.format("%02d", minutes % 60) + " min";
    }

    // Prints the summary of a finished trip as a small aligned receipt.
    private void printTripSummary(LeapCard leap, int minutes)
    {
        String line = "-".repeat(WIDTH);

        System.out.println("\n" + line);
        System.out.println("  TRIP SUMMARY");
        System.out.println(line);
        System.out.printf("  %-10s %s\n", "Route:", leap.getRoute());
        System.out.printf("  %-10s %s\n", "From:", leap.getStart());
        System.out.printf("  %-10s %s\n", "To:", leap.getDestination());
        System.out.printf("  %-10s %.2f km\n", "Distance:", (double) leap.getTripDistance() / 1000);
        System.out.printf("  %-10s ~ %s\n", "Time:", formatMinutes(minutes));
        System.out.printf("  %-10s EUR %.2f\n", "Fare:", leap.getTripFare());
        System.out.printf("  %-10s EUR %.2f\n", "Balance:", leap.getBalance());
        System.out.println(line);
    }

    public Person initialiseUser()
    {
        String name, surname;
        int age;

        // Request data
        name = askUntilValid("Enter your name: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Name must be 2-20 characters: letters, spaces, hyphens or apostrophes only");  
        surname = askUntilValid("Enter your surname: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Surname must be 2-20 characters: letters, spaces, hyphens or apostrophes only");
        age = Integer.parseInt(askUntilValid("Enter your age: ", validAge, "[Error] Age must be a number between 5 and 99"));

        printHeader("WELCOME TO DUBLIN, " + name.toUpperCase() + "!");
        System.out.println("  Your Leap card has been successfully activated.");
        return new Person(name, surname, age);
    }

    // Asks for the route and both stops. Returns false if the player typed the cancel word
    // at any of the three questions; in that case the card's trip details are left untouched.
    public boolean askTripDetails(Person user)
    {
        LeapCard leap = user.getLeapCard();
        String route, start, destination;

        System.out.printf("  (type \"%s\" at any question to go back)\n\n", CANCEL_WORD);

        route = askUntilValid("Enter route: ", isCancel.or(validRoute), "[Error] Route ID must be 1-3 characters: A-Z letters, digits 0-9");
        if(isCancel.test(route))
        {
            return false;
        }

        start = askUntilValid("Enter starting stop: ", validStop, "[Error] Stop name must be 3-30 characters: A-Z letters, digits 0-9, apostrophes, spaces, hyphens, full stops");
        if(isCancel.test(start))
        {
            return false;
        }

        destination = askUntilValid("Enter destination stop: ", validStop, "[Error] Stop name must be 3-30 characters: A-Z letters, digits 0-9, apostrophes, spaces, hyphens, full stops");
        if(isCancel.test(destination))
        {
            return false;
        }

        leap.setRoute(route.toUpperCase());
        leap.setStart(start);
        leap.setDestination(destination);

        return true;
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
        printOption(1, "Yes");
        printOption(2, "No");
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
        System.out.printf("\nTop-up successful: EUR %.2f\n", amount);
        System.out.printf("New balance:       EUR %.2f\n", leap.getBalance());
    }

    public void printHistory(LeapCard leap)
    {
        printHeader("CARD HISTORY");
        for(int i = leap.getHistory().size() - 1; i >= 0; i--)
        {
            HistoryEntry historyEntry = leap.getHistory().get(i);
            System.out.printf("  %s | %s\n", historyEntry.getTimeStamp().format(dateFormat), historyEntry.getDescription());
        }
    }

    public void changePersonalDetails(Person user)
    {
        boolean inUse = true;
        while(inUse)
        {
            printHeader("CHANGE PERSONAL DETAILS");
            printOption(1, "Name");
            printOption(2, "Surname");
            printOption(3, "Age");
            printOption(4, "Back to account management");

            int choice = Integer.parseInt(askUntilValid("Enter choice: ", s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) >= 1) && (Integer.parseInt(s) <= 4), "[Error] Choice must be \"1\", \"2\", \"3\" or \"4\"!"));
            String stringInput = "";

            switch(choice)
            {
                case 1 ->
                {
                    stringInput = askUntilValid("Enter your new name: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Name must be 2-20 characters: letters, spaces, hyphens or apostrophes only");  
                    user.setName(stringInput);
                    System.out.printf("\nName changed to %s.\n", stringInput);
                }
                case 2 ->
                {
                    stringInput = askUntilValid("Enter your new surname: ", notBlank.and(lengthBounds).and(validSymbols), "[Error] Surname must be 2-20 characters: letters, spaces, hyphens or apostrophes only");
                    user.setSurname(stringInput);
                    System.out.printf("\nSurname changed to %s.\n", stringInput);
                }
                case 3 ->
                {
                    int newAge = Integer.parseInt(askUntilValid("Enter your age: ", validAge, "[Error] Age must be a number between 5 and 99"));
                    String cardType = user.getLeapCard().getType();
                    boolean validAgeMenu = false;
                    int tries = 2;

                    while(!validAgeMenu)
                    {
                        if(cardType.equalsIgnoreCase("Adult") && newAge > 25)
                        {
                            user.setAge(newAge);
                            validAgeMenu = true;
                        }
                        else if(cardType.equalsIgnoreCase("Young Adult / Student") && newAge > 15 && newAge < 26)
                        {
                            user.setAge(newAge);
                            validAgeMenu = true;
                        }
                        else if(cardType.equalsIgnoreCase("Child") && newAge < 16)
                        {
                            user.setAge(newAge);
                            validAgeMenu = true;
                        }
                        else if(tries > 0)
                        {
                            System.out.println("\n[Error] Your new age must not change your card type!");
                            System.out.println("        For example, if you are 26 and want to change your age to 25,");
                            System.out.println("        you would need a new Leap card, as that changes your card type.");
                            System.out.println("\nTry to enter a new age that is within your card type's band.");
                            System.out.println("Tries left: " + tries + "\n");
                            tries -= 1;

                            newAge = Integer.parseInt(askUntilValid("Enter your age: ", validAge, "[Error] Age must be a number between 5 and 99"));
                        }
                        else
                        {
                            System.out.println("You have failed to enter sensible age. Exiting...");
                            System.exit(1);
                        }
                    }

                    System.out.printf("\nAge changed to %d.\n", newAge);
                }
                case 4 ->
                {
                    return;
                }
            }
        }
    }

    public void accountMenu(Person user)
    {
        boolean inUse = true;
        while(inUse)
        {
            printHeader("ACCOUNT MANAGEMENT");
            printOption(1, "Top-up balance");
            printOption(2, "Print card history");
            printOption(3, "Print personal details");
            printOption(4, "Change personal details");
            printOption(5, "Back to main menu");

            int choice = Integer.parseInt(askUntilValid("Enter choice: ", s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) >= 1) && (Integer.parseInt(s) <= 5), "[Error] Choice must be \"1\", \"2\", \"3\", \"4\" or \"5\"!"));

            switch(choice)
            {
                case 1 ->
                {
                    topUp(user.getLeapCard());
                }
                case 2 ->
                {
                    printHistory(user.getLeapCard());
                }
                case 3 ->
                {
                    printHeader("PERSONAL DETAILS");
                    user.printPersonDetails();
                }
                case 4 ->
                {
                    changePersonalDetails(user);
                }
                case 5 ->
                {
                    return;
                }
            }
        }
    }

    public void tripMenu(Person user)
    {
        boolean inUse = true;
        LeapCard leap = user.getLeapCard();

        while(inUse)
        {
            printHeader("TRIP");

            if(!askTripDetails(user))
            {
                System.out.println("\nTrip cancelled.");
                return;
            }

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
                    double fare = leap.calculateFare(Utility.routes.get(leap.getRoute()));
                    System.err.printf("\n[Error] Insufficient balance: the trip costs EUR %.2f but you have EUR %.2f.\n", fare, leap.getBalance());

                    if(!askYesNo("Would you like to top-up?"))
                    {
                        break;
                    }

                    topUp(leap);
                }

                if(leap.enoughMoneyForTrip(Utility.routes.get(leap.getRoute())))
                {
                    double timeSpent = user.travel();

                    printTripSummary(leap, (int) timeSpent);
                }
                else
                {
                    System.out.println("\nYou step off the bus without travelling.");
                }
            }

            if(!askYesNo("One more trip?"))
            {
                inUse = false;
            }
        }
    }

    public void mainMenu(Person user)
    {
        boolean inUse = true;

        // Greet the user and print their details.
        System.out.println("\nYou are standing alone on a feels-like almost desolate Dublin Bus stop on the edge of the city...");

        while(inUse)
        {
            printHeader("TFI SIMULATOR - MAIN MENU");
            printCardInfo(user);
            printOption(1, "Account management");
            printOption(2, "Trip");
            printOption(3, "Exit");

            int choice = Integer.parseInt(askUntilValid("Enter choice: ", s -> Utility.isWholeNumber(s) && (Integer.parseInt(s) >= 1) && (Integer.parseInt(s) <= 3), "[Error] Choice must be \"1\", \"2\" or \"3\"!"));

            switch(choice)
            {
                case 1 ->
                {
                    accountMenu(user);
                }
                case 2 ->
                {
                    tripMenu(user);
                }
                case 3 ->
                {
                    inUse = false;
                    break;
                }
            }
        }

        printHeader("THANK YOU FOR USING TFI SIMULATOR!");
    }
}
