package com.example.gamal.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private PopupWindow changeSortPopUp;
    Context mContext;
    public static ArrayList<Integer> moves = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }

    public void newGameClicked(View view) {
        showSortPopup(this, new Point(0, 0));
    }

    public void optionsClicked(View view) {
        String b[][] = {{"5", "2", "3"}, {"4", "1", "6"}, {"7", "", "8"}};
        String c[][] = {{"1", "2", "6"}, {"8", "5", ""}, {"7", "4", "3"}};
        Solution solution = new Solution();
        solution.set_a(c);
        solution.test();

    }

    public void exitClicked(View view) {

    }

    private void showSortPopup(final Activity context, Point p) {
        // Inflate the popup_layout.xml
        ConstraintLayout viewGroup = (ConstraintLayout) context.findViewById(R.id.choose_playing_mode_layout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.playing_mode, viewGroup, false);

        // Creating the PopupWindow
        changeSortPopUp = new PopupWindow(context);
        changeSortPopUp.setContentView(layout);
//        changeSortPopUp.setWidth(200);
        changeSortPopUp.setHeight(750);
        changeSortPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = 0;
        int OFFSET_Y = 0;

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());


        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0 + OFFSET_X, 0 + OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button ai = (Button) layout.findViewById(R.id.btn_ai);
        Button human = layout.findViewById(R.id.btn_human);
        Button close = layout.findViewById(R.id.close_popup);
        ai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
                Intent intent = new Intent(mContext, Game.class);
                intent.putExtra(Game.PLAYING_MODE, "ai");
                startActivity(intent);
            }
        });

        human.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
                Intent intent = new Intent(mContext, Game.class);
                intent.putExtra(Game.PLAYING_MODE, "human");
                startActivity(intent);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSortPopUp.dismiss();
            }
        });

    }
}
