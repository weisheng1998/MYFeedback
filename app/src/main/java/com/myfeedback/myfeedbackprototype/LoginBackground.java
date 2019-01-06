package com.myfeedback.myfeedbackprototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class LoginBackground extends AsyncTask<String, Void, String> {
    public String p_username;
    ProgressDialog loadingDialog;
    public Context context;
    String sharedpref_url = "http://192.168.0.176/android/loginQuery.php?user_email=";

    public LoginBackground(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String login_url = "https://developer.tprocenter.net/android/login.php";
        String type = params[0];
        p_username = params[1];

        if (type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        loadingDialog = ProgressDialog.show(context, "Please Wait", "Logging In ...", true);
    }

    @Override
    protected void onPostExecute(String result) {
        loadingDialog.dismiss();
        if (result.contentEquals("login_success")) {
            //retrieve data from database and saved to SharedPrefManager
            String full_url = sharedpref_url.concat(p_username);
            RequestQueue queue = Volley.newRequestQueue(context);

            final String finalResult = result;
            JsonArrayRequest jsonRequest = new JsonArrayRequest
                    (Request.Method.GET, full_url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject jObj = new JSONObject(String.valueOf(response.get(0)));
                                String q_userid = jObj.getString("userid");
                                String q_f_name = jObj.getString("f_name");
                                String q_l_name = jObj.getString("l_name");
                                String q_age = jObj.getString("age");
                                String q_email = jObj.getString("email");
                                String q_address = jObj.getString("address");
                                String q_ic = jObj.getString("ic");
                                String q_imageinfo = jObj.getString("imageinfo");

                                SharedPrefManager
                                        .getInstance(context.getApplicationContext())
                                        .userLogin(q_userid, q_f_name, q_l_name, q_age, q_email, q_address, q_ic, q_imageinfo);

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

            ((LoginActivity) context).finish();
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Login Failure")
                    .setMessage(result)
                    .setCancelable(false)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
