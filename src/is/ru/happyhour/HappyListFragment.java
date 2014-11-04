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

    private int nowOrAll;

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
            // Get the same strings provided for the drop-down's ArrayAdapter
            String[] strings = getResources().getStringArray(R.array.action_list);


            @Override
            public boolean onNavigationItemSelected(int position, long itemId) {
                nowOrAll = position;

                if(strings[position].equals(strings[0])) { //now
                    System.out.println("NOW pressed");

                    //load the current happy hours from the db -> asynchron!
                    dbTask = new LoadHappiesDB(thisFrag);
                    dbTask.execute(0); // 0 for NOW
                } else if(strings[position].equals(strings[1])) { //all
                    System.out.println("ALL pressed");

                    //load the current happy hours from the db -> asynchron!
                    dbTask = new LoadHappiesDB(thisFrag);
                    dbTask.execute(1); // 1 for ALL
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
                this.setListShown(false);
                downloadTask = new LoadHappiesHttp(this);
                downloadTask.execute();
                break;
            case R.id.menu_order_by_price:
                System.out.println("ORDER BY PRICE pressed");
                this.setListShown(false);
                dbTask = new LoadHappiesDB(this);
                dbTask.execute(2);
                break;
            case R.id.menu_order_by_location:
                System.out.println("ORDER BY LOCATION pressed");
                //location ordering not supported right now!
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void downloadFinished(boolean success) {
        if(success) {
            //now load the happy hours from the db
            dbTask = new LoadHappiesDB(this);
            dbTask.execute(nowOrAll);
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