package com.example.mihael.vehiclemanapp.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.view.EditPersonActivity;

import java.util.List;

import static com.example.mihael.vehiclemanapp.helpers.Constants.EMPLOYEE;

/**
 * Person recycler adapter handles recycler view
 * for listing person row items
 */

public class PersonRecyclerAdapter extends RecyclerView.Adapter<PersonRecyclerAdapter.MyViewHolder> {

    private final List<Person> persons;

    public PersonRecyclerAdapter(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonRecyclerAdapter.MyViewHolder holder, int position) {
        holder.firstName.setText(persons.get(position).getFirstName());
        holder.lastName.setText(persons.get(position).getLastName());
        holder.email.setText(persons.get(position).getEmail());
        holder.deleteButton.setTag(String.valueOf(persons.get(position).getPersonId()));
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView firstName;
        final TextView lastName;
        final TextView email; // id, phone, company;
        final ImageButton deleteButton;

        public MyViewHolder(View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            email = itemView.findViewById(R.id.email);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            itemView.setOnClickListener(this);
        }

        /**
         * Get person p and start other activity with p
         */
        @Override
        public void onClick(View view) {
            Person p = persons.get(getLayoutPosition());
            Intent intent = new Intent(view.getContext(), EditPersonActivity.class);
            intent.putExtra(EMPLOYEE, p);
            view.getContext().startActivity(intent);
        }
    }
}
