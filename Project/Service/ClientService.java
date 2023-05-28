package Project.Service;

import Project.Class.Accounts;
import Project.Class.Operation;
import Project.Class.Users;
import Project.Exception.ExceptionExit;
import Project.Exception.ExceptionUsers;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public interface ClientService {
    Accounts addAccounts(long accountNumber, String currency, long userId) throws ExceptionUsers;
    void payment(Accounts accounts, double value, Users users);
    void moneyTransfer(long accountsTo, long accountsFrom, double value) throws ExceptionExit;
    void deleteAccounts(long id, long usersId) throws ExceptionUsers;
    void changeLogin(String login, long usersId) throws ExceptionUsers;
    void changePassword(String password, long usersId) throws ExceptionUsers;
    double convertValueTOBYN(List<Accounts> accounts);
    public List<Operation> translationHistory(long id, LocalDate localDate);
}
