    package com.TJokordeGdeAgungAbelPutra.jbus_android.adapter;

    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;

    import com.TJokordeGdeAgungAbelPutra.jbus_android.UI.EditScheduleActivity;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.UI.ManageBusActivity;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.R;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.BaseResponse;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.model.Bus;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.BaseApiService;
    import com.TJokordeGdeAgungAbelPutra.jbus_android.request.UtilsApi;

    import java.util.List;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class busListAdapter extends ArrayAdapter<Bus> {
        private BaseApiService mApiService;
        private Context mContext;
        private AlertDialog alertDialog;
        public busListAdapter(@NonNull Context context, List<Bus> busList){
            super(context,0,busList);
            mApiService = UtilsApi.getApiService();
            mContext = context;
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

            ImageView scheduleSet = currentItemView.findViewById(R.id.schedule);
            scheduleSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ManageBusActivity.selectedBus = getItem(position);
                    Intent intent = new Intent(getContext(), EditScheduleActivity.class);
                    getContext().startActivity(intent);
                }
            });

            ImageView deleteBus = currentItemView.findViewById(R.id.delete);
            deleteBus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ManageBusActivity.selectedBus = getItem(position);
                    showDeleteBusPopup(getItem(position));
                }
            });
            return currentItemView;

        }

        private void showDeleteBusPopup(Bus bus) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            View deleteBusLayout = LayoutInflater.from(mContext).inflate(R.layout.deletebus_popup, null);
            builder.setView(deleteBusLayout);

            TextView cancelDelete = deleteBusLayout.findViewById(R.id.cancel_delete);
            TextView confirmDelete = deleteBusLayout.findViewById(R.id.confirm_delete);

            alertDialog = builder.create();

            cancelDelete.setOnClickListener(v -> {
                alertDialog.dismiss();
            });

            confirmDelete.setOnClickListener(v -> {
                alertDialog.dismiss();
                handleDeleteBus(bus.id);
                Toast.makeText(mContext, "Bus deleted", Toast.LENGTH_SHORT).show();
            });

            alertDialog.show();
        }

        private void handleDeleteBus(int busId){
            mApiService.deleteBus(busId).enqueue(new Callback<BaseResponse<Bus>>() {
                @Override
                public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(mContext, "App Error", Toast.LENGTH_SHORT).show();
                    }
                    removeBusFromList(busId);
                    notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void removeBusFromList(int busId) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).id == busId) {
                    remove(getItem(i));
                    break;
                }
            }
        }
    }
