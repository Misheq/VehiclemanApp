package com.example.mihael.vehiclemanapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.PersonRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for listing existing persons from database
 */

public class PersonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PersonRecyclerAdapter personRecyclerAdapter;
    private List<Person> persons;
    private ApiInterface apiInterface;
    private final Context context = this;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        loadPersonList();
    }

    private void loadPersonList() {

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<List<Person>> call = apiInterface.getPersonsForManager(LoginManager.getLogedInManagerToken(), LoginManager.getManagerId()); // was getPersons
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (statusCode == 200) {
                    persons = response.body();
                    personRecyclerAdapter = new PersonRecyclerAdapter(persons);
                    recyclerView.setAdapter(personRecyclerAdapter);
                } else {
                    Toast.makeText(PersonsActivity.this, "Loading persons failed. Status: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(PersonsActivity.this, "Loading persons failed.\nPlease check internet connection", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", "something is wrong");
            }
        });
    }

    private void startAddPersonActivity() {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }

    public void alertForDelete(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Delete confirmation");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this element?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Button button = view.findViewById(R.id.buttonDelete);
                        int personId = Integer.parseInt((String) button.getTag());

                        deletePersonById(personId);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void deletePersonById(int personId) {

        mLoadingIndicator.setVisibility(View.VISIBLE);

        Call<Void> call = apiInterface.deletePerson(LoginManager.getLogedInManagerToken(), personId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (statusCode == 204) {
                    Toast.makeText(PersonsActivity.this, "Person deleted successfully", Toast.LENGTH_SHORT).show();
                    loadPersonList();
                } else {
                    Toast.makeText(PersonsActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(PersonsActivity.this, "Delete went wrong", Toast.LENGTH_SHORT).show();
                Log.d("MY_TAG", "Delete went wrong");
            }
        });
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.NEW);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            startAddPersonActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
