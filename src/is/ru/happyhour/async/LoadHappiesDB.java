package is.ru.happyhour.async;

import android.os.AsyncTask;
import is.ru.happyhour.HappyListFragment;
import is.ru.happyhour.model.Address;
import is.ru.happyhour.model.DayOfWeek;
import is.ru.happyhour.model.HappyHour;
import is.ru.happyhour.model.HappyHourType;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

public class LoadHappiesDB extends AsyncTask<Integer, Void, Boolean> {

    private HappyListFragment frag;
    ArrayList<HappyHour> dbHappies;

    public LoadHappiesDB(HappyListFragment frag) {
        this.frag = frag;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        System.out.println("Happies load from DB success: " + success);

        if(success) {
            frag.loadedFromDBSuccess(this.dbHappies);
        } else {
            frag.loadedFromDatabaseError();
        }
    }

    protected Boolean doInBackground(Integer... args) {
        System.out.println("loading happy hours from database: " + args[0]);

        int nowOrAll = args[0];

        //TODO here call methods to get happy hours from database!


        if(nowOrAll == 0) { //0 == now = load the current happy hours
            //simulating loading now from db
            this.dbHappies = new ArrayList<HappyHour>();
            for(int i = 0; i < 5; i++) {
                HappyHour happy = new HappyHour();
                happy.setName("Loco-Bar " + i);
                happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Saturday, DayOfWeek.Sunday));
                happy.setDescriptionHappy("Beer: 3,5€\nCocktails: 1,5€");
                happy.setDescriptionBar("Not only a bar but also an art lounge. L18 provides a strong artist environment with blending of art and architecture. There are trendy lounge music, creative performances and a breathtaking view of the Huangpi River flowing in front of the splendid Pudong skyline.");
                happy.setStartDate(new Date().getTime());
                happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
                happy.setPrice(0.5*i + 2);
                happy.setStartTime(60*60*17);
                happy.setEndTime(60 * 60 * 22);
                happy.setType(HappyHourType.BEER);

                Address address = new Address("Währinger Gürtel Stadtbahnbogen 172-174 ", 1090);
                address.setLatitude(48.231262);
                address.setLongitude(16.351871);
                happy.setAddress(address);

                this.dbHappies.add(happy);
            }
        } else if (nowOrAll == 1) { // load all happy hours!
            //simulating loading ALL happy hours from db
            this.dbHappies = new ArrayList<HappyHour>();
            for(int i = 0; i < 10; i++) {
                HappyHour happy = new HappyHour();
                happy.setName("Loco-Bar " + i);
                happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Saturday, DayOfWeek.Sunday));
                happy.setDescriptionHappy("Beer: 3,5€\nCocktails: 1,5€");
                happy.setDescriptionBar("Not only a bar but also an art lounge. L18 provides a strong artist environment with blending of art and architecture. There are trendy lounge music, creative performances and a breathtaking view of the Huangpi River flowing in front of the splendid Pudong skyline.");
                happy.setStartDate(new Date().getTime());
                happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
                happy.setPrice(0.5*i + 2);
                happy.setStartTime(60*60*17);
                happy.setEndTime(60 * 60 * 22);
                happy.setType(HappyHourType.BEER);

                Address address = new Address("Währinger Gürtel Stadtbahnbogen 172-174 ", 1090);
                address.setLatitude(48.231262);
                address.setLongitude(16.351871);
                happy.setAddress(address);

                this.dbHappies.add(happy);
            }
        } else if (nowOrAll == 2) { // load all happy hours!
            //simulating loading ALL happy hours from db
            this.dbHappies = new ArrayList<HappyHour>();
            for(int i = 10; i > 0; i--) {
                HappyHour happy = new HappyHour();
                happy.setName("Loco-Bar " + i);
                happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday, DayOfWeek.Saturday, DayOfWeek.Sunday));
                happy.setDescriptionHappy("Beer: 3,5€\nCocktails: 1,5€");
                happy.setDescriptionBar("Not only a bar but also an art lounge. L18 provides a strong artist environment with blending of art and architecture. There are trendy lounge music, creative performances and a breathtaking view of the Huangpi River flowing in front of the splendid Pudong skyline.");
                happy.setStartDate(new Date().getTime());
                happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
                happy.setPrice(0.5*i + 2);
                happy.setStartTime(60*60*17);
                happy.setEndTime(60 * 60 * 22);
                happy.setType(HappyHourType.BEER);

                Address address = new Address("Währinger Gürtel Stadtbahnbogen 172-174 ", 1090);
                address.setLatitude(48.231262);
                address.setLongitude(16.351871);
                happy.setAddress(address);

                this.dbHappies.add(happy);
            }
        }

        return true;
    }
}
