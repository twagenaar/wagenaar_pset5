package a11021047.restaurant2;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Tessa on 27-11-2017.
 */

public class RestoAdapter extends ResourceCursorAdapter {

    RestoDatabase db;

    public RestoAdapter(Context context, Cursor cursor) {
        super(context, R.layout.row_order, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        db = RestoDatabase.getInstance(context);
        TextView name = view.findViewById(R.id.name);
        TextView amount = view.findViewById(R.id.amount);
        String name_text = cursor.getString(cursor.getColumnIndex("name"));
        String amount_text = Integer.toString(cursor.getInt(cursor.getColumnIndex("amount")));
        name.setText(name_text);
        amount.setText(amount_text);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row_order, parent, false);
    }
}
