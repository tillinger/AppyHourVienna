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
                happy.setDaysOfWeek(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
                happy.setDescriptionHappy("Beer: 3,5€\nCocktails: 1,5€");
                happy.setDescriptionBar("This Bar is a very nice location for all kind of people!\nMeet with your friends!\nSee you");
                happy.setStartDate(new Date().getTime());
                happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
                happy.setPrice(0.5*i);
                happy.setEndTime(60*60*17);
                happy.setEndTime(60 * 60 * 22);
                happy.setType(HappyHourType.BEER);

                Address address = new Address("Regengasse " + i, 1010);
                happy.setAddress(address);

                this.dbHappies.add(happy);
            }
        } else { // load all happy hours!
            //simulating loading ALL happy hours from db
            this.dbHappies = new ArrayList<HappyHour>();
            for(int i = 0; i < 10; i++) {
                HappyHour happy = new HappyHour();
                happy.setName("Loco-Bar " + i);
                happy.setDaysOfWeek(EnumSet.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));
                happy.setDescriptionHappy("Beer: 3,5€\nCocktails: 1,5€");
                happy.setDescriptionBar("This Bar is a very nice location for all kind of people!\nMeet with your friends!\nSee you");
                happy.setStartDate(new Date().getTime());
                happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
                happy.setPrice(0.5*i);
                happy.setEndTime(60*60*17);
                happy.setEndTime(60 * 60 * 22);
                happy.setType(HappyHourType.BEER);

                Address address = new Address("Regengasse " + i, 1010);
                happy.setAddress(address);

                this.dbHappies.add(happy);
            }
        }

        return true;
    }
}
