package com.TJokordeGdeAgungAbelPutra.jbus_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;

import java.util.ArrayList;

public class BusArrayAdapter  extends ArrayAdapter<Bus> {
    public BusArrayAdapter(@NonNull Context ctx, ArrayList<Bus> busList){
        super(ctx,0,busList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View currentItemView = convertView;
        if(currentItemView == null){
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view,parent,false);
        }

        Bus currentNumberPosition = getItem(position);

        TextView busName = currentItemView.findViewById(R.id.busname);
        busName.setText(currentNumberPosition.toString());

        TextView busDesc = currentItemView.findViewById(R.id.description);
        busDesc.setText(currentNumberPosition.departure.stationName + " Tujuan " + currentNumberPosition.arrival.stationName);

        return currentItemView;
    }
}
