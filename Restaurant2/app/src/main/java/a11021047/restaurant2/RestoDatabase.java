package a11021047.restaurant2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Tessa on 27-11-2017.
 */

public class RestoDatabase extends SQLiteOpenHelper {

    private static String DB_NAME = "OrderDB";
    private static String TABLE = "order_items";
    private static int VERSION_NUMBER = 1;
    private static RestoDatabase instance;

    private RestoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context, DB_NAME, null, VERSION_NUMBER, null);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, price INTEGER NOT NULL, amount INTEGER NOT NULL)";
        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        VERSION_NUMBER = i1;
        onCreate(sqLiteDatabase);
    }

    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE);
    }

    public void addItem(String name, int price) {
        SQLiteDatabase db = getWritableDatabase();


        ContentValues values = new ContentValues();
        if (checkNotExists(name)) {
            values.put("name", name);
            values.put("price", price);
            values.put("amount", 1);
            db.insert(TABLE, null, values);
        }
        else {
            System.out.println("ENTERED FUNCTION");
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE name=?", new String[] {name});
            System.out.println("colcount" + cursor.getColumnCount() + " " + cursor.getCount());


            //String test = cursor.getString(1);
            //Log.d("test", test);
            cursor.moveToFirst();

            int amount = cursor.getInt(3);
            Log.d("amount", Integer.toString(amount));
            values.put("amount", ++amount);
            db.update(TABLE, values, "name=?", new String[] {name});
            cursor.close();
        }
    }

    private boolean checkNotExists(String name)
    {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT name FROM " + TABLE + " WHERE name=?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false;
        }
        else {
            cursor.close();
            return true;
        }
    }

    public Cursor selectAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }
}
