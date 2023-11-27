    package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;

    import com.TJokordeGdeAgungAbelPutra.jbus_android.R;

    import java.util.ArrayList;
    import java.util.List;

    public class busListAdapter extends ArrayAdapter<Bus> {
        public busListAdapter(@NonNull Context context, List<Bus> busList){
            super(context,0,busList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View currentItemView = convertView;

            if(currentItemView == null){
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_manage_view, parent, false);
            }
            Bus currentBusPosition = getItem(position);

            TextView busName = currentItemView.findViewById(R.id.bus_name);
            busName.setText(currentBusPosition.name);

            return currentItemView;
        }
    }
