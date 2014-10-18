package is.ru.happyhour;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class DetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Intent intent = getIntent();
        Integer position = intent.getIntExtra("POS", -1);

        TextView textView = (TextView) findViewById(R.id.detail_textview);
        textView.setText("detail activity text: " + position);
    }
}