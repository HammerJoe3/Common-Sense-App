package com.commonsense.seniorproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private View view;

    private Button logout;
    private Button genEst;
    private Button save;
    private Button cancel;
    private Button coupon;
    private Button change_password;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;

    private ProgressDialog pDialog;
    private SessionManager session;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        // get the reference of Button

        logout = (Button) view.findViewById(R.id.logout);
        genEst = (Button) view.findViewById(R.id.genEst);
        save = (Button) view.findViewById(R.id.save);
        cancel = (Button) view.findViewById(R.id.cancel);
        coupon = (Button) view.findViewById(R.id.redeemPoints);
        change_password = (Button) view.findViewById(R.id.change_password);
        inputFirstName = (EditText) view.findViewById(R.id.profileFirst);
        inputEmail = (EditText) view.findViewById(R.id.profileEmail);
        inputLastName = (EditText) view.findViewById(R.id.profileLast);

        // Progress Dialog
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);

        // Session Manager
        session = new SessionManager(view.getContext().getApplicationContext());

        inputFirstName.setText(session.getFirstName());
        inputLastName.setText(session.getLastName());
        inputEmail.setText(session.getEmail());

        // the data in the editText boxes will be commit to firstName, lastName, and email
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userID = session.getUserID();
                String first = inputFirstName.getText().toString();
                String last = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();

                // Check if the email has the correct email format (example@whatever.blank)
                if (!isEmailValid(email)) {
                    inputEmail.setError("Please enter a valid email");
                }

                updateUserInDatabase(userID, first, last, email);
            }
        });

        //logs the user out of the app
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.resetAllOnLogout();
                getActivity().finish();
            }
        });

        // the data in the editText boxes will be reset to the last commit and no edits will be made
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputFirstName.setText(session.getFirstName());
                inputLastName.setText(session.getLastName());
                inputEmail.setText(session.getEmail());
            }
        });

        genEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EstimateActivity.class);
                startActivity(intent);
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // opens a new page where the user will be able to get an estimate
    private void generateEstimate(View view) {

    }

    // if the user has enough points, a coupon will be sent to their email and the points deducted
    private void redeemPoints(View view) {

    }



    /**
     * Attempts to check user's login information with what is in the database.  If correct, proceed to the NavigationActivity,
     * Otherwise, tell the user that the information was wrong and prompt the user to try again
     *
     * @param first_name
     * @param last_name
     * @param email
     */
    private void updateUserInDatabase(final String userID, final String first_name, final String last_name, final String email) {
        //tag used to cancel the request
        String tag_string_req = "req_update";

        pDialog.setMessage("Updating Info...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, CommonSenseConfig.URL_UPDATE_USER_INFO, new Response.Listener<String>() {
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
                        session.resetAllExecptID();
                        session.setAllExceptID(first_name, last_name, email);

                    } else {
                        // error in login, get Error Message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(view.getContext().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(view.getContext().getApplicationContext(), "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "Update error: " + error.getMessage());
                Toast.makeText(view.getContext().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userID", userID);
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("email", email);

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

    /**
     * Checks if email is in correct email format (example@whatever.blank)
     * @param email
     * @return
     */
    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
