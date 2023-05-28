package Project.Service.impl;

import Project.Class.Accounts;
import Project.Class.Currency;
import Project.Class.Operation;
import Project.Class.Users;
import Project.Exception.ExceptionExit;
import Project.Exception.ExceptionUsers;
import Project.Service.ClientService;
import Project.Validator.AccountValidator;
import Project.repository.RepositoryAccount;
import Project.repository.RepositoryOperation;
import Project.repository.RepositoryUser;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class ClientServiceImpl implements ClientService {
    private final RepositoryOperation repositoryOperation;
    private final RepositoryAccount repositoryAccount;
    private final RepositoryUser repositoryUser;

    public ClientServiceImpl(RepositoryAccount repositoryAccount, RepositoryOperation repositoryOperation, RepositoryUser repositoryUser) {
        this.repositoryOperation = repositoryOperation;
        this.repositoryAccount = repositoryAccount;
        this.repositoryUser = repositoryUser;
    }

    @Override
    public Accounts addAccounts(long accountNumber, String currency, long userId) throws ExceptionUsers {
        AccountValidator.checkCurrency(currency);
        Accounts accounts = new Accounts(accountNumber, currency, userId);
        repositoryAccount.serialization(accounts);
        return accounts;
    }

    @Override
    public void deleteAccounts(long id, long usersId) throws ExceptionUsers {
        List<Accounts> list = repositoryAccount.deserialization();
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).getUserId() == usersId) && (list.get(i).getId() == id) && (list.get(i).getValue() == 0)) {
                list.remove(i);
            } else if ((list.get(i).getUserId() == usersId) && (list.get(i).getId() == id) && (list.get(i).getValue() != 0)) {
                throw new ExceptionUsers("На счету есть деньги. Необходимо перекинуть на другой счет");
            }
        }
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
    public void payment(Accounts accounts, double value, Users users) {
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
    public void moneyTransfer(long accountsTo, long accountsFrom, double value) throws ExceptionExit {
        Accounts accountTo = sendingMoney(accountsTo, value);
        Accounts accountFrom = getMoney(accountsFrom, value, accountTo.getCurrency());
        Users usersTo = repositoryUser.getUsers(accountTo.getUserId());
        Users usersFrom = repositoryUser.getUsers(accountFrom.getUserId());
        Operation operation = new Operation(usersTo.getId(), usersTo.getName(), usersTo.getSurName(),
                accountTo.getNumberAccounts(), accountTo.getId(), usersFrom.getId(), usersFrom.getName(),
                usersFrom.getSurName(), accountFrom.getId(), accountFrom.getNumberAccounts(), value, accountTo.getCurrency());
        repositoryOperation.serialization(operation);
    }

    private Accounts sendingMoney(long accountsTo, double value) throws ExceptionUsers {
        for (Accounts accounts : repositoryAccount.deserialization()) {
            if (accounts.getId() == accountsTo && accounts.getValue() >= value) {
                accounts.setValue(accounts.getValue() - value);
                repositoryAccount.updateAccount();
                return accounts;
            } else if (accounts.getId() == accountsTo && accounts.getValue() < value) {
                throw new ExceptionUsers("Денежных средст недостаточно для перевода");
            }
        }
        throw new ExceptionUsers("Счет не найден");
    }
    public List<Operation> translationHistory(long id, LocalDate localDate) {
        List<Operation> operations = repositoryOperation.deserialization();
        return operations.stream().filter(operation -> (operation.getClientIdAccountFrom() == id && operation.getLocalDate().isAfter(localDate)) || (operation.getClientIdAccountTo() == id && operation.getLocalDate().isAfter(localDate))).collect(Collectors.toList());
    }

    private Accounts getMoney(long accountsFrom, double value, String currency) throws ExceptionUsers {
        double curs = 1;
        for (Accounts accounts : repositoryAccount.deserialization()) {
            if (accounts.getId() == accountsFrom) {
                if (!currency.equals(accounts.getCurrency())) {
                    curs = Currency.currency.get(currency + "/" + accounts.getCurrency());
                }
                accounts.setValue(accounts.getValue() + curs * (value - (Currency.commission * value) / 100));
                repositoryAccount.updateAccount();
                return accounts;
            }
        }
        throw new ExceptionUsers("Счет не найден");
    }
}
