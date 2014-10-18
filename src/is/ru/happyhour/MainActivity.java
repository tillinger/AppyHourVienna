package is.ru.happyhour;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity implements HappyListFragment.OnHappyHourClickListener {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public void onHappyHourClicked(int position) {
        System.out.println("onHappyHourClicked called!");

        DetailFragment detailFragment = (DetailFragment)
                getFragmentManager().findFragmentById(R.id.detail_fragment);

        if (detailFragment != null) {
            detailFragment.updateArticleView(position);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("POS", position);
            startActivity(intent);
        }
    }
}
