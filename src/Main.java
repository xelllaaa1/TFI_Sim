public class Main
{
    public static void main(String[] args)
    {
        Utility.routes = Utility.loadRoutes("resources/routes.txt");
        
        Menu main_menu = new Menu();
        
        Person user = main_menu.initialiseUser();

        main_menu.menu(user);
        
        Utility.scanner.close();
    }
}
