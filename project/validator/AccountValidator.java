package project.validator;



import project.entity.Accounts;
import project.entity.Currency;
import project.exception.ExceptionUsers;
import project.repository.RepositoryAccount;

import java.util.List;
import java.util.Optional;

public class AccountValidator {
    private final RepositoryAccount repositoryAccount = new RepositoryAccount();
    public void checkCurrency(String currency) throws ExceptionUsers {
        for (String s : Currency.name) {
            if(s.equals(currency)){
                return;
            }
        }
        throw new ExceptionUsers("Данной валюты нету");
    }
    public void checkAccountNumber(long accountNumber) throws ExceptionUsers {
        for (Accounts accounts : repositoryAccount.deserialization()) {
            if(accounts.getNumberAccounts() == accountNumber) {
                throw new ExceptionUsers("Номер аккаунта уже существует");
            }
        }
    }
    public void checkAccountTheUsers(long id, long userId) throws ExceptionUsers {
        Optional<Accounts> accountsStream = repositoryAccount.deserialization().stream().filter(accounts -> accounts.getId() == id && accounts.getUserId() == userId).findAny();
        if (accountsStream.isEmpty()) {
            throw new ExceptionUsers("У данного пользователя нету такого счета");
        }
    }
    public Accounts checkAccountNotTheUsers(long idAccount, long userId) throws ExceptionUsers {
        List<Accounts> accountsList = repositoryAccount.deserialization();
        for (Accounts accounts : accountsList) {
            if (accounts.getId() == idAccount && accounts.getUserId() == userId) throw new ExceptionUsers("Нельзя отправить денежные средства на свой счет");
            if (accounts.getId() == idAccount && accounts.getUserId() != userId) return accounts;
        }
        throw new ExceptionUsers("Данного аккаунта нету в банке");
    }
}
