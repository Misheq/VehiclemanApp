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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.PersonRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CONNECTION_FAILED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DELETE_PERSON_SUCCESSFUL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_MESSAGE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_NO;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_TITLE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_YES;

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
    private TextView mNoEmployeesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        loadPersonList();
    }

    private void loadPersonList() {

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mNoEmployeesTextView = findViewById(R.id.tv_no_persons);

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
                    if(persons.size() == 0) {
                        displayNoPersonMessage();
                    } else {
                        hideNoPersonMessage();
                    }

                    personRecyclerAdapter = new PersonRecyclerAdapter(persons);
                    recyclerView.setAdapter(personRecyclerAdapter);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(PersonsActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(PersonsActivity.this, CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
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
        alertDialogBuilder.setTitle(DIALOG_BOX_TITLE);

        // set dialog message
        alertDialogBuilder
                .setMessage(DIALOG_BOX_MESSAGE)
                .setCancelable(false)
                .setPositiveButton(DIALOG_BOX_YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Button button = view.findViewById(R.id.buttonDelete);
                        int personId = Integer.parseInt((String) button.getTag());

                        deletePersonById(personId);
                    }
                })
                .setNegativeButton(DIALOG_BOX_NO, new DialogInterface.OnClickListener() {
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
                    Toast.makeText(PersonsActivity.this, DELETE_PERSON_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                    loadPersonList();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(PersonsActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(PersonsActivity.this, CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // helpers

    private void displayNoPersonMessage() {
        mNoEmployeesTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoPersonMessage() {
        mNoEmployeesTextView.setVisibility(View.INVISIBLE);
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
