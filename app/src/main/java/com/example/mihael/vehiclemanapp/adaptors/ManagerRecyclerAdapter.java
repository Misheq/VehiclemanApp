package com.example.mihael.vehiclemanapp.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Manager;

/**
 * Addapter which recycles objects to view
 */

public class ManagerRecyclerAdapter extends RecyclerView.Adapter<ManagerRecyclerAdapter.MyViewHolder> {

    private List<Manager> managers;

    public ManagerRecyclerAdapter(List<Manager> managers) {
        this.managers = managers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.id.setText(String.valueOf(managers.get(position).getManagerId()));
        holder.firstName.setText(managers.get(position).getFirstName());
        holder.lastName.setText(managers.get(position).getLastName());
        holder.email.setText(managers.get(position).getEmail());
        holder.phone.setText(managers.get(position).getPhone());
        holder.company.setText(managers.get(position).getCompanyName());
    }

    @Override
    public int getItemCount() {
        if (managers == null) {
            return 0;
        }
        return managers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, firstName, lastName, email, phone, company;

        public MyViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            company = itemView.findViewById(R.id.companyName);
        }
    }
}