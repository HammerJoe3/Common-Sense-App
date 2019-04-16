package com.commonsense.seniorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EstimateActivity extends AppCompatActivity {

    private EditText square_feet_input;
    private EditText length;
    private EditText width;
    private EditText height;
    private Button estimate;
    private TextView cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate);

        square_feet_input = (EditText) findViewById(R.id.input_square_feet);
        estimate = (Button) findViewById(R.id.getEstimate);
        length = (EditText) findViewById(R.id.length);
        width = (EditText) findViewById(R.id.width);
        height = (EditText) findViewById(R.id.height);
        cost = (TextView) findViewById(R.id.cost_estimate);


        estimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int squareFeet = 0;

                if(square_feet_input.length() > 0) {
                    String squareFeetString = square_feet_input.getText().toString();
                    getCost(Integer.parseInt(squareFeetString));
                } else if (length.length() == 0 || width.length() == 0 || height.length() == 0) {
                    Toast.makeText(EstimateActivity.this, "Error: length, width, and height must be greater than 0. The Estimate is incorrect.", Toast.LENGTH_SHORT).show();
                } else {
                    String lengthS = length.getText().toString();
                    String widthS = width.getText().toString();
                    String heightS = height.getText().toString();
                    int l = Integer.parseInt(lengthS);
                    int w = Integer.parseInt(widthS);
                    int h = Integer.parseInt(heightS);
                    getCost(l*w*h);
                }


                }
            }
        );
    }

    public void getCost(int squareFeet) {
        //this is for the standard product
        final int costPerLiter = 6;
        //.0456 is the coverage factor this is a company standard
        double totalOZ = squareFeet * .0456;
        //Conversion to milliliters
        double totalML = totalOZ/ .033814;
        double netTotal = (totalML/1000) * costPerLiter;
        // the .20 is for freight percentage
        double totalProductCost = (netTotal * .20) + netTotal;
        // plus 25 for minutes to set up and takedown  .. 1.4 is standard company machine fog rate
        double minutesOfLabor = ( totalOZ/ 1.4) +25;
        double laborCost = (minutesOfLabor / 60) * 25.00;
        double finalCost = totalProductCost + laborCost;
        int value = (int) finalCost;
        Log.d("Total: ", String.valueOf(value));
        cost.setText("$" + String.valueOf(value));
    }
}
