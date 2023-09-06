package project.controller;

import project.entity.Operation;
import project.entity.Users;
import project.exception.ExceptionExit;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;
import project.repository.RepositoryAccount;
import project.repository.RepositoryOperation;
import project.repository.RepositoryUser;
import project.service.AdminService;
import project.service.impl.AdminServiceImpl;
import project.validator.AccountValidator;
import project.validator.UsersValidator;

import java.util.List;
import java.util.Scanner;

public class AdminController {
    private final AdminService service;
    private final AccountValidator accountValidator;
    private final UsersValidator usersValidator;

    public AdminController(RepositoryUser repositoryUser, RepositoryOperation repositoryOperation, RepositoryAccount repositoryAccount) {
        accountValidator = new AccountValidator();
        usersValidator = new UsersValidator();
        this.service = new AdminServiceImpl(repositoryUser, repositoryAccount, repositoryOperation);
    }

    public void adminMenu(Scanner scanner, Users users) throws ExceptionExit {
        System.out.println("Добро пожаловать " + users.getName() + " " + users.getSurName());
        System.out.println("Выберите действие:\n" +
                "1 - Просмотр всех клиентов\n" +
                "2 - Просмотр операций по клиенту\n" +
                "3 - Удаление клиента\n" +
                "4 - Смена пароля\n" +
                "5 - Смена логина\n" +
                "6 - Создание счета\n" +
                "7 - Просмотр счета с коммисиями\n" +
                "8 - Просмотр счета с неоплаченными коммисиями\n" +
                "9 - Перевод коммисий на счет\n" +
                "10 - Получить сумму коммисий не переведенных\n" +
                "11 - Изменение коммиссии\n" +
                "12 - Регистрация пользователя\n" +
                "13 - Просмотр клиента по логину\n" +
                "14 - Просмотр всех счетов\n" +
                "15 - Просмотр всех операций\n" +
                "16 - Просмотр всех операций(пагинация)\n" +
                "17 - Просмотр всех клиентов(пагинация)\n" +
                "18 - Выход");
        String numberAction = scanner.next();
        try {
            switch (numberAction) {
                case "1":
                    System.out.println(service.getAllUsers());
                    break;
                case "2":
                    System.out.println(service.getAllUsers());
                    System.out.println("Введите id клиента по которому хотите посмотреть Операции");
                    long id = scanner.nextLong();
                    usersValidator.checkUsersId(id);
                    System.out.println(service.getOperationUsers(id));
                    break;
                case "3":
                    System.out.println(service.getAllUsers());
                    System.out.println("Введите id клиента которого нужно удалить");
                    id = scanner.nextLong();
                    usersValidator.checkUsersId(id);
                    service.deleteUsers(id);
                    break;
                case "4":
                    changePass(scanner, users);
                    break;
                case "5":
                    changeLogin(scanner, users);
                    break;
                case "6":
                    System.out.println("Введите номер счета");
                    long accountNumber = scanner.nextLong();
                    accountValidator.checkAccountNumber(accountNumber);
                    System.out.println(service.createAccount(accountNumber, "BYN", users.getId()));
                    break;
                case "7":
                    System.out.println(service.getCommissionsAccount(users.getId()));
                    break;
                case "8":
                    System.out.println(service.getOperationNotPayCommissions());
                    break;
                case "9":
                    service.payCommissions(service.getCommissionsAccount(users.getId()));
                    break;
                case "10":
                    System.out.println("Сумма коммисий не переведенных = " + service.getValueCommissions());
                    break;
                case "11":
                    System.out.println("Введите процент от коммиссии");
                    double value = scanner.nextDouble();
                    if (value > 30) {
                        throw new ExceptionOperation("Коммисия не может быть больше 30%");
                    }
                    service.changeCommissions(value);
                    break;
                case "12":
                    System.out.println("Введите логин");
                    String login = scanner.next();
                    usersValidator.checkLogin(login);
                    System.out.println("Введите пароль");
                    String password = scanner.next();
                    System.out.println("Введите имя");
                    String name = scanner.next();
                    System.out.println("Введите фамилию");
                    String surName = scanner.next();
                    service.createUsers(login, password, name, surName);
                    break;
                case "13":
                    System.out.println("Введите логин клиент");
                    login = scanner.next();
                    service.getInformationUser(usersValidator.checkUsersLogin(login));
                    break;
                case "14":
                    System.out.println("Все счета");
                    System.out.println(service.getAllAccounts());
                    break;
                case "15":
                    System.out.println("Все операции");
                    System.out.println(service.getAllOperation());
                    break;
                case "16":
                    listOperation(scanner);
                    break;
                case "17":
                    listUsers(scanner);
                    break;
                case "18":
                    throw new ExceptionExit();
            }
        } catch (ExceptionUsers | ExceptionOperation exceptionUsers) {
            System.err.println(exceptionUsers.getMessage());
            System.out.println();
        }
        adminMenu(scanner, users);
    }

    private void listUsers(Scanner scanner) throws ExceptionUsers {
        List<List<Users>> listList = service.getListUsers();
        int a = 1;
        do {
            System.out.println(listList.get(a - 1));
            System.out.println("Текущая страница " + a + " Всего страниц " + listList.size() +
                    "\nВведите страницу которую хотите посмотреть");
            a = scanner.nextInt();
            if (a > listList.size()) {
                throw new ExceptionUsers("Нету такой страницы");
            }
        } while (a > 0);
    }

    private void listOperation(Scanner scanner) throws ExceptionUsers {
        List<List<Operation>> listList = service.getListOperation();
        int a = 1;
        do {
            System.out.println(listList.get(a - 1));
            System.out.println("Текущая страница " + a + " Всего страниц " + listList.size() +
                    "\nВведите страницу которую хотите посмотреть");
            a = scanner.nextInt();
            if (a > listList.size()) {
                throw new ExceptionUsers("Нету такой страницы");
            }
        } while (a > 0);
    }

    private void changePass(Scanner scanner, Users users) throws ExceptionUsers {
        System.out.println("Подтверждение пользователя");
        System.out.print("Введите логин:");
        String login = scanner.next();
        System.out.print("Введите пароль:");
        String password = scanner.next();
        if (users.getLogin().equals(login) && users.getPassword().equals(password)) {
            System.out.print("Введите новый пароль:");
            password = scanner.next();
            service.changePassword(password, users.getId());
        } else throw new ExceptionUsers("Данные введены неверно");
    }

    private void changeLogin(Scanner scanner, Users users) throws ExceptionUsers {
        System.out.println("Подтверждение пользователя");
        System.out.print("Введите логин:");
        String login = scanner.next();
        System.out.print("Введите пароль:");
        String password = scanner.next();
        if (users.getLogin().equals(login) && users.getPassword().equals(password)) {
            System.out.print("Введите новый логин:");
            login = usersValidator.checkLogin(scanner.next());
            service.changeLogin(login, users.getId());
        } else throw new ExceptionUsers("Данные введены неверно");
    }
}
