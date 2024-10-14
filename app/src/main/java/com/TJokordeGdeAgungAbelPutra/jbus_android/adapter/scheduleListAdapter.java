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
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Schedule;

import java.util.List;

public class scheduleListAdapter extends ArrayAdapter<Schedule> {

    public scheduleListAdapter(@NonNull Context context, List<Schedule> schedules) {
        super(context, 0, schedules);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        Schedule currentSchedulePosition = getItem(position);
        TextView departureTimeTextView = currentItemView.findViewById(android.R.id.text1);
        departureTimeTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        departureTimeTextView.setText(String.valueOf(currentSchedulePosition.departureSchedule));
        return currentItemView;
    }
}
