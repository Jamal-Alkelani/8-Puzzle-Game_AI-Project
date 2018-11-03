package com.example.gamal.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends AppCompatActivity implements TilesAdapter.onTileClickedListner {
    public static final String PLAYING_MODE = "PLAYING_MODE";
    private RecyclerView rv_tiles;
    TilesAdapter.onTileClickedListner listner;
    List<Tile> tiles;
    int currentPos = 9;
    private int moves = 0;
    boolean isHuman;
    PopupWindow changeSortPopUp;
    static ArrayList<String> ai_moves = new ArrayList<>();
    public int i = 0;
    public int ai_moves_size;
    AsyncTask task;
    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rv_tiles = findViewById(R.id.rv_tiles);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        listner = this;
        tiles = new ArrayList<>();
        initilizeTiles();
        TilesAdapter adapter = new TilesAdapter(this, tiles, listner);
        rv_tiles.setLayoutManager(gridLayoutManager);
        rv_tiles.setAdapter(adapter);
        rv_tiles.setHasFixedSize(true);
        Intent intent = getIntent();
        if (intent.hasExtra(PLAYING_MODE)) {
            if (intent.getStringExtra(PLAYING_MODE).equals("ai")) {
                isHuman = false;
                Button btn_forward_hint = findViewById(R.id.btn_forward_hint);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_forward_hint.setBackground(getDrawable(R.drawable.forward_button));
                }
            } else {
                isHuman = true;
                Button btn_forward_hint = findViewById(R.id.btn_forward_hint);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btn_forward_hint.setBackground(getDrawable(R.drawable.hint));
                }
            }
        }
    }

    private void doMove(String ai_move) {
        switch (ai_move) {
            case "UP":
                int pos = currentPos - 3;
                ai_doMove(pos);
                break;

            case "DOWN":
                pos = currentPos + 3;
                ai_doMove(pos);
                break;

            case "LEFT":
                pos = currentPos - 1;
                ai_doMove(pos);
                break;

            case "RIGHT":
                pos = currentPos + 1;
                ai_doMove(pos);
                break;
        }
    }

    public void start_ai_solving(View view) {
        if (isHuman) {
            String a[][] = new String[3][3];
            int o = 0;
            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[0][i] = "";
                else
                    a[0][i] = tiles.get(o).getNumber() + "";
                o++;
            }

            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[1][i] = "";
                else
                    a[1][i] = tiles.get(o).getNumber() + "";
                o++;
            }

            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[2][i] = "";
                else
                    a[2][i] = tiles.get(o).getNumber() + "";
                o++;
            }

//            task=null;
//            task=new AsyncTask() {
//                @Override
//                protected void onPostExecute(Object o) {
//                    super.onPostExecute(o);
//                    Log.e("xx",ai_moves.toString());
//                    ai_moves_size=ai_moves.size();
//                    doMove(ai_moves.get(0));
//                }
//
//                @Override
//                protected Object doInBackground(Object[] objects) {
//                    String [][]a= (String[][]) objects;
//                    Solution solution=new Solution();
//                    solution.set_a(a);
//                    solution.test();
//                    return null;
//                }
//            };
//            task.execute(a);
            Log.e("zz", "i'm a human");


            Solution solution = new Solution();
            solution.set_a(a);
            solution.test();
            solution.releaseResources();
            solution = null;
            ai_moves_size = ai_moves.size();
            TextView tv = findViewById(R.id.best_move);
            tv.setText("Best Moves :" + ai_moves_size);
            if (!isWin())
                doMove(ai_moves.get(0));
//                    doMove(ai_moves.get(0));
//                    Log.e("zz",ai_moves.get(0));
        } else {
            CountDownTimer countDownTimer = new CountDownTimer(1000 * ai_moves_size, 500) {
                @Override
                public void onTick(long l) {
                    if (i < ai_moves_size) {
                        doMove(ai_moves.get(i));
                        i++;
                    }
                }

                @Override
                public void onFinish() {
                }
            }.start();
        }
    }


    private void initilizeTiles() {
        for (int i = 1; i < 10; i++) {
            Tile tile = new Tile(i);
            tile.setNumber(i);
            tile.setPosition(i);
            tiles.add(tile);
        }
        shuffleTiles();
        while (!isPuzzleSolvable() && !isWin()) {
            shuffleTiles();

        }
        currentPos = getCurrentPosition();
        setRecylceViewAdapter();
        if (!isHuman) {
            String a[][] = new String[3][3];
            int o = 0;
            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[0][i] = "";
                else
                    a[0][i] = tiles.get(o).getNumber() + "";
                o++;
            }

            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[1][i] = "";
                else
                    a[1][i] = tiles.get(o).getNumber() + "";
                o++;
            }

            for (int i = 0; i < 3; i++) {
                if (tiles.get(o).getNumber() == 9)
                    a[2][i] = "";
                else
                    a[2][i] = tiles.get(o).getNumber() + "";
                o++;
            }

