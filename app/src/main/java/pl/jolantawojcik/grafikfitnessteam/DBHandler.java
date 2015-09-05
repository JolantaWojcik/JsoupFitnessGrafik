package pl.jolantawojcik.grafikfitnessteam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jola on 25/08/2015.
 */
@SuppressWarnings("ALL")
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "grafik.db";
    public static final String TABLE_NAME = "zajecia";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CLASSES = "classes";
    public static final String COLUMN_INSTRUCTOR = "instructor";
    private static final String TAG = "DbAdapter";
    private Date dt = new Date();
    private Calendar c = Calendar.getInstance();
    private DateFormat currentDayOfWeek = new SimpleDateFormat("EEEE");
    private String day1, day2, day3, day4, day5, day6, day7;
    private String[] days;
    private List listOfallRecords;

    //chyba nie potrzebne
//    private static final String[] COLUMNS = { COLUMN_ID, COLUMN_TIME, COLUMN_DATE, COLUMN_CLASSES, COLUMN_INSTRUCTOR };

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_CLASSES + " TEXT, " +
                COLUMN_INSTRUCTOR + " TEXT " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void createTableRow(Record record) {
        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, record.getTime());
        values.put(COLUMN_DATE, record.getDate());
        values.put(COLUMN_CLASSES, record.getClasses());
        values.put(COLUMN_INSTRUCTOR, record.getInstructor());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //to correct
    @SuppressWarnings("unchecked")
    public List getSortedRecords(){
        listOfallRecords = new LinkedList();
        day1  = currentDayOfWeek.format(dt); c.setTime(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day2 = currentDayOfWeek.format(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day3 = currentDayOfWeek.format(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day4 = currentDayOfWeek.format(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day5 = currentDayOfWeek.format(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day6 = currentDayOfWeek.format(dt); c.add(Calendar.DATE, 1); dt = c.getTime();
        day7 = currentDayOfWeek.format(dt);
        days= new String[]{day1, day2, day3, day4, day5, day6, day7};
        Log.d("day51", day1);

        SQLiteDatabase db = this.getReadableDatabase();
        //implement it somehow with query .......................
        String query = "SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        Record records = null;
        for (int y = 0; y < days.length; y++) {
            if (cursor.moveToFirst()) {
                do {
                    records = new Record();
                    if (cursor.getString(2).equals(days[y])) {
                        Log.d("day77", days[y]);
                        records.setId(Integer.parseInt(cursor.getString(0)));
                        records.setTime(cursor.getString(1));
                        records.setDate(cursor.getString(2));
                        records.setClasses(cursor.getString(3));
                        records.setInstructor(cursor.getString(4));
                        listOfallRecords.add(records);
                        Log.d("day712", String.valueOf(listOfallRecords));
                    }
                } while (cursor.moveToNext());
            }
        }
        Log.d("day711", String.valueOf(listOfallRecords));
        return listOfallRecords;
    }

    public boolean deleteAllRecords() {
        SQLiteDatabase db = getWritableDatabase();
        int doneDelete = 0;
        doneDelete = db.delete(TABLE_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
}
}
