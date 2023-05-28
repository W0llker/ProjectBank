package Project.Validator;

import Project.Class.Currency;
import Project.Exception.ExceptionUsers;

public class AccountValidator {
    public static void checkCurrency(String currency) throws ExceptionUsers {
        for (String s : Currency.name) {
            if(s.equals(currency)){
                return;
            }
        }
        throw new ExceptionUsers("Данной валюты нету");
    }
    public static void checkAccountNumber(long accountNumber) {

    }
}
