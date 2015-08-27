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
    private static final String url = "http://www.fitnessteam.net/grafik_zajec.html";
    private ProgressDialog progressDialog;
    ArrayList<HashMap<String, String>> arrayList;
    private List<String> timeListString;
    private List list;
    static String CLASSES = "class";
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
    }

    private class AddRecords extends AsyncTask<Void, Void, Void>{

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
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(url).get();
                for(Element table : doc.select("table[class=schedule2]")){
                    Elements content = table.select("tr");
                    Element rowDays = content.select("tr").first();
                    Elements days = rowDays.select("th");
                    Element allClasses = content.get(1);
                    Elements columns = allClasses.select("td");
                    for(int i =1; i < columns.size(); i++){
                        Element singleClass = columns.get(i);
                        timeListString = new ArrayList<String>();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            list = dbHandler.getSortedRecords();
            for(int i=0; i<list.size(); i++){
                record = (Record) list.get(i);
                map.put("time", record.getTime());
                map.put("instructor", record.getInstructor());
                map.put("class", record.getClasses());
                map.put("date", record.getDate());
                arrayList.add(map);
                listView = (ListView) findViewById(R.id.listView);
                adapter = new ListViewAdapter(MainActivity.this, arrayList);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }
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
            Intent i = new Intent(this, Contact.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
