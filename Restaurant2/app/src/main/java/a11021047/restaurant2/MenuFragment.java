package a11021047.restaurant2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {

    ListView listView;
    RestoDatabase db;
    String course;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = RestoDatabase.getInstance(getActivity().getApplicationContext());

        Bundle arguments = this.getArguments();
        course = arguments.getString("category");

        String url = "https://resto.mprog.nl/menu?category=";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + course,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(stringRequest);

    }

    public void parseResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            final JSONArray array = object.getJSONArray("items");
            ArrayList<String> myStringArray = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                String name = item.getString("name");
                myStringArray.add(name);
            }

            ArrayAdapter<String> list = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_1,
                    myStringArray);
            this.setListAdapter(list);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        listView = view.findViewById(android.R.id.list);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String name = l.getItemAtPosition(position).toString();

//        String url = "https://resto.mprog.nl/menu?category=";
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + course,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println(error.toString());
//            }
//        });
//
//        queue.add(stringRequest);


        int price = 3;
        db.addItem(name, price);
    }
}
