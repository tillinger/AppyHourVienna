package is.ru.happyhour;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
    }

    @Override
    public void onHappyHourClicked(HappyHour clickedHappyHour) {
        System.out.println("onHappyHourClicked called!");

        DetailFragment detailFragment = (DetailFragment)
                getFragmentManager().findFragmentById(R.id.detail_fragment);

        if (detailFragment != null) {
            detailFragment.updateHappyHour(clickedHappyHour);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(HAPPYHOUR_EXTRA, clickedHappyHour);
            startActivity(intent);
        }
    }
}
