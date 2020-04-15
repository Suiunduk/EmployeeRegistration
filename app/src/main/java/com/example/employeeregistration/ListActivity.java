package com.example.employeeregistration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employeeregistration.registrationActivity.RegistrationActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ListActivity extends MainActivity {

    private BottomNavigationView bottomNavigationView;
    private Bundle savedInstanceState;
    //this is the JSON Data URL
    //make sure you are using the correct ip else it will not work
    private static final String URL_PRODUCTS = "http://192.168.0.100:90/api_get.php";

    //a list to store all the employees
    private List<EmployeeInfo> employeeInfoList;

    private EmployeeAdapter adapter;
    //the recyclerview
    private RecyclerView recyclerView;

    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_employees);
        init();
        bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.go_to_list_button);

        onClickRegistrationButton();
        this.savedInstanceState = savedInstanceState;
        text2 = findViewById(R.id.text2);
        //initializing the employee
        employeeInfoList = new ArrayList<>();
        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmployeeAdapter(getApplicationContext(), employeeInfoList);
        recyclerView.setAdapter(adapter);

        //this method will fetch and parse json
        //to display it in recyclerview
        loadEmployees();
    }

    private void onClickRegistrationButton() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.go_to_registration_button:
                        Intent intent = new Intent(ListActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    public void init() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }


    private void loadEmployees() {
        RequestParams params = new RequestParams();
        WebReq.get(getApplicationContext(), "api_get.php", params, new ListActivity.ResponseHandler());
    }

    private class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response ", response.toString() + " ");
            try {
                //converting the string to json array object
                JSONArray array = response;

                //traversing through all the object
                for (int i = 0; i < array.length(); i++) {

                    //getting employee object from json array
                    JSONObject employee = array.getJSONObject(i);

                    //adding the employee to employee list
                    employeeInfoList.add(new EmployeeInfo(
                            employee.getInt("id"),
                            employee.getString("tel_number"),
                            employee.getString("name"),
                            employee.getString("second_name"),
                            employee.getString("role"),
                            employee.getString("photo")
                    ));
                }

                //creating adapter object and setting it to recyclerview
                adapter = new EmployeeAdapter(getApplicationContext(), employeeInfoList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
        }

        @Override
        public void onFinish() {
            super.onFinish();
        }
    }
}