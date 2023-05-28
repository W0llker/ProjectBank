package Project.StartMenu;

import Project.Class.Role;
import Project.Class.Users;
import Project.Controller.AdminController;
import Project.Controller.ClientController;
import Project.Controller.ControllerControls;
import Project.Controller.UserController;
import Project.Exception.ExceptionExit;
import Project.Exception.ExceptionUsers;

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
                System.out.println("1 - Регистрация пользователя\n" +
                        "2 - Войти в личный кабинет");
                work = scanner.next();
                if (work.equals("1")) {
                    Users users = userController.registration(scanner);
                } else if (work.equals("2")) {
                    Users users = userController.autotenification(scanner);
                    lestgoMenu(users, clientController, adminController, scanner);
                }
            }
        } catch (ExceptionUsers e) {
            System.err.println(e.getMessage());
        } catch (ExceptionExit e) {
            e.getMessage();
        }
        menu();
    }
    public static void lestgoMenu(Users users,ClientController clientController, AdminController adminController,Scanner scanner) throws ExceptionExit {
        if(users.getRole().equals(Role.ADMIN)) {
            adminController.adminMenu(scanner, users);
        } else if(users.getRole().equals(Role.CLIENT)) {
            clientController.clientMenu(scanner, users);
        }
    }

}
