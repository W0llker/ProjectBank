package project.service;

import project.entity.Accounts;
import project.entity.Operation;
import project.entity.Users;
import project.exception.ExceptionExit;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;

import java.time.LocalDate;
import java.util.List;

public interface ClientService {
    Accounts addAccounts(long accountNumber, String currency, long userId) throws ExceptionUsers;

    void payment(long id, double value, Users users) throws ExceptionUsers, ExceptionOperation;

    void moneyTransfer(long accountsTo, long accountsFrom, double value) throws ExceptionExit, ExceptionUsers, ExceptionOperation;

    void deleteAccounts(long id, long usersId) throws ExceptionUsers, ExceptionOperation;

    void changeLogin(String login, long usersId) throws ExceptionUsers;

    void changePassword(String password, long usersId) throws ExceptionUsers;

    double convertValueTOBYN(List<Accounts> accounts);

    List<Operation> translationHistory(long id, LocalDate localDate);

    List<Accounts> getAccountsToUser(long userId);

    String getCurrencyAll();
}
