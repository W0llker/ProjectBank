package project.startMenu;


import project.entity.Currency;
import project.repository.CurrencyRepository;

import java.util.TimerTask;

public class CurrencySetting extends TimerTask {
    private final CurrencyRepository currencyRepository;

    public CurrencySetting() {
        currencyRepository = new CurrencyRepository();
        String[] com = splittingString(currencyRepository.deserializationCurrency(), "commission=");
        Currency.commission = Double.parseDouble(com[1]);
    }

    @Override
    public void run() {
        String[] currency = splittingString(currencyRepository.deserializationCurrency(), ";");
        for (int i = 0; i < currency.length; i++) {
            String[] splitValueCurrency = splittingString(currency[i], "=");
            Currency.currency.put(splitValueCurrency[0], Double.valueOf(splitValueCurrency[1]));
        }
    }

    public static String[] splittingString(String currency, String regex) {
        return currency.split(regex);
    }
}
