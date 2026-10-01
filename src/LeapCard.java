import java.util.*;
import java.io.*;

public class LeapCard
{
    private String cardNumber;
    private double balance = 0;
    private String type = "Adult";
    private Person owner;

    private ArrayList<String> history = new ArrayList<String>();

    public LeapCard(Person owner)
    {
        Random random = new Random();
        cardNumber = String.format("%08d", random.nextInt(100000000));
        history.add("Card #" + cardNumber + " activated.");
        
        this.owner = owner;
    }

    // Getters and setters for instance variables
    public String getCardNumber()
    {
        return cardNumber;
    }

    public double getBalance()
    {
        return balance;
    }
    
    public String getType()
    {
        return type;
    }

    public ArrayList<String> getHistory()
    {
        return history;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    // Useful functions
    public void printDetails()
    {
        System.out.println("\nName: " + owner.getName() + " " + owner.getSurname());
        System.out.println("Current route: " + owner.getRoute());
        System.out.println("Start: " + owner.getStart());
        System.out.println("Destination: " + owner.getDestination());
        System.out.println("\nBalance: " + balance);
        System.out.println("Card Type: " + type);
        System.out.println("Card Number: " + cardNumber);
    }

    public void addBalance(double amount)
    {
        if(amount > 0)
            balance += amount;

        else
        {
            System.out.println("Amount has to be positive.");
            balance += 0;
        }
    }

    public void spendBalance(double amount)
    {
        if(amount > 0)
            balance -= amount;

        else
        {
            System.out.println("Amount has to be positive.");
            balance -= 0;
        }
    }

    public void printHistory()
    {
        System.out.println("\nCard History:");
        for(int i = 0; i < history.size(); i++)
        {
            System.out.println(history.get(i));
        }
    }
}
