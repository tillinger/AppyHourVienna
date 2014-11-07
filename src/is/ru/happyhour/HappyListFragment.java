package is.ru.happyhour;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.SharedPreferences;
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
import is.ru.happyhour.async.LoadHappiesDB;
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
    private LoadHappiesDB dbTask;

    private SharedPreferences prefs;

    public static final int POSITION_NOW = 0;
    public static final int POSITION_ALL = 1;

    public static final int ORDERBYNOTHING = 0;
    public static final int ORDERBYPRICE = 1;
    public static final int ORDERBYLOCATION = 2;
    private int orderBy = 0;

    private static final String LAST_ACTUALIZATION = "LAST_ACTUALIZATION";

    public interface OnHappyHourClickListener {
        public void onHappyHourClicked(HappyHour clickedHappyHour);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.action_list, android.R.layout.simple_spinner_dropdown_item);

        final HappyListFragment thisFrag = this;
        this.navigationListener = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                System.out.println("spinner position is: " + position);
                //position = 0 = NOW is called by starting this fragment

                //check if the app shall actualize happyhours (when not actualized for 7 days!)
                if (prefs.getLong(LAST_ACTUALIZATION, (long) 0) + (1000 * 60 * 60 * 24 * 7) < System.currentTimeMillis()) {
                    actualizeList();
                } else {
                    //load the current happy hours from the db with filter -> asynchron
                    setListShown(false);
                    dbTask = new LoadHappiesDB(thisFrag);
                    dbTask.execute(position, orderBy);
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

        //initialize Adapter with empty Happy Hour List
        this.listAdapter = new HappyListAdapter(getActivity(), new ArrayList<HappyHour>());
        this.setListAdapter(listAdapter);
        this.setListShown(false);

        this.prefs = getActivity().getSharedPreferences(
                getActivity().getApplicationContext().getPackageName(), getActivity().MODE_PRIVATE);
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
        clickListener.onHappyHourClicked(this.listAdapter.getItem(position));
    }

    @Override
    public void onStart() {
        super.onStart();
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
                actualizeList();
                break;

            case R.id.menu_order_by_price:
                setOrdering(ORDERBYPRICE);
                this.setListShown(false);
                dbTask = new LoadHappiesDB(this);
                dbTask.execute(getActivity().getActionBar().getSelectedNavigationIndex(), orderBy);
                break;

            case R.id.menu_order_by_location:
                this.setOrdering(ORDERBYLOCATION);
                Toast.makeText(getActivity(), "Ordering by location is not supported right now!", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void actualizeList() {
        prefs.edit().putLong(LAST_ACTUALIZATION, System.currentTimeMillis()).commit();

        this.setListShown(false);
        downloadTask = new LoadHappiesHttp(this);
        downloadTask.execute();
    }

    private void setOrdering(int newOrdering) {
        if(orderBy == newOrdering) {
            orderBy = ORDERBYNOTHING;
        } else {
            orderBy = newOrdering;
        }
    }

    public void downloadFinished(boolean success) {
        if(success) {
            //now load the happy hours from the db
            dbTask = new LoadHappiesDB(this);
            dbTask.execute(getActivity().getActionBar().getSelectedNavigationIndex(), orderBy);
        } else {
            Toast.makeText(getActivity(), "Error downloading Happy Hours!", Toast.LENGTH_LONG).show();
        }
    }

    public void loadedFromDBSuccess(ArrayList<HappyHour> dbHappies) {
        System.out.println("loadedFromDBSuccess called, got: " + dbHappies.size() + " happy hours!");

        this.listAdapter.removeAll();
        this.listAdapter.addHappyHours(dbHappies);
        this.listAdapter.notifyDataSetChanged();
        this.setListShown(true);
    }

    public void loadedFromDatabaseError() {
        Toast.makeText(getActivity(), "Error occured during loading Happy Hours from the DB!", Toast.LENGTH_LONG).show();
    }
}