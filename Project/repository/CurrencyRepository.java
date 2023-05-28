package Project.repository;

import java.io.*;

public class CurrencyRepository {
    private final static File file = new File("resources/currencies");
    private static String currency;
    public String deserializationCurrency() {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            currency = bufferedReader.readLine();
        }catch (Exception e) {
           e.printStackTrace();
        }
        return currency;
    }
    public void updateCommission(String commission) {
        currency = commission;
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){
            bufferedWriter.write(currency);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String[] splittingString(String currency, String regex) {
        return currency.split(regex);
    }
}
