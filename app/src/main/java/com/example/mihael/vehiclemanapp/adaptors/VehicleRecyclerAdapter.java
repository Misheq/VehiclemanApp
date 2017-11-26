package com.example.mihael.vehiclemanapp.adaptors;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.view.EditVehicleActivity;

import java.util.List;

import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLE;

/**
 * Vehicle recycler adapter handles recycler view
 * for listing vehicle row items
 */

public class VehicleRecyclerAdapter extends RecyclerView.Adapter<VehicleRecyclerAdapter.MyViewHolder> {

    private final List<Vehicle> vehicles;

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
        holder.type.setText(vehicles.get(position).getVehicleType());
        holder.reg.setText(vehicles.get(position).getRegistrationNumber());
        holder.deleteButton.setTag(String.valueOf(vehicles.get(position).getVehicleId()));
    }

    @Override
    public int getItemCount() {
        return vehicles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView type;
        final TextView reg;
        final ImageButton deleteButton;

        MyViewHolder(View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.type);
            reg = itemView.findViewById(R.id.reg);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Vehicle v = vehicles.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), EditVehicleActivity.class);
            intent.putExtra(VEHICLE, v);
            view.getContext().startActivity(intent);
        }
    }
}
