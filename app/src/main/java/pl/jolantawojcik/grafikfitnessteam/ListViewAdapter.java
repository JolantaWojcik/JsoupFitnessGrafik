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

public class ListViewAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> data;
    private HashMap<String, String> resultp = new HashMap<String, String>();
    private View itemView;
    private LinearLayout customLayout;
    private TextView classes, time, date, instructor;

    public ListViewAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        itemView = inflater.inflate(R.layout.list_item, parent, false);

        customLayout = (LinearLayout) itemView.findViewById(R.id.customLayout);

        resultp = data.get(position);

        classes = (TextView) itemView.findViewById(R.id.classes);
        time = (TextView) itemView.findViewById(R.id.time);
        date = (TextView) itemView.findViewById(R.id.date);
        instructor = (TextView) itemView.findViewById(R.id.instructor);

        Date today = new Date();
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("E, dd-MM-yyyy");

        String pon = df.format(c.getTime());
        c.add(Calendar.DATE, 1);
        String wt = df.format(c.getTime());
        c.add(Calendar.DATE, 1);

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
                resultp = data.get(position);
                Intent intent = new Intent(context, SingleItemView.class);
                intent.putExtra("classes", resultp.get(MainActivity.CLASSES));
                intent.putExtra("time", resultp.get(MainActivity.TIME));
                intent.putExtra("date",resultp.get(MainActivity.DATE));
                intent.putExtra("instructor", resultp.get(MainActivity.INSTRUCTOR));
                context.startActivity(intent);

            }
        });
        return itemView;
    }
}

