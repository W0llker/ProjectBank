package Project.Class;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

public class Operation implements Serializable {
    @Serial
    private static final long serialVersionUID = 4658676928757523012L;
    private Long id;
    private Integer clientIdTo;
    private String clientToName;
    private String clientToSurname;
    private Long clientIdAccountTo;
    private Long clientToNumberAccount;
    private Integer clientIdFrom;
    private String clientFromName;
    private String clientFromSurname;
    private Long clientIdAccountFrom;
    private Long clientFromNumberAccount;
    private String currency;
    private Double value;
    private LocalDate localDate;
    private Double valueCommission;
    private Boolean isPayCommission;

    public Operation(Integer clientIdTo, String clientToName, String clientToSurname, Long clientToAccount, Long clientIdAccountTo,
                     Integer clientIdFrom, String clientFromName, String clientFromSurname, Long clientIdAccountFrom, Long clientFromAccount, Double value, String currency) {
        this.clientIdTo = clientIdTo;
        this.clientToName = clientToName;
        this.clientToSurname = clientToSurname;
        this.clientToNumberAccount = clientToAccount;
        this.clientIdAccountTo = clientIdAccountTo;
        this.clientIdFrom = clientIdFrom;
        this.clientFromName = clientFromName;
        this.clientFromSurname = clientFromSurname;
        this.clientIdAccountFrom = clientIdAccountFrom;
        this.clientFromNumberAccount = clientFromAccount;
        this.localDate = LocalDate.now();
        this.currency = currency;
        this.valueCommission = convertCommissionToBYN((Currency.commission * value) / 100);
        this.value = value - ((Currency.commission * value) / 100);
        this.isPayCommission = false;
    }

    private double convertCommissionToBYN(double value) {
        double curs = 1;
        if (!currency.equals("BYN")) {
            curs = Currency.currency.get(currency + "/BYN");
        }
        return value * curs;
    }

    public Double getValueCommission() {
        return valueCommission;
    }

    public Long getClientIdAccountTo() {
        return clientIdAccountTo;
    }

    public void setClientIdAccountTo(Long clientIdAccountTo) {
        this.clientIdAccountTo = clientIdAccountTo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setValueCommission(Double valueCommission) {
        this.valueCommission = valueCommission;
    }

    public Boolean getPayCommission() {
        return isPayCommission;
    }

    public void setPayCommission(Boolean payCommission) {
        isPayCommission = payCommission;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getClientIdTo() {
        return clientIdTo;
    }

    public void setClientIdTo(Integer clientIdTo) {
        this.clientIdTo = clientIdTo;
    }

    public String getClientToName() {
        return clientToName;
    }

    public void setClientToName(String clientToName) {
        this.clientToName = clientToName;
    }

    public String getClientToSurname() {
        return clientToSurname;
    }

    public void setClientToSurname(String clientToSurname) {
        this.clientToSurname = clientToSurname;
    }

    public Long getClientToNumberAccount() {
        return clientToNumberAccount;
    }

    public void setClientToNumberAccount(Long clientToNumberAccount) {
        this.clientToNumberAccount = clientToNumberAccount;
    }

    public int getClientIdFrom() {
        return clientIdFrom;
    }

    public void setClientIdFrom(int clientIdFrom) {
        this.clientIdFrom = clientIdFrom;
    }

    public String getClientFromName() {
        return clientFromName;
    }

    public void setClientFromName(String clientFromName) {
        this.clientFromName = clientFromName;
    }

    public String getClientFromSurname() {
        return clientFromSurname;
    }

    public void setClientFromSurname(String clientFromSurname) {
        this.clientFromSurname = clientFromSurname;
    }

    public Long getClientIdAccountFrom() {
        return clientIdAccountFrom;
    }

    public void setClientIdAccountFrom(Long clientIdAccountFrom) {
        this.clientIdAccountFrom = clientIdAccountFrom;
    }

    public Long getClientFromNumberAccount() {
        return clientFromNumberAccount;
    }

    public void setClientFromNumberAccount(Long clientFromNumberAccount) {
        this.clientFromNumberAccount = clientFromNumberAccount;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", clientIdTo=" + clientIdTo +
                ", clientToName='" + clientToName + '\'' +
                ", clientToSurname='" + clientToSurname + '\'' +
                ", clientIdAccountTo=" + clientIdAccountTo +
                ", clientToNumberAccount=" + clientToNumberAccount +
                ", clientIdFrom=" + clientIdFrom +
                ", clientFromName='" + clientFromName + '\'' +
                ", clientFromSurname='" + clientFromSurname + '\'' +
                ", clientIdAccountFrom=" + clientIdAccountFrom +
                ", clientFromNumberAccount=" + clientFromNumberAccount +
                ", currency='" + currency + '\'' +
                ", value=" + value +
                ", localDate=" + localDate +
                ", valueCommission=" + valueCommission +
                ", isPayCommission=" + isPayCommission +
                "\n}";
    }
}
