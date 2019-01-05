package com.myfeedback.myfeedbackprototype;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AchievementFragment extends Fragment {

    private static final String url = "http://192.168.1.104/phpQuery/achievementCount.php";

    TextView tv_totalComplaintCount, tv_message, tv_notify;
    ConstraintLayout cl;
    FrameLayout fl;

    public AchievementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_achievement, container, false);

        cl = view.findViewById(R.id.constLayoutAchievement);
        tv_notify = view.findViewById(R.id.textViewNonMemberNotify2);

        //check user is logged in
        if (SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
            cl.setVisibility(View.VISIBLE);
            tv_notify.setVisibility(View.INVISIBLE);

            //get total count from database
            String result = null;
            tv_totalComplaintCount = view.findViewById(R.id.textViewTotalComplaintCount);
            tv_message = view.findViewById(R.id.textViewQuote);
            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

            final String finalResult = result;
            JsonArrayRequest jsonRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject jObj = new JSONObject(String.valueOf(response.get(0)));
                                String count = jObj.getString("COUNT(*)");
                                //display the message based on how many achievement we achieve
                                int calcCount = Integer.parseInt(count);

                                String message = "";
                                if (calcCount == 0) {
                                    message = "No complaint made. Consider adding one?";
                                } else if (calcCount == 1) {
                                    message = "A first big step from you. Keep it up!";
                                } else if (calcCount > 2 && calcCount <= 10) {
                                    message = "Keep it going!";
                                } else if (calcCount > 10 && calcCount < 50) {
                                    message = "Incredible work! You are our nation's hero!";
                                } else if (calcCount >= 50 && calcCount < 100) {
                                    message = "Just unbelievable...";
                                } else if (calcCount >= 100) {
                                    message = "Thanks for contributing to our society and makes it a better place.";
                                }

                                tv_message.setText(message);
                                tv_totalComplaintCount.setText(count);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            queue.add(jsonRequest);

        } else {
            cl.setVisibility(View.INVISIBLE);
            tv_notify.setVisibility(View.VISIBLE);
        }

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // do your variables initialisations here

    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialise your views

    }
}
