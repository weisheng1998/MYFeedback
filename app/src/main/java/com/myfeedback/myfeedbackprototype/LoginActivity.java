package com.myfeedback.myfeedbackprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText email_et, password_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void OnLogin(View view) {
        EditText user = (EditText)findViewById(R.id.email_et);
        String email = String.valueOf(user.getText());

        EditText pass = (EditText)findViewById(R.id.password_et);
        String password = String.valueOf(pass.getText());

        String type = "login";
        LoginBackground loginBackground = new LoginBackground(this);
        loginBackground.execute(type, email, password);
    }

    public void BtnRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
