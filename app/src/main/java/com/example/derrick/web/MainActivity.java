package com.example.derrick.web;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String URL_DATA="http://clp.tinkeredu.net/online.json";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listItems = new ArrayList<>();

        loadRecyclerViewData();

        }
        private void loadRecyclerViewData(){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Data...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    URL_DATA,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONObject jsono =  jsonObject.getJSONObject("data");
                        JSONArray jarray = jsono.getJSONArray("content");

                        for(int i=0; i<jarray.length(); i++){
                            JSONObject object = jarray.getJSONObject(i);
                            JSONObject thumbnail = object.getJSONObject("thumbnail");

//                            String head = thumbnail.getString("name");
//                            String desc = thumbnail.getString("contentType");
//                            String imageUrl = thumbnail.getString("resourceUri");

                            ListItem item = new ListItem(
                                    thumbnail.getString("name"),
                                    thumbnail.getString("contentType"),
                                    thumbnail.getString("resourceUri")
                            );
                            listItems.add(item);
                        }


                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                    adapter = new MyAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);

                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError){
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }

