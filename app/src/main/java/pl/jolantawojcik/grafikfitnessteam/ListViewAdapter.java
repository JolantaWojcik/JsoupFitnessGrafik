package pl.jolantawojcik.grafikfitnessteam;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jola on 25/08/2015.
 */
public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout customLayout;
    private View view;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> map;
    Date newDate = new Date();
    Calendar calendar = Calendar.getInstance();
    DateFormat df = new SimpleDateFormat("E, dd-MM-yyyy");
    private String dayOfWeek;
    private TextView time, instructor, className, date;


    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.list_item, parent, false);
        customLayout = (LinearLayout) view.findViewById(R.id.customLayout);
        arrayList.get(position);

        time = (TextView) view.findViewById(R.id.time);
        instructor = (TextView) view.findViewById(R.id.instructor);
        className = (TextView) view.findViewById(R.id.classes);
        date = (TextView) view.findViewById(R.id.date);

        time.setText(map.get(MainActivity.TIME));
        instructor.setText(map.get(MainActivity.INSTRUCTOR));
        className.setText(map.get(MainActivity.CLASSES));
        date.setText(map.get(MainActivity.DATE));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map = arrayList.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                intent.putExtra("time", map.get(MainActivity.TIME));
                intent.putExtra("instructor", map.get(MainActivity.INSTRUCTOR));
                intent.putExtra("class", map.get(MainActivity.CLASSES));
                intent.putExtra("date", map.get(MainActivity.DATE));
                context.startActivity(intent);
            }
        });

        dayOfWeek = map.get(MainActivity.DATE);
        if(dayOfWeek.contains("Pon")){
            customLayout.setBackgroundColor(0xFFDDE4FF);
        }
        if (dayOfWeek.contains("?ro")){
            customLayout.setBackgroundColor(0x218757FF);
        }
        if (dayOfWeek.contains("Pi")){
            customLayout.setBackgroundColor(0x1758FF91);
        }
        if (dayOfWeek.contains("Nie")){
            customLayout.setBackgroundColor(0xFFFEFFD8);
        }
        return view;
    }
}
