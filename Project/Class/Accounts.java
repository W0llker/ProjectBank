package Project.Class;

import Project.repository.RepositoryAccount;

import java.io.Serial;
import java.io.Serializable;

public class Accounts implements Serializable {
    @Serial
    private static final long serialVersionUID = 2715250613032999126L;
    private long id;
    private long numberAccounts;
    private String currency;
    private double value;
    private long userId;

    public Accounts(long numberAccounts, String currency, long userId) {
        this.numberAccounts = numberAccounts;
        this.currency = currency;
        this.userId = userId;
    }
    public String accountInformation() {
        return "id:" + id + " номер:" + numberAccounts + " валюта:" +currency;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumberAccounts() {
        return numberAccounts;
    }

    public void setNumberAccounts(long numberAccounts) {
        this.numberAccounts = numberAccounts;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "id=" + id +
                ", numberAccounts=" + numberAccounts +
                ", currency='" + currency + '\'' +
                ", value=" + value +
                ", userId=" + userId +
                "\n}";
    }
}
