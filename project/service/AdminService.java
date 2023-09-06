package project.service;

import project.entity.Accounts;
import project.entity.Operation;
import project.entity.Users;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;

import java.util.List;

public interface AdminService {
    List<Operation> getAllOperation();

    List<Operation> getOperationUsers(long id);

    void createUsers(String login, String password, String name, String surName) throws ExceptionUsers;

    void deleteUsers(long id);

    List<Users> getAllUsers();

    List<Accounts> getAllAccounts();

    void changeLogin(String login, long usersId) throws ExceptionUsers;

    void changePassword(String password, long usersId) throws ExceptionUsers;

    Accounts createAccount(long accountNumber, String currency, long userId) throws ExceptionUsers, ExceptionOperation;

    Accounts getCommissionsAccount(long userId) throws ExceptionUsers, ExceptionOperation;

    List<Operation> getOperationNotPayCommissions();

    double getValueCommissions();

    void payCommissions(Accounts accounts);

    void changeCommissions(double valueCommissions);

    void getInformationUser(long id) throws ExceptionUsers;

    List<List<Operation>> getListOperation();

    List<List<Users>> getListUsers();
}
