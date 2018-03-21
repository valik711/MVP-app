package com.valentinfilatov.mvpapp.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.valentinfilatov.mvpapp.R;

public class CoordinateAdapter extends RecyclerView.Adapter<CoordinateAdapter.CoordinateHolder> {

    List<Coordinate> data = new ArrayList<>();

    @Override
    public CoordinateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new CoordinateHolder(view);
    }

    @Override
    public void onBindViewHolder(CoordinateHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Coordinate> coordinates) {
        data.clear();
        data.addAll(coordinates);
        notifyDataSetChanged();
    }

    static class CoordinateHolder extends RecyclerView.ViewHolder {

        TextView text;

        public CoordinateHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        void bind(Coordinate coord) {
            text.setText(String.format("id: %s, lat: %.6f, lng: %.6f, time: %s", coord.getId(), coord.getLat(), coord.getLng(), coord.getDate()));
        }
    }

}
