package com.example.mihael.vehiclemanapp.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Vehicle;

import java.util.List;

public class VehicleRecyclerAdapter extends RecyclerView.Adapter<VehicleRecyclerAdapter.MyViewHolder> {

    private List<Vehicle> vehicles;

    public VehicleRecyclerAdapter(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(vehicles.get(position).getVehicleId()));
        holder.type.setText(vehicles.get(position).getVehicleType());
        holder.reg.setText(vehicles.get(position).getRegistrationNumber());
        holder.deleteButton.setTag(String.valueOf(vehicles.get(position).getVehicleId()));
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, type, reg;
        Button deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            type = itemView.findViewById(R.id.type);
            reg = itemView.findViewById(R.id.reg);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
