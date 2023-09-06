package project.controller;

import project.entity.Accounts;
import project.entity.Users;
import project.exception.ExceptionExit;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;
import project.repository.RepositoryAccount;
import project.repository.RepositoryOperation;
import project.repository.RepositoryUser;
import project.service.ClientService;
import project.service.impl.ClientServiceImpl;
import project.validator.AccountValidator;
import project.validator.UsersValidator;

import java.time.LocalDate;
import java.util.Scanner;

public class ClientController {
    private final ClientService clientService;
    private final RepositoryUser repositoryUser;
    private final UsersValidator usersValidator;
    private final AccountValidator accountValidator;

    public ClientController(RepositoryUser repositoryUser, RepositoryAccount repositoryAccount, RepositoryOperation repositoryOperation) {
        this.repositoryUser = repositoryUser;
        this.usersValidator = new UsersValidator();
        this.accountValidator = new AccountValidator();
        clientService = new ClientServiceImpl(repositoryAccount, repositoryOperation, repositoryUser);
    }

    public void clientMenu(Scanner scanner, Users users) throws ExceptionExit {
        System.out.println("Добро пожаловать " + users.getName() + " " + users.getSurName());
        System.out.println("Выберите действие:\n" +
                "1 - Обмен валюты\n" +
                "2 - Перевод средств на другой счет\n" +
                "3 - Просмотр текущих счетов\n" +
                "4 - Просмотр текущих курсов\n" +
                "5 - Создание счета\n" +
                "6 - Внесение денег на счет\n" +
                "7 - Удаление счета\n" +
                "8 - Смена пароля\n" +
                "9 - Смена логина\n" +
                "10 - Посчитать общую сумму на счетах в BYN\n" +
                "11 - Вывести все операции по счету за промежуток времени\n" +
                "12 - Выход");
        try {
            String numberAction = scanner.next();
            switch (numberAction) {
                case "1":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    System.out.println("Выберите счет валюты которую меняете");
                    long idAc = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(idAc, users.getId());
                    System.out.println("Введите счет валюты которую получайте");
                    long idRecipient = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(idRecipient, users.getId());
                    if (idAc == idRecipient) {
                        throw new ExceptionOperation("Нельзя обменять одну и туже валюту");
                    }
                    System.out.println("Введите сумма для обмена");
                    double value = scanner.nextDouble();
                    clientService.moneyTransfer(idAc, idRecipient, value);
                    break;
                case "2":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    System.out.println("Выберите счет с которого хотите сделать перевод");
                    idAc = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(idAc, users.getId());
                    System.out.println("Введите счет на который хотите перевести сумму (другого человека)");
                    idRecipient = scanner.nextLong();
                    confirmationTranslation(idRecipient, scanner, users);
                    System.out.println("Введите сумма для перевода");
                    value = scanner.nextDouble();
                    clientService.moneyTransfer(idAc, idRecipient, value);
                    break;
                case "3":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    break;
                case "4":
                    System.out.println(clientService.getCurrencyAll());
                    break;
                case "5":
                    System.out.println("Введите номер счета");
                    long accountNumber = scanner.nextLong();
                    accountValidator.checkAccountNumber(accountNumber);
                    System.out.println("Введите валюту счета");
                    String currency = scanner.next();
                    accountValidator.checkCurrency(currency);
                    System.out.println(clientService.addAccounts(accountNumber, currency, users.getId()));
                    break;
                case "6":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    System.out.println("Выберите id счета который хотите пополнить");
                    long id = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(id, users.getId());
                    System.out.println("Введите сумма пополнения (пополнение происходит в BYN)");
                    value = scanner.nextDouble();
                    if (value < 0) {
                        throw new ExceptionOperation("Нельзя пополнить на эту сумму");
                    }
                    clientService.payment(id, value, users);
                    break;
                case "7":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    System.out.println("Введите id счета который хотите удалить");
                    id = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(id, users.getId());
                    clientService.deleteAccounts(id, users.getId());
                    break;
                case "8":
                    changePass(scanner, users);
                    break;
                case "9":
                    changeLogin(scanner, users);
                    break;
                case "10":
                    System.out.println("Ваш счет в BYN= " + clientService.convertValueTOBYN(clientService.getAccountsToUser(users.getId())));
                    break;
                case "11":
                    System.out.println(clientService.getAccountsToUser(users.getId()));
                    System.out.println("Выберите id счета который вас интересует");
                    long idAccount = scanner.nextLong();
                    accountValidator.checkAccountTheUsers(idAccount, users.getId());
                    System.out.println("Введите дату от которой вас интересует история(год-месяц-дата)");
                    String date = scanner.next();
                    System.out.println(clientService.translationHistory(idAccount, LocalDate.parse(date)));
                    break;
                case "12":
                    throw new ExceptionExit();
            }
        } catch (ExceptionUsers | ExceptionOperation e) {
            System.err.println(e.getMessage());
            System.out.println("");
        }
        System.out.println();
        clientMenu(scanner, users);
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
            clientService.changePassword(password, users.getId());
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
            clientService.changeLogin(login, users.getId());
        } else throw new ExceptionUsers("Данные введены неверно");
    }

    private void confirmationTranslation(long id, Scanner scanner, Users user) throws ExceptionUsers, ExceptionOperation {
        Accounts accounts = accountValidator.checkAccountNotTheUsers(id, user.getId());
        Users users = repositoryUser.getUsers(accounts.getUserId());
        System.out.println("Пользователь \n" + users.userInformation() + "\nсчет на который " +
                "будет произведен перевод\n" + accounts.accountInformation() + "\n");
        System.out.println("Вы действительно хотите перевести деньги данному пользователю?(Да/Нет)");
        String words = scanner.next();
        switch (words) {
            case "Да":
                break;
            case "Нет":
                throw new ExceptionOperation("Отмена");
        }
    }
}
