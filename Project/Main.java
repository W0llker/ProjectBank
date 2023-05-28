package Project;

import Project.StartMenu.CurrencySetting;
import Project.StartMenu.Start;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new CurrencySetting(), 0,10000);
        Start.menu();
    }
}
