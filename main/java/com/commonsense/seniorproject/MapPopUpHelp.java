package com.commonsense.seniorproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

class MapPopUpHelp extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pop_up_help);

        // get display size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        // set layout to a ratio of the display size
        getWindow().setLayout((int)(width*0.8),(int)(height*0.35));
    }

}
