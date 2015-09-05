package pl.jolantawojcik.grafikfitnessteam;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
/*public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private LinearLayout customLayout;
    private View view;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> map = new HashMap<String, String>();
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
        Log.d("record111", String.valueOf(map.get(MainActivity.TIME)));
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
/*        if(dayOfWeek.contains("Pon")){
            customLayout.setBackgroundColor(0xFFDDE4FF);
        }
        if (dayOfWeek.contains("roda")){
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
}*/
public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    HashMap<String, String> resultp = new HashMap<String, String>();

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        TextView classes;
        TextView time;
        TextView date;
        TextView instructor;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.list_item, parent, false);

        LinearLayout customLayout = (LinearLayout) itemView.findViewById(R.id.customLayout);

        resultp = data.get(position);

        classes = (TextView) itemView.findViewById(R.id.classes);
        time = (TextView) itemView.findViewById(R.id.time);
        date = (TextView) itemView.findViewById(R.id.date);
        instructor = (TextView) itemView.findViewById(R.id.instructor);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("E, dd-MM-yyyy");

        String pon = df.format(c.getTime());
        c.add(Calendar.DATE, 1);  // number of days to add
        String wt = df.format(c.getTime());
        c.add(Calendar.DATE, 1);  // number of days to add

        classes.setText(resultp.get(MainActivity.CLASSES));
        time.setText(resultp.get(MainActivity.TIME));
        instructor.setText(resultp.get(MainActivity.INSTRUCTOR));
        date.setText(resultp.get(MainActivity.DATE));

        String dayOfWeek = resultp.get(MainActivity.DATE);
        if(dayOfWeek.contains("Wt")){
            customLayout.setBackgroundColor(0x218757FF);
        }
        if(dayOfWeek.contains("Cz")){
            customLayout.setBackgroundColor(0x1758FF91);
        }
        if(dayOfWeek.contains("So")){
            customLayout.setBackgroundColor(0xFFFEFFD8);
        }

        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data
                intent.putExtra("classes", resultp.get(MainActivity.CLASSES));
                // Pass all data
                intent.putExtra("time", resultp.get(MainActivity.TIME));
                // Pass all data
                intent.putExtra("date",resultp.get(MainActivity.DATE));
                intent.putExtra("instructor", resultp.get(MainActivity.INSTRUCTOR));
                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}

