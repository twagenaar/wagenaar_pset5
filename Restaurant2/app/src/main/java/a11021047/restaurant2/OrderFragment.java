package a11021047.restaurant2;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends DialogFragment implements View.OnClickListener {

    RestoDatabase db;
    RestoAdapter adapter;
    ListView listView;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        db = RestoDatabase.getInstance(getActivity().getApplicationContext());
        Cursor cursor = db.selectAll();
        adapter = new RestoAdapter(getActivity(), cursor);
        listView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        Button b = (Button) view.findViewById(R.id.cancel);
        b.setOnClickListener(this);
        Button b2 = (Button) view.findViewById(R.id.place);
        b2.setOnClickListener(this);

        listView = view.findViewById(R.id.orders);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                db.clear();
                break;
            case R.id.place:
                place_order();
                break;
        }
        adapter.swapCursor(null);
    }


    private void place_order() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://resto.mprog.nl/order";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            CharSequence text = "Preparation Time: " + Integer.toString(object.getInt("preparation_time"));
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(getContext(), text, duration).show();
                        }
                        catch (Exception e) {
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(stringRequest);
        db.clear();


    }
}

