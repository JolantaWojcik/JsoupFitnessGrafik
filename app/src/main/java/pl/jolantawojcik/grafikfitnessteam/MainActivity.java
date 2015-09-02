package pl.jolantawojcik.grafikfitnessteam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    private ListView listView;
    ListViewAdapter adapter;
    DBHandler dbHandler;
    Record record;
    private static final String PING_URL = "http://www.fitnessteam.net/grafik_zajec.html";
    private ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> arrayList;
   // private List<String> timeListString;
    private List list;
    static String CLASSES = "classes";
    static String DATE = "date";
    static  String TIME = "time";
    static String INSTRUCTOR = "instructor";
    HashMap<String, String> map = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this, null, null, 1);
        record = new Record();
        arrayList = new ArrayList<HashMap<String, String>>();

        dbHandler.deleteAllRecords();
        new AddRecords().execute();

        Log.d("where", "exect1");
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
            Log.d("where", "exect2");
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d("where", "exect3");
            try {
                Document doc = Jsoup.connect(PING_URL).get();
                for(Element table : doc.select("table[class=schedule2]")){
                    Elements content = table.select("tr");
                    Element rowDays = content.select("tr").first();
                    Elements days = rowDays.select("th");
                    Element allClasses = content.get(1);
                    Elements columns = allClasses.select("td");
                    for(int i =1; i < columns.size(); i++){
                        Element singleClass = columns.get(i);
                        List<String> timeListString = new ArrayList<String>();
                        for(Element event : singleClass.select("div[class=inner]")){
                            Elements hour = event.getElementsByClass("czas");
                            timeListString.add(hour.text());
                        }
                        Collections.sort(timeListString, new Comparator<String>() {
                            @Override
                            public int compare(String hourFirst, String hourNext) {
                                try {
                                    Matcher matcherToHourFirst = Pattern.compile("\\d+").matcher(hourFirst);
                                    matcherToHourFirst.find();
                                    Integer fistHour = Integer.valueOf(matcherToHourFirst.group());
                                    Matcher matcherToHourNext = Pattern.compile("\\d+").matcher(hourNext);
                                    matcherToHourNext.find();
                                    Integer nextHour = Integer.valueOf(matcherToHourNext.group());
                                    return fistHour.compareTo(nextHour);
                                } catch (java.lang.NumberFormatException e) {
                                    return hourFirst.compareTo(hourNext);
                                }
                            }
                        });
                        for(int j =0; j<timeListString.size();j++) {
                            for (Element event : singleClass.select("div[class=inner]")) {
                                Elements className = event.getElementsByClass("zajecia");
                                Elements instructor = event.getElementsByTag("a");
                                Elements time = event.getElementsByClass("czas");
                                if (time.equals(timeListString.get(j))) {
                                    dbHandler.createTableRow(new Record(className.text(), instructor.text(), days.get(j).text(), time.text()));
                                    Log.d("database", String.valueOf(dbHandler.getSortedRecords()));
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
            super.onPostExecute(result);
            list = dbHandler.getSortedRecords();
            if(result == false || list.isEmpty()){
                Toast.makeText(getApplicationContext(), "Przepraszamy wyst\u0105pi\u0142 problem z pobieraniem danych, " +
                        " prosz\u0119 sprawdzi\u0107 grafik na stronie internetowej", Toast.LENGTH_LONG).show();
            }
            else{
            for(int i=0; i<list.size(); i++) {
                record = (Record) list.get(i);
                map.put("time", record.getTime());
                map.put("instructor", record.getInstructor());
                map.put("classes", record.getClasses());
                map.put("date", record.getDate());
                arrayList.add(map);
                listView = (ListView) findViewById(R.id.listView);
                adapter = new ListViewAdapter(MainActivity.this, arrayList);
                listView.setAdapter(adapter);
                Log.d("where", "exect4");
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
