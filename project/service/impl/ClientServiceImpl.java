package project.service.impl;

import project.entity.Accounts;
import project.entity.Currency;
import project.entity.Operation;
import project.entity.Users;
import project.exception.ExceptionOperation;
import project.exception.ExceptionUsers;
import project.repository.CurrencyRepository;
import project.repository.RepositoryAccount;
import project.repository.RepositoryOperation;
import project.repository.RepositoryUser;
import project.service.ClientService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class ClientServiceImpl implements ClientService {
    private final RepositoryOperation repositoryOperation;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryUser repositoryUser;
    private final CurrencyRepository currencyRepository;

    public ClientServiceImpl(RepositoryAccount repositoryAccount, RepositoryOperation repositoryOperation, RepositoryUser repositoryUser) {
        this.repositoryOperation = repositoryOperation;
        this.repositoryAccount = repositoryAccount;
        this.repositoryUser = repositoryUser;
        this.currencyRepository = new CurrencyRepository();
    }

    @Override
    public Accounts addAccounts(long accountNumber, String currency, long userId) throws ExceptionUsers {
        Accounts accounts = new Accounts(accountNumber, currency, userId);
        repositoryAccount.serialization(accounts);
        return accounts;
    }

    @Override
    public void deleteAccounts(long id, long usersId) throws ExceptionOperation {
        List<Accounts> list = repositoryAccount.deserialization();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getId() == id) && (list.get(i).getValue() == 0)) {
                list.remove(i);
                deleteOperationUsers(id);
            } else if ((list.get(i).getId() == id) && (list.get(i).getValue() != 0)) {
                throw new ExceptionOperation("На счету есть деньги. Необходимо перекинуть на другой счет");
            }
        }
        repositoryAccount.updateAccount();
    }

    private void deleteOperationUsers(long id) {
        changeClientIdTo(id);
        changeClientIdFrom(id);
        List<Operation> operations = repositoryOperation.deserialization();
        operations.removeIf(operation -> operation.getClientIdAccountTo() == -1 && operation.getClientIdAccountFrom() == -1);
        repositoryOperation.uploadOperation();
    }

    private void changeClientIdTo(long id) {
        List<Operation> operations = repositoryOperation.deserialization();
        operations.stream().filter(operation -> operation.getClientIdAccountTo() == id).forEach(operation -> operation.setClientIdAccountTo((long) -1));
        repositoryOperation.uploadOperation();
    }

    private void changeClientIdFrom(long id) {
        List<Operation> operations = repositoryOperation.deserialization();
        operations.stream().filter(operation -> operation.getClientIdAccountFrom() == id).forEach(operation -> operation.setClientIdAccountFrom((long) -1));
        repositoryOperation.uploadOperation();
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
    public double convertValueTOBYN(List<Accounts> accounts) {
        double valueBYN = 0;
        for (Accounts accounts1 : accounts) {
            double curs = 1;
            if (!accounts1.getCurrency().equals("BYN")) {
                curs = Currency.currency.get(accounts1.getCurrency() + "/BYN");
            }
            System.out.println(accounts1);
            valueBYN += accounts1.getValue() * curs;
        }
        return valueBYN;
    }

    @Override
    public void payment(long id, double value, Users users) throws ExceptionOperation {
        Accounts accounts = repositoryAccount.getAccountToUser(id, users.getId());
        double curs = 1;
        Operation operation = new Operation(users.getId(), users.getName(), users.getSurName(), accounts.getNumberAccounts(), accounts.getId(),
                users.getId(), users.getName(), users.getSurName(), accounts.getId(), accounts.getNumberAccounts(), value, "BYN");
        if (!accounts.getCurrency().equals("BYN")) {
            curs = Currency.currency.get("BYN/" + accounts.getCurrency());
        }
        accounts.setValue(accounts.getValue() + curs * (value - operation.getValueCommission()));
        repositoryOperation.serialization(operation);
        repositoryAccount.updateAccount();
    }

    @Override
    public String getCurrencyAll() {
        return currencyRepository.deserializationCurrency();
    }

    @Override
    public void moneyTransfer(long accountsTo, long accountsFrom, double value) throws ExceptionUsers, ExceptionOperation {
        Accounts accountTo = sendingMoney(accountsTo, value);
        Accounts accountFrom = getMoney(accountsFrom, value, accountTo.getCurrency());
        Users usersTo = repositoryUser.getUsers(accountTo.getUserId());
        Users usersFrom = repositoryUser.getUsers(accountFrom.getUserId());
        Operation operation = new Operation(usersTo.getId(), usersTo.getName(), usersTo.getSurName(),
                accountTo.getNumberAccounts(), accountTo.getId(), usersFrom.getId(), usersFrom.getName(),
                usersFrom.getSurName(), accountFrom.getId(), accountFrom.getNumberAccounts(), value, accountTo.getCurrency());
        repositoryOperation.serialization(operation);
    }

    private Accounts sendingMoney(long accountsTo, double value) throws ExceptionOperation {
        Accounts account = null;
        for (Accounts accounts : repositoryAccount.deserialization()) {
            if (accounts.getId() == accountsTo && accounts.getValue() >= value) {
                accounts.setValue(accounts.getValue() - value);
                repositoryAccount.updateAccount();
                account = accounts;
            } else if (accounts.getId() == accountsTo && accounts.getValue() < value) {
                throw new ExceptionOperation("Денежных средст недостаточно для перевода");
            }
        }
        return account;
    }

    public List<Operation> translationHistory(long id, LocalDate localDate) {
        List<Operation> operations = repositoryOperation.deserialization();
        return operations.stream().filter(operation -> (operation.getClientIdAccountFrom() == id && operation.getLocalDate().isAfter(localDate)) || (operation.getClientIdAccountTo() == id && operation.getLocalDate().isAfter(localDate))).collect(Collectors.toList());
    }

    @Override
    public List<Accounts> getAccountsToUser(long userId) {
        return repositoryAccount.getAccountsToUser(userId);
    }

    private Accounts getMoney(long accountsFrom, double value, String currency) {
        double curs = 1;
        Accounts account = null;
        for (Accounts accounts : repositoryAccount.deserialization()) {
            if (accounts.getId() == accountsFrom) {
                if (!currency.equals(accounts.getCurrency())) {
                    curs = Currency.currency.get(currency + "/" + accounts.getCurrency());
                }
                accounts.setValue(accounts.getValue() + curs * (value - (Currency.commission * value) / 100));
                repositoryAccount.updateAccount();
                account = accounts;
            }
        }
        return account;
    }
}
