package com.myfeedback.myfeedbackprototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, email, pass, age, IC, address;
    String str_fname, str_lname, str_email, str_pass, str_age, str_IC, str_Address, type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = (EditText)findViewById(R.id.regFname);
        lname = (EditText)findViewById(R.id.regLname);
        email = (EditText)findViewById(R.id.regEmail);
        pass = (EditText)findViewById(R.id.regPassword);
        age = (EditText)findViewById(R.id.regAge);
        IC = (EditText)findViewById(R.id.regIC);
        address = (EditText)findViewById(R.id.regAddress);
    }

    public void OnRegister(View view){
        str_fname = String.valueOf(fname.getText());
        str_lname = String.valueOf(lname.getText());
        str_email = String.valueOf(email.getText());
        str_pass = String.valueOf(pass.getText());
        str_age = String.valueOf(age.getText());
        str_IC = String.valueOf(IC.getText());
        str_Address = String.valueOf(address.getText());
        type = "register";

        LoginBackground loginBackground = new LoginBackground(this);
        loginBackground.execute(type, str_fname, str_lname, str_age, str_email, str_IC, str_Address, str_pass);
    }
}

