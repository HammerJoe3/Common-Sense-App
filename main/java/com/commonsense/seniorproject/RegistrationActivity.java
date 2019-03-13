package com.commonsense.seniorproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A registration screen that allows users to register and be entered into the database
 */
public class RegistrationActivity extends AppCompatActivity {

    private Button register;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;

    private ProgressDialog pDialog;

    // Can be used later if we want to save information for later use
    //private SessionManager session;

    /**
     * onCreate sets the visuals from the activity_registration.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        register = (Button) findViewById(R.id.registeration_button);
        inputFirstName = (EditText) findViewById(R.id.first_name);
        inputLastName = (EditText) findViewById(R.id.last_name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session Manager
        // session = new SessionManager(getApplicationContext());

        /**
         * When login button is pressed, check if email and password are valid and non-empty
         * then call CheckLogin
         * @param view
         */
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check if the email has the correct email format (example@whatever.blank)
                if (!isEmailValid(email)) {
                    inputEmail.setError("Please enter a valid email");
                }

                // Check for empty data in the form
                if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    // register user
                    checkRegistration(firstName, lastName, email, password);
                } else {
                    // Prompt the user to enter credentials
                    if (firstName.isEmpty() || lastName.isEmpty()) {
                        inputFirstName.setError("Please enter your name");
                    } else if (email.isEmpty()) {
                        inputEmail.setError("Please enter a valid email");
                    } else {
                        inputPassword.setError("Please enter your password");
                    }
                }
            }
        });
    }

    /**
     * Attempts to register the user and add them into the database.  If successful, return to login,
     * Otherwise, tell the user that the information was wrong and prompt the user to try again
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    private void checkRegistration(final String firstName, final String lastName, final String email, final String password) {
        //tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, CommonSenseConfig.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", "Register response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in JSON
                    if (!error) {
                        // User successfully logged in
                        // TODO: Create Login session here
                        // session.setLogin(true);

                        // String userID = jObj.getString("userID");
                        // session.setUserID(userID);

                        finishRegistration();
                        Toast.makeText(getApplicationContext(), "User successfully registered.", Toast.LENGTH_LONG).show();

                    } else {
                        // error in login, get Error Message
                        String errorMsg = jObj.getString("error_msg");
                        inputEmail.setError("Email already exists in our database");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "Login error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", firstName);
                params.put("last_name", lastName);
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Checks if email is in correct email format (example@whatever.blank)
     * @param email
     * @return
     */
    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void finishRegistration() {
        finish();
    }
}
