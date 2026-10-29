public class Menu
{
    public Person initialiseUser()
    {
        String name, surname;
        int age;

        // Request data
        System.out.print("Enter your name: ");
        name = Utility.scanner.nextLine().strip();
    
        System.out.print("Enter your surname: ");
        surname = Utility.scanner.nextLine().strip();

        System.out.print("Enter your age: ");
        age = Utility.scanner.nextInt();

        Utility.scanner.nextLine();

        System.out.printf("\nWelcome to Dublin, %s, your new Leap card has been activated, good luck!", name);
        return new Person(name, surname, age);
    }

    public void askTripDetails(Person user)
    {
        LeapCard leap = user.getLeapCard();

        System.out.print("What bus are we thinking?: ");
        leap.setRoute(Utility.scanner.nextLine().strip().toUpperCase());
        
        System.out.print("Where exactly are we?... : ");
        leap.setStart(Utility.scanner.nextLine().strip());

        System.out.print("Where are we going?: ");
        leap.setDestination(Utility.scanner.nextLine().strip());
    }

    public void invalidTripHandler(LeapCard.Validation validity)
    {
        switch(validity)
        {
            case VALID ->
            {
                // TODO: Do something about this. Valid is already handled in the caller method.
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

            case BACKWARD_DIRECTION ->
            {
                System.out.println("\nYou cannot travel in backward direction (for now)!\n");
            }

            case ZERO_STOPS ->
            {
                System.out.println("\nYou cannot travel zero stops!\n");
            }
        }
    }

    public boolean continueTravel(String input)
    {
        if(input.equalsIgnoreCase("yes")) return true;
        else return false;
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

            System.out.print("Start a new trip?: (Say 'yes' to proceed, 'no' to stay here.): ");
            if(!(continueTravel(Utility.scanner.nextLine().strip()))) in_use = false;
        }
    }
}
