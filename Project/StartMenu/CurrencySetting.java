package Project.StartMenu;


import Project.Class.Currency;
import Project.repository.CurrencyRepository;

import java.util.TimerTask;

public class CurrencySetting extends TimerTask {
    private final CurrencyRepository currencyRepository;
    public CurrencySetting() {
        currencyRepository = new CurrencyRepository();
        String[] com = currencyRepository.splittingString(currencyRepository.deserializationCurrency(),"commission=");
        Currency.commission = Double.parseDouble(com[1]);
    }

    @Override
    public void run() {
        String[] currency = currencyRepository.splittingString(currencyRepository.deserializationCurrency(), ";");
        for (int i = 0; i < currency.length; i++) {
            String[] splitValueCurrency = currencyRepository.splittingString(currency[i], "=");
            Currency.currency.put(splitValueCurrency[0], Double.valueOf(splitValueCurrency[1]));
        }
    }
}
