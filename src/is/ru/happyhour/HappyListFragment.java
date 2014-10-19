package is.ru.happyhour;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import is.ru.happyhour.adapter.HappyListAdapter;
import is.ru.happyhour.async.LoadHappiesHttp;
import is.ru.happyhour.model.Address;
import is.ru.happyhour.model.DayOfWeek;
import is.ru.happyhour.model.HappyHour;
import is.ru.happyhour.model.HappyHourType;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;

public class HappyListFragment extends ListFragment {

    private OnHappyHourClickListener clickListener;
    private HappyListAdapter listAdapter;
    private SpinnerAdapter spinnerAdapter;
    private ActionBar.OnNavigationListener navigationListener;

    private LoadHappiesHttp downloadTask;

    public interface OnHappyHourClickListener {
        public void onHappyHourClicked(int position);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);

        this.navigationListener = new ActionBar.OnNavigationListener() {
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.action_list);

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                if(strings[position].equals(strings[0])) { //now
                    //TODO load everything from database
                    System.out.println("NOW pressed");
                } else if(strings[position].equals(strings[1])) { //all
                    //TODO load only the current happy hours from the database
                    System.out.println("ALL pressed");
                }
                return true;
            }
        };

        //set the actionbar to the navigation list and the callback method
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);
        getActivity().getActionBar().setNavigationMode(getActivity().getActionBar().NAVIGATION_MODE_LIST);
        getActivity().getActionBar().setListNavigationCallbacks(spinnerAdapter, this.navigationListener);

        //tell the framework that this fragment has a menu
        this.setHasOptionsMenu(true);


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
        this.listAdapter = new HappyListAdapter(getActivity(), happies);
        setListAdapter(listAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            clickListener = (OnHappyHourClickListener) activity;
        }
        catch ( ClassCastException e ) {
            throw new ClassCastException(activity.toString() + " must implement OnHappyHourSelectedListener!" + e);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        clickListener.onHappyHourClicked(position);
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode( ListView.CHOICE_MODE_SINGLE );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //running downloads get cancelled here!
        if(downloadTask != null) {
            downloadTask.cancel(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                this.setListShown(false);
                downloadTask = new LoadHappiesHttp(this);
                downloadTask.execute();
                break;
            case R.id.menu_order_by_price:
                System.out.println("ORDER BY PRICE pressed");
                break;
            case R.id.menu_order_by_location:
                System.out.println("ORDER BY LOCATION pressed");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void downloadFinished(boolean success) {
        if(success) {
            this.setListShown(true);
        } else {
            Toast.makeText(getActivity(), "Error downloading Happy Hours!", Toast.LENGTH_LONG).show();
        }
    }
}