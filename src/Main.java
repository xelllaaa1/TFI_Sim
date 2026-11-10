public class Main
{
    public static void main(String[] args)
    {
        Utility.routes = Utility.loadRoutes("resources/routes.txt");
        
        Menu mainMenu = new Menu();
        
        Person user = mainMenu.initialiseUser();

        mainMenu.mainMenu(user);
        Utility.scanner.close();
    }
}
