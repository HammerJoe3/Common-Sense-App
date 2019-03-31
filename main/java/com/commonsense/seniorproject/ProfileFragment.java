package com.commonsense.seniorproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Fragment;

public class ProfileFragment extends Fragment {

    private View view;

    private Button logout;
    private Button genEst;
    private Button save;
    private Button cancel;
    private Button redeem;
    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
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
        redeem = (Button) view.findViewById(R.id.redeemPoints);
        inputFirstName = (EditText) view.findViewById(R.id.profileFirst);
        inputEmail = (EditText) view.findViewById(R.id.profileEmail);
        inputLastName = (EditText) view.findViewById(R.id.profileLast);
        session = new SessionManager(view.getContext().getApplicationContext());

        inputFirstName.setText(session.getFirstName());
        inputLastName.setText(session.getLastName());
        inputEmail.setText(session.getEmail());

        // the data in the editText boxes will be commit to firstName, lastName, and email
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = inputFirstName.getText().toString();
                String last = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();

                session.resetAllExecptID();
                session.setAllExceptID(first,last,email);
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

        return view;
    }

    // opens a new page where the user will be able to get an estimate
    /*private void generateEstimate(View view) {
        Intent intent = new Intent(getActivity(), EstimateActivity.class);
        startActivity(intent);
    }

    // if the user has enough points, a coupon will be sent to their email and the points deducted
    private void redeemPoints(View view) {

    }
*/

}
