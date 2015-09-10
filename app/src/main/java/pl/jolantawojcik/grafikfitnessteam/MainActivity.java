package pl.jolantawojcik.grafikfitnessteam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity {

    ListViewAdapter adapter;
    DBHandler dbHandler;
    Record record;
    private static final String PING_URL = "http://www.fitnessteam.net/grafik_zajec.html";
    private ProgressDialog progressDialog;
    private ArrayList<HashMap<String, String>> arrayList;
    private List list;
    static String CLASSES = "classes";
    static String DATE = "date";
    static  String TIME = "time";
    static String INSTRUCTOR = "instructor";
    private HashMap<String, String> map = new HashMap<String, String>();
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this, null, null, 1);
        record = new Record();
        arrayList = new ArrayList<HashMap<String, String>>();

        dbHandler.deleteAllRecords();
        new AddRecords().execute();
    }

    private class AddRecords extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Fitness Team Grafik");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(PING_URL).get();
                for (Element table : doc.select("table[class=schedule2]")) {
                    Elements rows = table.select("tr");
                    Element columnsDays = rows.select("tr").first();
                    Elements day = columnsDays.select("th");
                    Element row = rows.get(1);
                    Elements columns = row.select("td");
                    for (int i = 1; i < columns.size(); i++) {
                        Element column = columns.get(i);
                        List<String> timeListString = new ArrayList<String>();
                        for (Element event : column.select("div[class=inner]")) {
                            Elements time = event.getElementsByClass("czas");
                            timeListString.add(time.text());
                        }
                        Collections.sort(timeListString, new Comparator<String>() {
                            @Override
                            public int compare(String hour_one, String hour_next) {
                                try {
                                    Matcher matcher = Pattern.compile("\\d+").matcher(hour_one);
                                    matcher.find();
                                    Integer integer1 = Integer.valueOf(matcher.group());
                                    Matcher matcher2 = Pattern.compile("\\d+").matcher(hour_next);
                                    matcher2.find();
                                    Integer integer2 = Integer.valueOf(matcher2.group());
                                    return integer1.compareTo(integer2);
                                } catch (java.lang.NumberFormatException e) {
                                    return hour_one.compareTo(hour_next);
                                }
                            }
                        });
                        for (int y = 0; y < timeListString.size(); y++) {
                            Log.d("timeList", timeListString.get(y));
                            for (Element event : column.select("div[class=inner]")) {
                                Elements name = event.getElementsByClass("zajecia");
                                Elements instructor = event.getElementsByTag("a");
                                Elements time = event.getElementsByClass("czas");
                                if (time.text().equals(timeListString.get(y))) {
                                    dbHandler.createTableRow(new Record(time.text(), day.get(i).text(), name.text(), instructor.text()));
                                }
                            }
                        }
                    }
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            list = dbHandler.getSortedRecords();
            if(result == false || list.isEmpty()){
                Toast toast= Toast.makeText(getApplicationContext(),
                        "Przepraszamy wyst\u0105pi\u0142 problem z pobieraniem danych, " +
                                " prosz\u0119 sprawdzi\u0107 grafik na stronie internetowej",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
            else {
                for (int i = 0; i < list.size(); i++) {
                    record = (Record) list.get(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("time", record.getTime());
                    map.put("date", record.getDate());
                    map.put("classes", record.getClasses());
                    map.put("instructor", record.getInstructor());
                    arrayList.add(map);
                    listview = (ListView) findViewById(R.id.listView);
                    adapter = new ListViewAdapter(MainActivity.this, arrayList);
                    listview.setAdapter(adapter);
                }
            }
            progressDialog.dismiss();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_contact) {
            Intent i = new Intent(MainActivity.this, Contact.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
