package is.ru.happyhour;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import is.ru.happyhour.adapter.HappyListAdapter;
import is.ru.happyhour.model.Address;
import is.ru.happyhour.model.DayOfWeek;
import is.ru.happyhour.model.HappyHour;
import is.ru.happyhour.model.HappyHourType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class HappyListFragment extends ListFragment {

    private OnHappyHourClickListener listener;
    private HappyListAdapter adapter;

    public interface OnHappyHourClickListener {
        public void onHappyHourClicked(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the data for the happy hours (usually a xml-request would be here)
        //TODO
        ArrayList<HappyHour> happies = new ArrayList<HappyHour>();
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

            happies.add(happy);
        }
        this.adapter = new HappyListAdapter(getActivity(), happies);
        setListAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnHappyHourClickListener) activity;
        }
        catch ( ClassCastException e ) {
            throw new ClassCastException(activity.toString() + " must implement OnHappyHourSelectedListener!" + e);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        listener.onHappyHourClicked(position);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode( ListView.CHOICE_MODE_SINGLE );
    }
}