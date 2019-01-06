package com.myfeedback.myfeedbackprototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

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

public class RegisterBackground extends AsyncTask<String,Void,String> {
    ProgressDialog loadingDialog;
    public Context context;

    public RegisterBackground(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String register_url = "https://developer.tprocenter.net/android/register.php";

        if (type.equals("register")) {
            try {
                String fname = params[1];
                String lname = params[2];
                String age = params[3];
                String email = params[4];
                String IC = params[5];
                String address = params[6];
                String password = params[6];
                //String deviceID = params[7];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data =
                        URLEncoder.encode("f_name","UTF-8")+"="+URLEncoder.encode(fname,"UTF-8")+"&"
                        + URLEncoder.encode("l_name","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8")+"&"
                        + URLEncoder.encode("age","UTF-8")+"="+URLEncoder.encode(age,"UTF-8")+"&"
                        + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        + URLEncoder.encode("ic","UTF-8")+"="+URLEncoder.encode(IC,"UTF-8")+"&"
                        + URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                        + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                        //+ URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(deviceID,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line;
                while((line = bufferedReader.readLine())!= null) {
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
        loadingDialog = ProgressDialog.show(context, "Please Wait", "Registering ...", true);
    }

    @Override
    protected void onPostExecute(String result) {
        loadingDialog.dismiss();
        if (result.contentEquals("register_success")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Registration Success")
                    .setMessage("Thank You for being part of the family.")
                    .setCancelable(false)
                    .setNegativeButton("Close",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            ((RegisterActivity)context).finish();
                            context.startActivity(new Intent(context, LoginActivity.class));
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage("Sorry. Something went wrong on our side. Please try again later.");
            alertDialog.setTitle("Register failed.");
            alertDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