//            if(task==null) {
//                task = new AsyncTask() {
//                    @Override
//                    protected void onPostExecute(Object o) {
//                        super.onPostExecute(o);
//                        Log.e("xx", ai_moves.toString());
//                        ai_moves_size = ai_moves.size();
//                    }
//
//                    @Override
//                    protected Object doInBackground(Object[] objects) {
//                        String[][] a = (String[][]) objects;
//                        Solution solution = new Solution();
//                        solution.set_a(a);
//                        solution.test();
//                        return null;
//                    }
//                };
//                task.execute(a);
//            }
//            else
//                task.execute(a);

            Solution solution = new Solution();
            solution.set_a(a);
            solution.test();
            solution.releaseResources();
            solution = null;
            ai_moves_size = ai_moves.size();

            TextView tv = findViewById(R.id.best_move);
            tv.setText("Best Moves :" + ai_moves_size);
        }
    }

    private boolean isPuzzleSolvable() {

        int inv_count = 0;
        for (int num = 0; num < tiles.size() - 1; num++) {
            if (tiles.get(num).getNumber() == 9)
                continue;
            for (int num2 = num + 1; num2 < tiles.size(); num2++) {
                if (tiles.get(num).getNumber() > tiles.get(num2).getNumber())
                    inv_count++;
            }
        }
        Log.e("inv", inv_count + "");
        return inv_count % 2 == 0;
    }

    private int getCurrentPosition() {
        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).getNumber() == 9)
                return i + 1;
        }
        return -1;
    }

    private void shuffleTiles() {
        int shuffleNumbers = (int) (Math.random() * 3);
        Log.e("zz", shuffleNumbers + "");
        for (int i = 0; i < shuffleNumbers; i++) {
            int pos1 = (int) (Math.random() * 8) + 1;
            int pos2 = (int) (Math.random() * 8) + 1;
            swapTiles(pos1, pos2);
        }
        swapTiles(9, (int) (Math.random() * 8) + 1);

    }

    public void restartGame(View view) {
        tiles.clear();
        moves = 0;
        i = 0;
        ai_moves.clear();
        ai_moves_size = 0;
        initilizeTiles();
        if (isHuman)
            updateMoves_tv();
        setRecylceViewAdapter();
        TextView tv = findViewById(R.id.best_move);
        tv.setText("Best Moves :" + ai_moves_size);
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
    }

    private void updateMoves_tv() {
        TextView tv_moves = findViewById(R.id.moves);
        tv_moves.setText("Moves : " + moves);

    }

    public void ai_doMove(int position) {
        swapTiles(position, currentPos);
        currentPos = position;
        setRecylceViewAdapter();
        moves++;
        if (isWin()) {
            Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            showSortPopup(this, new Point(height / 2, width / 2));

        }
    }


    @Override
    public void onTileClicked(int position) {

        if (isValidPosition(position)) {
            changed = true;
            swapTiles(position, currentPos);
            currentPos = position;
            setRecylceViewAdapter();
            moves++;
            updateMoves_tv();
            if (isWin()) {
                Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                showSortPopup(this, new Point(height / 2, width / 2));

            }
        }
    }

    private void setRecylceViewAdapter() {
        TilesAdapter adapter = new TilesAdapter(this, tiles, listner);
        adapter.setTiles(tiles);
        rv_tiles.setAdapter(adapter);
    }

    private void swapTiles(int position, int currentPos) {

        Collections.swap(tiles, position - 1, currentPos - 1);
    }

    private boolean isValidPosition(int position) {
        switch (currentPos) {
            case 1:
                if (position == 2 || position == 4)
                    return true;
                break;
            case 2:
                if (position == 1 || position == 3 || position == 5)
                    return true;
                break;
            case 3:
                if (position == 2 || position == 6)
                    return true;
                break;
            case 4:
                if (position == 1 || position == 5 || position == 7)
                    return true;
                break;
            case 5:
                if (position == 2 || position == 4 || position == 6 || position == 8)
                    return true;
                break;
            case 6:
                if (position == 3 || position == 5 || position == 9)
                    return true;
                break;
            case 7:
                if (position == 4 || position == 8)
                    return true;
                break;
            case 8:
                if (position == 5 || position == 7 || position == 9)
                    return true;
                break;
            case 9:
                if (position == 6 || position == 8)
                    return true;
                break;
        }
        return false;
    }

    public boolean isWin() {
        for (int i = 0; i < tiles.size() - 1; i++) {
            if (tiles.get(i).getNumber() > tiles.get(i + 1).getNumber())
                return false;
        }
        return true;
    }

    private void showSortPopup(final Activity context, Point p) {
        // Inflate the popup_layout.xml
        ConstraintLayout viewGroup = (ConstraintLayout) context.findViewById(R.id.main_layout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort_popup_layout, viewGroup, false);

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
        Button close = (Button) layout.findViewById(R.id.close);
        TextView tv_moves_popup = layout.findViewById(R.id.tv_moves_popup);
        tv_moves_popup.setText("Moves : " + moves
        );
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
                restartGame(null);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Runtime.getRuntime().gc();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ee", "onDestroy");
    }
}
