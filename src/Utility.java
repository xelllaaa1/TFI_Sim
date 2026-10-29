import java.io.*;
import java.util.*;

public class Utility
{
    // Create objects for future use.
    static HashMap<String, ArrayList<Stop>> routes;
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static HashMap<String, ArrayList<Stop>> loadRoutes(String path)
    {
        HashMap<String, ArrayList<Stop>> hashedRoutes = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(path)))
        {
            String fileLine, routeNumber;
            String[] stopsOfARoute;
            int colon, lineNumber = 0;
            int errors = 0;
            
            while((fileLine = reader.readLine()) != null)
            {
                lineNumber += 1;
                if(fileLine.isBlank())
                {
                    System.err.printf("[Warning][line %d] Empty line\n", lineNumber);
                    continue;
                }

                // Key
                colon = fileLine.indexOf(':');
                if(colon == -1)
                {
                    System.err.printf("[Error][line %d] no colon, this line will not be present in the program\n", lineNumber);
                    errors += 1;
                    continue;
                }
                routeNumber = fileLine.substring(0, colon).strip().toUpperCase();
                    
                // Value
                ArrayList<Stop> finalArrayList = new ArrayList<>();
                stopsOfARoute = fileLine.substring(colon + 1, fileLine.length()).split(",");
                boolean lineIsBroken = false;

                for(String stop : stopsOfARoute)
                {
                    stop = stop.strip();

                    try
                    {
                        Stop stopObject = new Stop(stop.substring(0, stop.indexOf("|")),
                        Integer.parseInt(stop.substring(stop.indexOf("|") + 1, stop.length()).strip()));
                        finalArrayList.add(stopObject);
                    }
                    catch(NumberFormatException e)
                    {
                        System.err.printf("[Error][line %d] Invalid distance to next stop from \"%s\", route \"%s\" will not be present in the program\n", lineNumber, stop, routeNumber);
                        lineIsBroken = true;
                        errors += 1;
                    }
                    catch(StringIndexOutOfBoundsException e)
                    {
                        System.err.printf("[Error][line %d] Something went wrong when parsing stop \"%s\" for route \"%s\"\n", lineNumber, stop, routeNumber);
                        lineIsBroken = true;
                        errors += 1;
                    }
                }

                if(!lineIsBroken)
                {
                    if(finalArrayList.isEmpty())
                    {
                        System.err.printf("[Error][line %d] Route \"%s\" is empty and will not be present in the program\n", lineNumber, routeNumber);
                        errors += 1;
                    }
                    else if(hashedRoutes.containsKey(routeNumber))
                    {
                        System.err.printf("[Error][line %d] Route \"%s\" is defined more than once\n", lineNumber, routeNumber);
                        errors += 1;
                    }
                    else
                    {
                        hashedRoutes.put(routeNumber, finalArrayList);
                    }
                }
            }
        
            if(errors != 0)
            {
                System.err.printf("%d errors have occurred. Exiting...\n", errors);
                System.exit(1);
            }
        }
        catch(IOException e)
        {
            System.err.printf("Something went wrong when loading routes from %s, exiting...\n", path);
            e.printStackTrace();
            System.exit(1);
        }

        return hashedRoutes;
    }

    public static int getStopPosition(ArrayList<Stop> stops, String target)
    {
        for(int i = 0; i < stops.size(); i++)
        {
            if(stops.get(i).getName().equalsIgnoreCase(target)) return i; 
        }
        return -1;
    }
}
