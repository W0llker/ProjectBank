package project.service.impl;

import project.entity.*;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;
import project.repository.CurrencyRepository;
import project.repository.RepositoryAccount;
import project.repository.RepositoryOperation;
import project.repository.RepositoryUser;
import project.service.AdminService;
import project.startMenu.CurrencySetting;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;

public class AdminServiceImpl implements AdminService {
    private final RepositoryOperation repositoryOperation;
    private final RepositoryUser repositoryUser;
    private final RepositoryAccount repositoryAccount;
    private final CurrencyRepository currencyRepository;

    public AdminServiceImpl(RepositoryUser repositoryUser, RepositoryAccount repositoryAccount, RepositoryOperation repositoryOperation) {
        this.repositoryUser = repositoryUser;
        this.repositoryAccount = repositoryAccount;
        this.repositoryOperation = repositoryOperation;
        currencyRepository = new CurrencyRepository();
    }

    @Override
    public List<Operation> getAllOperation() {
        return repositoryOperation.deserialization();
    }

    @Override
    public List<Operation> getOperationUsers(long id) {
        return repositoryOperation.deserialization().stream().filter(operation -> operation.getClientIdTo() == id || operation.getClientIdFrom() == id).collect(Collectors.toList());
    }

    @Override
    public List<Users> getAllUsers() {
        return repositoryUser.deserialization();
    }

    @Override
    public List<Accounts> getAllAccounts() {
        return repositoryAccount.deserialization();
    }

    @Override
    public void createUsers(String login, String password, String name, String surName) {
        Users users = new Users(login, password, name, surName);
        users.setRole(Role.CLIENT);
        repositoryUser.serialization(users);
    }

    @Override
    public void deleteUsers(long id) {
        List<Users> list = repositoryUser.deserialization();
        list.removeIf(users -> users.getId() == id);
        deleteAccountUsers(id);
        deleteOperationUsers(id);
        repositoryUser.updateAccount();
    }

    private void deleteOperationUsers(long id) {
        changeClientIdTo(id);
        changeClientIdFrom(id);
        List<Operation> operations = repositoryOperation.deserialization();
        operations.removeIf(operation -> operation.getClientIdTo() == -1 && operation.getClientIdFrom() == -1);
        repositoryOperation.uploadOperation();
    }

    private void changeClientIdTo(long id) {
        List<Operation> operations = repositoryOperation.deserialization();
        operations.stream().filter(operation -> operation.getClientIdTo() == id).forEach(operation -> {
            operation.setClientIdTo(-1);
            operation.setClientIdAccountTo((long) -1);
        });
        repositoryOperation.uploadOperation();
    }

    private void changeClientIdFrom(long id) {
        List<Operation> operations = repositoryOperation.deserialization();
        operations.stream().filter(operation -> operation.getClientIdFrom() == id).forEach(operation -> {
            operation.setClientIdFrom(-1);
            operation.setClientIdAccountFrom((long) -1);
        });
        repositoryOperation.uploadOperation();
    }

    private void deleteAccountUsers(long id) {
        List<Accounts> accounts = repositoryAccount.deserialization();
        accounts.removeIf(accounts1 -> accounts1.getUserId() == id);
        repositoryAccount.updateAccount();
    }

    @Override
    public void changeLogin(String login, long usersId) throws ExceptionUsers {
        Users users = repositoryUser.getUsers(usersId);
        users.setLogin(login);
        repositoryUser.updateAccount();
    }

    @Override
    public void changePassword(String password, long usersId) throws ExceptionUsers {
        Users users = repositoryUser.getUsers(usersId);
        users.setPassword(password);
        repositoryUser.updateAccount();
    }

    @Override
    public Accounts createAccount(long accountNumber, String currency, long userId) throws ExceptionOperation {
        List<Accounts> list = repositoryAccount.deserialization().stream().filter(accounts -> accounts.getUserId() == userId).toList();
        if (list.size() > 0) {
            throw new ExceptionOperation("У банка уже есть счет для коммисий");
        }
        Accounts accounts = new Accounts(accountNumber, currency, userId);
        repositoryAccount.serialization(accounts);
        return accounts;
    }

    @Override
    public Accounts getCommissionsAccount(long userId) throws ExceptionOperation {
        Optional<Accounts> optionalAccounts = repositoryAccount.deserialization().stream().filter(accounts -> accounts.getUserId() == userId).findAny();
        if (optionalAccounts.isEmpty()) {
            throw new ExceptionOperation("Аккаунт с коммисиями не создан");
        }
        return optionalAccounts.get();
    }

    @Override
    public List<Operation> getOperationNotPayCommissions() {
        return repositoryOperation.deserialization().stream().filter(operation -> !operation.getPayCommission()).toList();
    }

    @Override
    public void payCommissions(Accounts accounts) {
        List<Operation> list = repositoryOperation.deserialization().stream().filter(operation -> !operation.getPayCommission()).toList();
        for (Operation operation : list) {
            operation.setPayCommission(true);
            accounts.setValue(accounts.getValue() + operation.getValueCommission());
        }
        repositoryOperation.uploadOperation();
        repositoryAccount.updateAccount();
    }

    @Override
    public double getValueCommissions() {
        List<Operation> list = repositoryOperation.deserialization().stream().filter(operation -> !operation.getPayCommission()).toList();
        double value = 0;
        for (Operation operation : list) {
            value += operation.getValueCommission();
        }
        return value;
    }

    @Override
    public void changeCommissions(double valueCommissions) {
        String[] currency = CurrencySetting.splittingString(currencyRepository.deserializationCurrency(), "commission=");
        currency[1] = "commission=" + valueCommissions;
        Currency.commission = valueCommissions;
        currencyRepository.updateCommission(currency[0] + currency[1]);
    }

    @Override
    public void getInformationUser(long id) throws ExceptionUsers {
        System.out.println(repositoryUser.getUsers(id));
        System.out.println(repositoryAccount.getAccountsToUser(id));
    }

    @Override
    public List<List<Operation>> getListOperation() {
        List<Operation> list = repositoryOperation.deserialization();
        List<List<Operation>> listList = Lists.partition(list, 4);
        return listList;
    }

    @Override
    public List<List<Users>> getListUsers() {
        List<Users> list = repositoryUser.deserialization();
        List<List<Users>> listList = Lists.partition(list, 4);
        return listList;
    }
}
