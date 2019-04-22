package com.commonsense.seniorproject;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button confirm;
    private Button cancel;

    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = (EditText) findViewById(R.id.old_pass);
        newPassword = (EditText) findViewById(R.id.new_pass);
        confirmNewPassword = (EditText) findViewById(R.id.confirm_new_pass);
        confirm = (Button) findViewById(R.id.change_password_confirm);
        cancel = (Button) findViewById(R.id.change_password_cancel);

        // Progress Dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = session.getUserID();
                String oldP = oldPassword.getText().toString().trim();
                String newP = newPassword.getText().toString().trim();
                String conNewP = confirmNewPassword.getText().toString().trim();

                if (newP.equals(conNewP)) {
                    changePasswordInDatabase(userID, oldP, newP);
                } else {
                    newPassword.setError("Passwords don't match");
                    confirmNewPassword.setError("Passwords don't match");
                }
            }
        });

        //cancels changing the password and sends user back to profile fragment
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Attempts to check user's password information with what is in the database.  If correct, change password to new_password,
     * Otherwise, tell the user that the information was wrong and prompt the user to try again
     *
     * @param userID
     * @param old_password
     * @param new_password
     */
    private void changePasswordInDatabase(final String userID, final String old_password, final String new_password) {
        //tag used to cancel the request
        String tag_string_req = "req_update";

        pDialog.setMessage("Updating Info...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, CommonSenseConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", "update response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in JSON
                    if (!error) {
                        // User successfully updated

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
                Log.e("TAG", "Update error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("old_password", old_password);
                params.put("new_password", new_password);

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
}
