package com.myfeedback.myfeedbackprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComplaintFragment extends Fragment {

    private static final String url = "http://192.168.0.176/android/complaintQuery.php?user_email=";

    List<ComplaintList> complaintList;
    RecyclerView recyclerView;

    public ComplaintFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_complaint, container, false);

        FloatingActionButton fab = RootView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newComplaint = new Intent(ComplaintFragment.this.getActivity(), NewComplaint.class);
                startActivity(newComplaint);
            }
        });

        // Inflate the layout for this fragment
        TextView tv = RootView.findViewById(R.id.textViewNonMemberNotify);
        recyclerView = RootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String full_url = "";
        //check user is logged in
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            tv.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);

            String user_email = SharedPrefManager.getInstance(getContext()).getKeyUserEmail();
            full_url = url.concat(user_email); //append uid to the url
        } else {
            tv.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }

        //initializing the productlist
        complaintList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, full_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject complaint = array.getJSONObject(i);

                                complaintList.add(new ComplaintList(
                                        complaint.getInt("id"),
                                        complaint.getString("title"),
                                        complaint.getString("description"),
                                        complaint.getString("status")
                                ));
                            }

                            //creating adapter object and setting it to recyclerView
                            ComplaintListAdapter adapter = new ComplaintListAdapter(getActivity(), complaintList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return RootView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here
        //getting the recyclerview from xml
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // initialise your views
    }
}
