package com.example.gamal.myapplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends AppCompatActivity implements TilesAdapter.onTileClickedListner {
    private RecyclerView rv_tiles;
    TilesAdapter.onTileClickedListner listner;
    List<Tile> tiles;
    int currentPos=9;
    private int moves=0;
    PopupWindow changeSortPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rv_tiles=findViewById(R.id.rv_tiles);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        listner=this;
        tiles=new ArrayList<>();
        initilizeTiles();
        TilesAdapter adapter=new TilesAdapter(this,tiles,listner);
        rv_tiles.setLayoutManager(gridLayoutManager);
        rv_tiles.setAdapter(adapter);
        rv_tiles.setHasFixedSize(true);

    }


    private void initilizeTiles() {
        for (int i=1;i<10;i++){
            Tile tile=new Tile();
            tile.setNumber(i);
            tile.setPosition(i);
            tiles.add(tile);
        }
    }

    public void restartGame(View view){
        tiles.clear();
        currentPos=9;
        initilizeTiles();
        moves=0;
        updateMoves_tv();
        setRecylceViewAdapter();
    }

    private void updateMoves_tv() {
        TextView tv_moves=findViewById(R.id.moves);
        tv_moves.setText("Moves : "+moves);

    }

    @Override
    public void onTileClicked(int position) {
        if (isValidPosition(position)) {
            swapTiles(position,currentPos);
            currentPos=position;
            setRecylceViewAdapter();
            moves++;
            updateMoves_tv();
            if (isWin()) {
                Toast.makeText(this, "You Win", Toast.LENGTH_SHORT).show();

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                showSortPopup(this,new Point(height/2,width/2));

            }
        }
    }

    private void setRecylceViewAdapter() {
        TilesAdapter adapter=new TilesAdapter(this,tiles,listner);
        adapter.setTiles(tiles);
        rv_tiles.setAdapter(adapter);
    }

    private void swapTiles(int position, int currentPos) {
        Collections.swap(tiles,position-1,currentPos-1);
    }

    private boolean isValidPosition(int position) {
        switch (currentPos){
            case 1:
                if(position==2 || position==4)
                    return true;
                break;
            case 2:
                if (position==1 ||position==3||position==5)
                    return true;
                break;
            case 3:
                if (position==2||position==6)
                    return true;
                break;
            case 4:
                if (position==1||position==5||position==7)
                    return true;
                break;
            case 5:
                if (position==2||position==4||position==6||position==8)
                    return true;
                break;
            case 6:
                if (position==3||position==5||position==9)
                    return true;
                break;
            case 7:
                if (position==4||position==8)
                    return true;
                break;
            case 8:
                if (position==5||position==7||position==9)
                    return true;
                 break;
            case 9:
                if (position==6||position==8)
                    return true;
                break;
        }
        return false;
    }

    public boolean isWin(){
        for (int i=0;i<tiles.size()-1;i++){
            if (tiles.get(i).getNumber()>tiles.get(i+1).getNumber())
                return false;
        }
        return true;
    }
    private void showSortPopup(final Activity context, Point p) {
        // Inflate the popup_layout.xml
        ConstraintLayout viewGroup = (ConstraintLayout) context.findViewById(R.id.main_layout);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.sort_popup_layout, viewGroup,false);

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
        changeSortPopUp.showAtLocation(layout, Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0 + OFFSET_X, 0 + OFFSET_Y);


        // Getting a reference to Close button, and close the popup when clicked.
        Button close = (Button) layout.findViewById(R.id.close);
        TextView tv_moves_popup=layout.findViewById(R.id.tv_moves_popup);
        tv_moves_popup.setText("Moves : "+moves
        );
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
                restartGame(v);
            }
        });

    }

}
