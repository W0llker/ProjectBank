package Project.Controller;

import Project.Class.Users;
import Project.Exception.ExceptionUsers;
import Project.Service.UserService;
import Project.Service.impl.UserServiceImpl;
import Project.repository.RepositoryUser;

import java.util.Scanner;

public class UserController {
    private final UserService userService;

    public UserController(RepositoryUser repositoryUser) {
        userService = new UserServiceImpl(repositoryUser);
    }

    public Users registration(Scanner scanner) throws ExceptionUsers {
        System.out.println("Введите логин");
        String login = scanner.next();
        System.out.println("Введите пароль");
        String password = scanner.next();
        System.out.println("Введите имя");
        String name = scanner.next();
        System.out.println("Введите фамилию");
        String surName = scanner.next();
        return userService.createUsers(login, password, name, surName);
    }

    public Users autotenification(Scanner scanner) throws ExceptionUsers {
        System.out.println("Введите логин");
        String login = scanner.next();
        System.out.println("Введите пароль");
        String password = scanner.next();
        return userService.cheakUsers(login, password);
    }
}
