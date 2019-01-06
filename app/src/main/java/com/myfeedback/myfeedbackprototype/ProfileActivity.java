package com.myfeedback.myfeedbackprototype;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    final int CODE_GALLERY_REQUEST = 999;
    Button btnedit, btnUpload;
    ImageView imageView;
    TextView tv_name, tv_age, tv_email, tv_address;
    Bitmap bitmap;

    public String db_img_path = "";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String temp_url = "http://192.168.0.176/android/upload.php?uid=";
        final String urlUpload = temp_url.concat(SharedPrefManager.getInstance(this).getKeyUserId());

        //get user profile from SharedPrefManager
        tv_name = findViewById(R.id.textViewName);
        tv_name.setText(SharedPrefManager.getInstance(this).getKeyFName() + " " + SharedPrefManager.getInstance(this).getKeyLName());
        tv_age = findViewById(R.id.textViewAge);
        tv_age.setText(SharedPrefManager.getInstance(this).getKeyAge());
        tv_email = findViewById(R.id.textViewEmail);
        tv_email.setText(SharedPrefManager.getInstance(this).getKeyUserEmail());
        tv_address = findViewById(R.id.textViewAddress);
        tv_address.setText(SharedPrefManager.getInstance(this).getKeyAddress());
        //image need to php to server to get image

        btnedit = findViewById(R.id.btnedit);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imageViewProfile);

        //check if profile image is in sharedPrefManager
        if (SharedPrefManager.getInstance(this).getKeyImageInfo() != "") {
            db_img_path = SharedPrefManager.getInstance(this).getKeyImageInfo();
            Picasso.get().load("http://192.168.0.176/android/" + db_img_path).into(imageView);
        }
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        ProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //post image to server
                progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                //send image to server using AndroidVolley StringRequest
                StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        SharedPrefManager.getInstance(ProfileActivity.this).setKeyImageInfo(response);
                        Picasso.get().invalidate("http://192.168.0.176/android" + db_img_path);
                        Toast.makeText(getApplicationContext(), "Image uploaded successfully.", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error uploading image: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String imageData = imageToString(bitmap);
                        params.put("image", imageData);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(getApplicationContext(), "You dont have permission to access gallery!", Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}
