package com.myfeedback.myfeedbackprototype;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {

    private ConstraintLayout constLayout;
    private int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    public void backMessage(View v){
        String snackBarMsg;
        if(count < 10){
            snackBarMsg = "Press back button or here to return";
        }else if(count >= 10 && count < 50){
            snackBarMsg = "Are you that bored? -.-";
        }else{
            snackBarMsg = "You get a new achievement [Bored deep to the bone]";
        }
        count++;
        constLayout = findViewById(R.id.aboutusConstLayout);
        Snackbar snackbar = Snackbar
                .make(constLayout, snackBarMsg, Snackbar.LENGTH_LONG)
                .setAction("BACK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AboutUsActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        snackbar.show();
    }
}
