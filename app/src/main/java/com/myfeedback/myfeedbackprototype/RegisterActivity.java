package com.myfeedback.myfeedbackprototype;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, email, pass, age, IC, address;
    String str_fname, str_lname, str_email, str_pass, str_age, str_IC, str_Address, type;
    boolean valid = false;
    private ConstraintLayout constraintLayout;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        constraintLayout = findViewById(R.id.registerConstLayout);

        //First name validation
        fname = findViewById(R.id.regFname);
        (findViewById(R.id.regFname)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(fname.getText().toString())) {
                        fname.setError("Please fill in the blank.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //Last name validation
        lname = findViewById(R.id.regLname);
        (findViewById(R.id.regLname)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(lname.getText().toString())) {
                        lname.setError("Please fill in the blank.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //Email validation
        email = findViewById(R.id.regEmail);
        (findViewById(R.id.regEmail)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(email.getText().toString())) {
                        email.setError("Please fill in the blank.");
                        valid = false;
                    } else if (!isEmailValid(email.getText().toString())) {
                        email.setError("Please enter a valid email address format.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //Password validation
        pass = findViewById(R.id.regPassword);
        (findViewById(R.id.regPassword)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(pass.getText().toString())) {
                        pass.setError("Please fill in the blank.");
                        valid = false;
                    } else if (pass.getText().toString().length() < 8 && !isValidPassword(pass.getText().toString())) {
                        pass.setError("Password must be more than 8 and no illegal characters.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //Age validation
        age = findViewById(R.id.regAge);
        (findViewById(R.id.regAge)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(age.getText().toString())) {
                        age.setError("Please fill in the blank.");
                        valid = false;
                    } else if (age.getText().toString().length() > 3 && Integer.parseInt(age.getText().toString()) < 1 && Integer.parseInt(age.getText().toString()) > 200) {
                        age.setError("Password must be more than 8 and no illegal characters.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //IC validation
        IC = findViewById(R.id.regIC);
        (findViewById(R.id.regIC)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(IC.getText().toString())) {
                        IC.setError("Please fill in the blank.");
                        valid = false;
                    } else if (IC.getText().toString().length() != 12 || IC.getText().toString().contains("-")) {
                        IC.setError("Please enter exactly 12 digits IC and without hyphen(-).");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });

        //Address validation
        address = findViewById(R.id.regAddress);
        (findViewById(R.id.regAddress)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (isEmptyString(address.getText().toString())) {
                        address.setError("Please fill in the blank.");
                        valid = false;
                    } else {
                        valid = true;
                    }
                }
            }
        });
    }

    public void OnRegister(View view){
        if (!valid) {
            Toast.makeText(RegisterActivity.this, "Invalid input. Please try again.",
                    Toast.LENGTH_LONG).show();
        } else {
            str_fname = String.valueOf(fname.getText()).trim();
            str_lname = String.valueOf(lname.getText()).trim();
            str_email = String.valueOf(email.getText()).trim();
            str_pass = String.valueOf(pass.getText()).trim();
            str_age = String.valueOf(age.getText()).trim();
            str_IC = String.valueOf(IC.getText()).trim();
            str_Address = String.valueOf(address.getText()).trim();
            type = "register";

            RegisterBackground registerBackground = new RegisterBackground(this);
            registerBackground.execute(type, str_fname, str_lname, str_age, str_email, str_IC, str_Address, str_pass);
        }
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
}

