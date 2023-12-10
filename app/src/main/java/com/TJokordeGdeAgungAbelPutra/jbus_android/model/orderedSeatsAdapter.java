package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;

import java.util.List;

public class orderedSeatsAdapter extends RecyclerView.Adapter<orderedSeatsAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mData;

    public orderedSeatsAdapter(Context context, List<String> orderedSeats) {
        mContext = context;
        mData = orderedSeats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String seat = mData.get(position);
        holder.bind(seat);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<String> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView seatInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seatInfo = itemView.findViewById(R.id.seat);
        }

        public void bind(String seat) {
            seatInfo.setText(seat);
        }
    }
}


