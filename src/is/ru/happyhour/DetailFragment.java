package is.ru.happyhour;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import is.ru.happyhour.model.HappyHour;

import java.io.IOException;


public class DetailFragment extends Fragment {

    private final static String HAPPYHOUR_SAVE = "HAPPYHOUR_SAVE";
    private HappyHour happyHour;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            this.happyHour = (HappyHour) savedInstanceState.getSerializable(HAPPYHOUR_SAVE);
        }

        this.view = inflater.inflate(R.layout.detail_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (this.happyHour != null) { //got from savedinstance state
            this.updateHappyHour(this.happyHour);
        } else if (args != null) {
            this.updateHappyHour((HappyHour) args.getSerializable(MainActivity.HAPPYHOUR_EXTRA));
        } else {
            System.err.println("ERROR: no args and happyhour == null");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //tell the framework that this fragment has a menu
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                String message = "Hey guy!\nI'm going to the Happy-Hour-Event at "
                        + happyHour.getBarName() + "! The address is "
                        + happyHour.getAddress().getAddress() + ".";
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Happy Hour Event");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(sharingIntent, "Share with ..."));
                break;

            case R.id.menu_location:
                MyMapFragment mapFragment = new MyMapFragment();
                Bundle args = new Bundle();
                args.putSerializable(MainActivity.HAPPYHOUR_EXTRA, this.happyHour);
                mapFragment.setArguments(args);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //decide which fragment to switch (depending on screen size)
                DetailFragment detailFragmentLarge = (DetailFragment)
                        getFragmentManager().findFragmentById(R.id.detail_fragment_large);
                if(detailFragmentLarge != null) {
                    fragmentTransaction.replace(R.id.detail_fragment_large, mapFragment);
                } else {
                    fragmentTransaction.replace(R.id.detail_fragment_container, mapFragment);
                }

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void updateHappyHour(HappyHour newHappyHour) {
        this.happyHour = newHappyHour;

        System.out.println("newhappyhour is: " + newHappyHour);

        //write the happyhour fields
        ImageView image = (ImageView) view.findViewById(R.id.detail_image);
        TextView title = (TextView) view.findViewById(R.id.detail_title);
        TextView address = (TextView) view.findViewById(R.id.detail_address);
        TextView days = (TextView) view.findViewById(R.id.detail_dayinweek_textview);
        TextView beerTime = (TextView) view.findViewById(R.id.detail_beer_time);
        TextView beerPrice = (TextView) view.findViewById(R.id.detail_beer_price);
        TextView description = (TextView) view.findViewById(R.id.detail_description_text);

        try { //TODO download lazily from server
            if(happyHour.getBarName().startsWith("Loco")) { //only testing purpose
                image.setImageDrawable(Drawable.createFromStream(getActivity().getAssets().open("2.jpg"), null));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                image.setImageDrawable(Drawable.createFromStream(getActivity().getAssets().open("stephans.jpg"), null));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(happyHour.getBarName() != null) {
            title.setText(happyHour.getBarName());
        }
        if(happyHour.getAddress() != null) {
            address.setText(happyHour.getAddress().getAddressWithPostCode());
        }
        if(happyHour.getDaysOfWeek() != null) {
            //Todo
            //if every day: write every day and try to abbreviate if with "monday to friday" for example!
            //TODO if today is happy hour first say: TODAY
            days.setText(happyHour.getDaysAsString());
        }

        beerTime.setText(happyHour.getTimeString());
        beerPrice.setText(happyHour.getPrice() + "0 €");



        if(happyHour.getDescriptionBar() != null) {
            description.setText(happyHour.getDescriptionBar());
        }

        //tell the scrollview in the layout that the content has changed
        //TODO scrollview problems
        view.invalidate();
        view.requestLayout();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current happyhour selection in case we need to recreate the fragment
        outState.putSerializable(HAPPYHOUR_SAVE, this.happyHour);
    }
}