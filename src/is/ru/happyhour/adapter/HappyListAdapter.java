package is.ru.happyhour.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import is.ru.happyhour.R;
import is.ru.happyhour.model.HappyHour;

import java.util.ArrayList;

public class HappyListAdapter extends ArrayAdapter<HappyHour> {

    private Context context;
    private ArrayList<HappyHour> happyhours;

    public HappyListAdapter(Context context, ArrayList<HappyHour> happyhours) {
        super(context, R.layout.happy_list_row, happyhours);
        this.context = context;
        this.happyhours = happyhours;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.happy_list_row, parent, false);

        //get Views of this layout
        ImageView image = (ImageView) rowView.findViewById(R.id.happy_row_image);
        TextView title = (TextView) rowView.findViewById(R.id.happy_row_title);
        TextView address = (TextView) rowView.findViewById(R.id.happy_row_address);
        TextView time = (TextView) rowView.findViewById(R.id.happy_row_time);
        TextView price = (TextView) rowView.findViewById(R.id.happy_row_price);

        //Set TextViews text for the elements in the row_layout
        if (happyhours.get(position).getBarName() != null) {
            title.setText(happyhours.get(position).getBarName());
        }
        if (happyhours.get(position).getAddress() != null) {
            address.setText(happyhours.get(position).getAddress().getAddress() + ", " + happyhours.get(position).getAddress().getPostcode());
        }

        time.setText(happyhours.get(position).getTimeString());
        price.setText(happyhours.get(position).getPrice() + "0 €");

        return rowView;
    }

    public void addHappyHours(ArrayList<HappyHour> newHappies) {
        this.happyhours.addAll(newHappies);
    }

    public void removeAll() {
        this.happyhours.clear();
    }
}
