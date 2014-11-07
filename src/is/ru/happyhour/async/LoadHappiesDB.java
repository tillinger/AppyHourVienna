package is.ru.happyhour.async;

import android.os.AsyncTask;
import is.ru.happyhour.HappyListFragment;
import is.ru.happyhour.db.HappyDataSource;
import is.ru.happyhour.model.Address;
import is.ru.happyhour.model.DayOfWeek;
import is.ru.happyhour.model.HappyHour;
import is.ru.happyhour.model.HappyHourType;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

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

        HappyDataSource dataSource = new HappyDataSource(frag.getActivity());
        dbHappies = dataSource.getAllHappies();
        return true;
    }
}
