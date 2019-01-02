package com.myfeedback.myfeedbackprototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    EditText email_et, password_et;
    boolean valid = false;

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }

    public boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        email_et = findViewById(R.id.email_et);
        (findViewById(R.id.email_et)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(email_et.getText().toString())) {
                        email_et.setError("Please fill in the blank.");
                        valid = false;
                    } else if (!isEmailValid(email_et.getText().toString())) {
                        email_et.setError("Please enter a valid email address format.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        password_et = findViewById(R.id.password_et);
        (findViewById(R.id.password_et)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(password_et.getText().toString())) {
                        password_et.setError("Please fill in the blank.");
                        valid = false;
                    } else if (password_et.getText().toString().length() < 8 && !isValidPassword(password_et.getText().toString())) {
                        password_et.setError("Password must be more than 8 and no illegal characters.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });
    }

    public void OnLogin(View view) {
        if (!valid) {
            Toast.makeText(LoginActivity.this, "Invalid input. Please try again.",
                    Toast.LENGTH_LONG).show();
        } else {
            EditText user = findViewById(R.id.email_et);
            String email = String.valueOf(user.getText()).trim();

            EditText pass = findViewById(R.id.password_et);
            String password = String.valueOf(pass.getText()).trim();

            String type = "login";
            LoginBackground loginBackground = new LoginBackground(this);
            loginBackground.execute(type, email, password);
            finishAndRemoveTask();

        }
    }

    public void BtnRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}