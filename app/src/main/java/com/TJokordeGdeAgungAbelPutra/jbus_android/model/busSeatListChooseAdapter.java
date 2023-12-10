package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class busSeatListChooseAdapter extends BaseAdapter {
    private Context context;
    private List<Map.Entry<String, Boolean>> data;

    public busSeatListChooseAdapter(Context context, Map<String, Boolean> hashMap) {
        this.context = context;
        this.data = new ArrayList<>(hashMap.entrySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bus_seat_layout, parent, false);
        }

        TextView keyTextView = convertView.findViewById(R.id.seat_code);
        ImageView statusImage = convertView.findViewById(R.id.status);

        Map.Entry<String, Boolean> entry = data.get(position);
        String key = entry.getKey();
        keyTextView.setText(key);
        boolean isOccupied = entry.getValue();
        statusImage.setImageResource(isOccupied ? R.drawable.ic_baseline_check_24 : R.drawable.ic_baseline_clear_24);
        return convertView;
    }
}
