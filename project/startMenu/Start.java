package project.startMenu;

import project.controller.AdminController;
import project.controller.ClientController;
import project.controller.ControllerControls;
import project.controller.UserController;
import project.entity.Role;
import project.entity.Users;
import project.exception.BankException;
import project.exception.ExceptionExit;
import project.exception.ExceptionUsers;

import java.util.Scanner;

public class Start {
    public static void menu() {
        String work = "";
        ClientController clientController = ControllerControls.getClientController();
        AdminController adminController = ControllerControls.getAdminController();
        UserController userController = ControllerControls.getUserController();
        Scanner scanner = new Scanner(System.in);
        try {
            while (!work.equals("stop")) {
                System.out.println("░░░░░░▄░░▄░░░░░░░░░░░░░░░░░░\n" +
                        "░░░░░░██▄██░░░░BANK░░░░░░░░░░\n" +
                        "░░░░▄██████░░░░░░░FOX░░░░░░░░\n" +
                        "░░░▄█████▄██▄▄▄▄░░░░░░░░▄▄▄▄▄\n" +
                        "░░████████████▀░░░░░░░▄█████░\n" +
                        "░▄████████▀▀▀░░░░░░░▄██████░░");
                System.out.println("1 - Регистрация пользователя\n" +
                        "2 - Войти в личный кабинет");
                work = scanner.next();
                if (work.equals("1")) {
                    Users users = userController.registration(scanner);
                } else if (work.equals("2")) {
                    Users users = userController.autotenification(scanner);
                    lestGoMenu(users, clientController, adminController, scanner);
                }
            }
        } catch (ExceptionUsers e) {
            System.err.println(e.getMessage());
        } catch (BankException e) {

        }
        menu();
    }

    public static void lestGoMenu(Users users, ClientController clientController, AdminController adminController, Scanner scanner) throws ExceptionExit {
        if (users.getRole().equals(Role.ADMIN)) {
            adminController.adminMenu(scanner, users);
        } else if (users.getRole().equals(Role.CLIENT)) {
            clientController.clientMenu(scanner, users);
        }
    }

}
