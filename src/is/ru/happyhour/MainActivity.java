package is.ru.happyhour;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import is.ru.happyhour.model.HappyHour;

public class MainActivity extends Activity implements HappyListFragment.OnHappyHourClickListener {

    public static final String HAPPYHOUR_EXTRA = "HAPPYHOUR_EXTRA";
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //only add fragment if first created and the screen size is large
        if(savedInstanceState == null && displaySizeLargeOrXLarge()) {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.detail_fragment_large, detailFragment)
                    .commit();
        }
    }

    private boolean displaySizeLargeOrXLarge() {
        return
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                        == Configuration.SCREENLAYOUT_SIZE_XLARGE)
                        ||
                ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                        == Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    @Override
    public void onHappyHourClicked(HappyHour clickedHappyHour) {
        System.out.println("onHappyHourClicked called!");

        Fragment fragment = getFragmentManager().findFragmentById(R.id.detail_fragment_large);

        if (fragment instanceof DetailFragment) {
            ((DetailFragment) fragment).updateHappyHour(clickedHappyHour);
        } else if (fragment instanceof MyMapFragment) {
            getFragmentManager().popBackStackImmediate(); //pop the last fragment from the
            DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.detail_fragment_large);
            detailFragment.updateHappyHour(clickedHappyHour);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(HAPPYHOUR_EXTRA, clickedHappyHour);
            startActivity(intent);
        }
    }
}
