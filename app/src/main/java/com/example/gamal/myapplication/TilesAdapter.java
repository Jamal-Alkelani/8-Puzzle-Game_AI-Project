package com.example.gamal.myapplication;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.TimeKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TilesAdapter extends RecyclerView.Adapter<TilesAdapter.TilesViewHolder> {

    List<Tile> tiles;
    Context mContext;
    onTileClickedListner listner;

    public interface onTileClickedListner {
        void onTileClicked(int position);
    }

    public TilesAdapter(Context context, List<Tile> tilesList, onTileClickedListner listner) {
        this.mContext = context;
        this.tiles = tilesList;
        this.listner = listner;
    }

    @NonNull
    @Override
    public TilesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, viewGroup, false);
        TilesViewHolder tilesViewHolder = new TilesViewHolder(view);
        return tilesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TilesViewHolder tilesViewHolder, int i) {
        if (tiles.get(i).getNumber() == 9) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tilesViewHolder.tile_btn.setBackground(mContext.getDrawable(R.drawable.exclamation_red));
            }
        } else
            tilesViewHolder.tile_btn.setText(tiles.get(i).getNumber() + "");

    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    @Override
    public int getItemCount() {
        if (tiles == null)
            return 0;
        return tiles.size();
    }

    public class TilesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button tile_btn;

        public TilesViewHolder(@NonNull View itemView) {
            super(itemView);
            tile_btn = itemView.findViewById(R.id.rv_btn);
            tile_btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            listner.onTileClicked(getAdapterPosition() + 1);
        }
    }
}
