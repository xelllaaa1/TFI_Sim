import java.util.*;

public class LeapCard
{
    private String cardNumber;
    private double balance = 0;
    private String type = "Adult";
    private ArrayList<String> history = new ArrayList<String>();

    public LeapCard()
    {
        Random random = new Random();
        cardNumber = String.format("%08d", random.nextInt(100000000));
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

    public void setType(String type)
    {
        this.type = type;
    }

    // Useful functions
    public void printDetails()
    {
        System.out.println("Balance: " + balance);
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

}
