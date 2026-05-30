package barberiaapp;

import barberiaapp.config.Config;
import barberiaapp.userinterface.MenuApp;

public class Main {
    public static void main(String[] args) {
        MenuApp menuApp = Config.createMenuApp();
        menuApp.mostrarMenuPrincipal();
    }
}
