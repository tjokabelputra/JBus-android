package com.TJokordeGdeAgungAbelPutra.jbus_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;

import java.util.List;

public class seatSelectedAdapter extends ArrayAdapter<String> {
    public seatSelectedAdapter(@NonNull Context context, List<String> seats) {
        super(context, 0, seats);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        String currentSeat = getItem(position);
        TextView departureTimeTextView = currentItemView.findViewById(android.R.id.text1);
        departureTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        departureTimeTextView.setText(currentSeat);
        return currentItemView;
    }
}
