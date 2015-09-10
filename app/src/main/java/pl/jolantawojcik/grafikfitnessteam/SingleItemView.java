package pl.jolantawojcik.grafikfitnessteam;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SingleItemView extends Activity {

    private String className, time, instructor, date;
    private TextView tvClassName, tvTime, tvInstructor, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_item_view);

        Intent i = getIntent();
        className = i.getStringExtra("class");
        time = i.getStringExtra("time");
        instructor = i.getStringExtra("instructor");
        date = i.getStringExtra("date");

        tvClassName = (TextView) findViewById(R.id.classes);
        tvTime = (TextView) findViewById(R.id.time);
        tvInstructor = (TextView) findViewById(R.id.instructor);
        tvDate = (TextView) findViewById(R.id.date);

        tvClassName.setText(className);
        tvTime.setText(time);
        tvInstructor.setText(instructor);
        tvDate.setText(date);
    }

}
