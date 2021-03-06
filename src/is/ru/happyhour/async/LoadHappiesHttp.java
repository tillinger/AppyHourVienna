package is.ru.happyhour.async;

import is.ru.happyhour.HappyListFragment;
import android.os.AsyncTask;
import is.ru.happyhour.db.HappyDataSource;
import is.ru.happyhour.model.Address;
import is.ru.happyhour.model.DayOfWeek;
import is.ru.happyhour.model.HappyHour;
import is.ru.happyhour.model.HappyHourType;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Random;

public class LoadHappiesHttp extends AsyncTask<Void, Void, Boolean> {

    private HappyListFragment frag;

    public LoadHappiesHttp(HappyListFragment frag) {
        this.frag = frag;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(!isCancelled()) {
            System.out.println("download success: " + success);
            frag.downloadFinished(success);
        } else {
            System.out.println("Asnyctask was cancelled");
        }
    }

    protected Boolean doInBackground(Void... args) {
        System.out.println("doInBackground downloading happies!");

        //Simulation of downloading happy hours
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        //here save the happy hours to the database
        HappyDataSource dataSource = new HappyDataSource(frag.getActivity());
        dataSource.insertHappyHours(generateHappies());

        return true;

    }

    private ArrayList<HappyHour> generateHappies() {

        ArrayList<HappyHour> happies = new ArrayList<HappyHour>();

        for(int i = 0; i < 5; i++) {
            HappyHour happy = new HappyHour();
            happy.setId(i);
            happy.setBarName("Loco-Bar " + i);
            happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Monday, DayOfWeek.Tuesday, DayOfWeek.Wednesday));
            happy.setDescriptionHappy("description of happyhour");
            happy.setDescriptionBar("Not only a bar but also an art lounge. Locobar provides a strong artist environment with blending of art and architecture. There are trendy lounge music, creative performances and a breathtaking view of the Huangpi River flowing in front of the splendid Pudong skyline.");
            happy.setStartDate(new Date().getTime());
            happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
            happy.setPrice(0.5 * (new Random().nextInt(10)) + 2);
            happy.setStartTime(60 * 60 * 12);
            happy.setEndTime((int) (60 * 60 * 23.9));
            happy.setType(HappyHourType.BEER);

            Address address = new Address("Währinger Gürtel Stadtbahnbogen 172-174 ", 1090);
            address.setLatitude(48.231262);
            address.setLongitude(16.351871);
            happy.setAddress(address);

            happies.add(happy);
        }

        for(int i = 5; i < 15; i++) {
            HappyHour happy = new HappyHour();
            happy.setId(i);
            happy.setBarName("Restaurant am Stephansdom " + i);
            happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Wednesday, DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Sunday));
            happy.setDescriptionHappy("description of happyhour");
            happy.setDescriptionBar("Not only a bar but also an art lounge. Stephansdom Bar provides a strong artist environment with blending of art and architecture. There are trendy lounge music, creative performances and a breathtaking view of the Huangpi River flowing in front of the splendid Pudong skyline.");
            happy.setStartDate(new Date().getTime());
            happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 7); //one week
            happy.setPrice(0.5 * (new Random().nextInt(10)) + 2);
            happy.setStartTime(60 * 60 * 0);
            happy.setEndTime(60 * 60 * 12);
            happy.setType(HappyHourType.BEER);

            Address address = new Address("Baumgasse 13/1/8", 1090);
            address.setLatitude(48.208807);
            address.setLongitude(16.372103);

            happy.setAddress(address);

            happies.add(happy);
        }
        for(int i = 15; i < 25; i++) {
            HappyHour happy = new HappyHour();
            happy.setId(i);
            happy.setBarName("Praterdome " + i);
            happy.setDaysOfWeek(EnumSet.of(DayOfWeek.Friday, DayOfWeek.Saturday, DayOfWeek.Wednesday));
            happy.setDescriptionHappy("description of happyhour");
            happy.setDescriptionBar("Im Prater Dome erlebt Ihr die Inszenierung des Augenblicks!\n" +
                    "\n" +
                    "Öffnungszeiten\n" +
                    "Donnerstag, Freitag, Samstag & vor gesetzlichen Feiertagen ab 22 Uhr.\n" +
                    "\n" +
                    "Dresscode:\n" +
                    "Dress to impress!\n" +
                    "Gesonderte Dresscodes bei bestimmten Events möglich.");
            happy.setStartDate(new Date().getTime());
            happy.setEndDate(new Date().getTime() + 60 * 60 * 24 * 1000 * 70); //70 days
            happy.setPrice(0.5 * (new Random().nextInt(10)) + 2);
            happy.setStartTime(60 * 60 * 14);
            happy.setEndTime(60 * 60 * 23);
            happy.setType(HappyHourType.BEER);

            Address address = new Address("Prateralle 22 - Tür 108", 1030);
            address.setLatitude(48.217496);
            address.setLongitude(16.396805);

            happy.setAddress(address);

            happies.add(happy);
        }


        return happies;
    }
}