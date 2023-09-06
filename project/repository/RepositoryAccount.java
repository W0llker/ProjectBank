package project.repository;

import project.entity.Accounts;
import project.exception.ExceptionOperation;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepositoryAccount {
    private final static File file = new File("resources/account");
    private static List<Accounts> accountsList;

    public RepositoryAccount() {
        accountsList = new ArrayList<>();
    }

    public void serialization(Accounts account) {
        accountsList = deserialization();
        Optional<Accounts> optionalId;
        if (accountsList.size() > 0) {
            optionalId = accountsList.stream().max(Comparator.comparingLong(Accounts::getId));
            account.setId(optionalId.get().getId() + 1);
        } else account.setId(0);
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            accountsList.add(account);
            stream.writeObject(accountsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Accounts> deserialization() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            accountsList = (List<Accounts>) stream.readObject();
        } catch (Exception e) {
        }
        return accountsList;
    }

    public List<Accounts> getAccountsToUser(long userId) {
        List<Accounts> result = new ArrayList<>();
        for (Accounts accounts : deserialization()) {
            if (accounts.getUserId() == userId) {
                result.add(accounts);
            }
        }
        return result;
    }

    public Accounts getAccountToUser(long id, long userId) throws ExceptionOperation {
        Optional<Accounts> accountsStream = accountsList.stream().filter(accounts -> accounts.getId() == id && accounts.getUserId() == userId).findAny();
        if (accountsStream.isEmpty()) {
            throw new ExceptionOperation("У данного пользователя нету такого счета");
        }
        return accountsStream.get();
    }

    public void updateAccount() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            stream.writeObject(accountsList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
