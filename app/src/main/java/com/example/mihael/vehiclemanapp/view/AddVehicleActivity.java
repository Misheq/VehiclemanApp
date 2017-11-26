package com.example.mihael.vehiclemanapp.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.DatePickerFragment;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;
import com.example.mihael.vehiclemanapp.helpers.SpinnerLoader;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CONNECTION_FAILED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.CREATE_VEHICLE_SUCCESSFUL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_EMPLOYEE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SERVICING_DATE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.TAP_TO_SET_SERVICING_DATE;

/**
 * Activity which is responsible for creating vehicles
 */

public class AddVehicleActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private View view;
    private SpinnerLoader spinnerLoader;
    private EditText type;
    private EditText registration;
    private EditText color;
    private EditText description;
    private TextView serviceDate;
    private Spinner spinnerWithPersons;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, -1);
        spinnerLoader.loadPersonsSpinnerForVehicle();
    }

    private void setVehicleFromForm() {
        InputValidator inVal = new InputValidator(this.view);

        if (inVal.isVehicleInputValid()) {
            Vehicle vehicle = createVehicleFromUi();
            vehicle = setPersonForVehicle(vehicle);

            saveVehicle(vehicle);
        }
    }

    private void saveVehicle(Vehicle vehicle) {

        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Vehicle> call = apiInterface.createVehicle(LoginManager.getLogedInManagerToken(), vehicle);
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 201) {
                    Toast.makeText(AddVehicleActivity.this, CREATE_VEHICLE_SUCCESSFUL, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddVehicleActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Vehicle> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(AddVehicleActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    ////////////// helpers //////////////

    private void getUiElements() {
        type = findViewById(R.id.inputType);
        registration = findViewById(R.id.inputRegistration);
        color = findViewById(R.id.inputColor);
        description = findViewById(R.id.inputDescription);
        serviceDate = findViewById(R.id.inputServiceDate);
        spinnerWithPersons = findViewById(R.id.spinnerPersons);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
    }

    private Vehicle createVehicleFromUi() {
        getUiElements();

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleType(type.getText().toString());
        vehicle.setRegistrationNumber(registration.getText().toString());
        vehicle.setManagerId(LoginManager.getManagerId());
        vehicle.setColor(color.getText().toString());
        vehicle.setDescription(description.getText().toString());

        if (!serviceDate.toString().equals(TAP_TO_SET_SERVICING_DATE)) {
            vehicle.setServicingDate(serviceDate.getText().toString().replace(SERVICING_DATE, ""));
        }

        return vehicle;
    }

    private Vehicle setPersonForVehicle(Vehicle vehicle) {
        Object selectedPerson = spinnerWithPersons.getSelectedItem();

        if (!selectedPerson.toString().equals(SELECT_EMPLOYEE)) {
            Person person = (Person) selectedPerson;
            vehicle.setAssigneeId(String.valueOf(person.getPersonId()));
            vehicle.setPerson(person);
        } else {
            vehicle.setAssigneeId("");
        }

        return vehicle;
    }

    // date picker logic
    public void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "dialog");
    }

    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        ((TextView) findViewById(R.id.inputServiceDate))
                .setText(SERVICING_DATE + dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.SAVE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            setVehicleFromForm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
