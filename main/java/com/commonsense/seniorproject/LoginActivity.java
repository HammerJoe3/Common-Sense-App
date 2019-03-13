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
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    private Button login;
    private Button createAccount;
    private Button forgotPassword;  // In case we need to make one later
    private EditText inputEmail;
    private EditText inputPassword;

    private ProgressDialog pDialog;

    // Can be used later if we want to save information for later use
    //private SessionManager session;

    /**
     * onCreate sets the visuals from the activity_login.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_sign_in_button);
        createAccount = (Button) findViewById(R.id.login_register_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        // Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session Manager
        // session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        // if (session.isLoggedIn()) {
        //      launch news activity;
        // }

        /**
         * When login button is pressed, check if email and password are valid and non-empty
         * then call CheckLogin
         * @param view
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check if the email has the correct email format (example@whatever.blank)
                if (!isEmailValid(email)) {
                    inputEmail.setError("Please enter a valid email");
                }

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    //login user
                    checkLogin(email, password);
                } else {
                    // Prompt the user to enter credentials
                    if (email.isEmpty()) {
                        inputEmail.setError("Please enter a valid email");
                    } else {
                        inputPassword.setError("Please enter your password");
                    }
                }
            }
        });
    }

    /**
     * Checks if email is in correct email format (example@whatever.blank)
     * @param email
     * @return
     */
    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Attempts to check user's login information with what is in the database.  If correct, proceed to the NavigationActivity,
     * Otherwise, tell the user that the information was wrong and prompt the user to try again
     * @param email
     * @param password
     */
    private void checkLogin(final String email, final String password) {
        //tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST, CommonSenseConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", "login response: " + response);
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

                        launchNavigationActivity();
                    } else {
                        // error in login, get Error Message
                        String errorMsg = jObj.getString("error_msg");
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
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void launchNavigationActivity() {
        Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
        startActivity(intent);
    }
}
