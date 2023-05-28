package Project.Controller;

import Project.Validator.AccountValidator;
import Project.Validator.UsersValidator;
import Project.repository.RepositoryAccount;
import Project.repository.RepositoryOperation;
import Project.repository.RepositoryUser;

public class ControllerControls {
    public static ClientController clientController;
    public static AdminController adminController;
    public static UserController userController;

    private ControllerControls() {
    }

    public static ClientController getClientController() {
        if (clientController == null) {
            clientController = new ClientController(new RepositoryUser(), new RepositoryAccount(), new RepositoryOperation(), new UsersValidator());
        }
        return clientController;
    }

    public static AdminController getAdminController() {
        if (adminController == null) {
            adminController = new AdminController(new RepositoryUser(), new RepositoryOperation(), new RepositoryAccount());
        }
        return adminController;
    }

    public static UserController getUserController() {
        if (userController == null) {
            userController = new UserController(new RepositoryUser());
        }
        return userController;
    }
}
