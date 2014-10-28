package is.ru.happyhour;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import is.ru.happyhour.model.HappyHour;

import java.io.IOException;


public class DetailFragment extends Fragment {

    private final static String HAPPYHOUR_SAVE = "HAPPYHOUR_SAVE";
    HappyHour happyHour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.

        if (savedInstanceState != null) {
            this.happyHour = (HappyHour) savedInstanceState.getSerializable(HAPPYHOUR_SAVE);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //tell the framework that this fragment has a menu
        this.setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            this.updateHappyHour((HappyHour) args.getSerializable(MainActivity.HAPPYHOUR_EXTRA));
        } else { //HappyHour already read in onCreateView because of savedInstanceState
            this.updateHappyHour(this.happyHour);
        }
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
                //TODO share this happyhour
                System.out.println("share pressed!");
                break;
            case R.id.menu_location:
                System.out.println("location pressed!");

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
                    System.out.println("detailFragmentLarge != null!");
                    fragmentTransaction.replace(R.id.detail_fragment_large, mapFragment);
                } else {
                    System.out.println("detailFragmentLarge == null!");
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

        ImageView image = (ImageView) getActivity().findViewById(R.id.detail_image);
        try {
            image.setImageDrawable(Drawable.createFromStream(getActivity().getAssets().open("2.jpg"), null));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current happyhour selection in case we need to recreate the fragment
        outState.putSerializable(HAPPYHOUR_SAVE, this.happyHour);
    }
}