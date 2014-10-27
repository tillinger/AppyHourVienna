package is.ru.happyhour;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.content.Intent;
import android.view.Window;
import android.widget.TextView;

public class DetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the Fragment as the content of android.R.id.content
        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(android.R.id.content, new DetailFragment()).commit();
        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99666666")));
    }
}