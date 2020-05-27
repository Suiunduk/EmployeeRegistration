package com.example.employeeregistration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EmployeeInfoActivity extends AppCompatActivity {

    private String id;
    private String phone_number;
    private String first_name;
    private String second_name;
    private String third_name;
    private String address;
    private String birth_date;
    private String passport_number;
    private String passport_org;
    private String password;
    private String role;
    private String photo;

    ImageView iv_photo;
    TextView tv_id;
    TextView tv_phone_number;
    TextView tv_first_name;
    TextView tv_second_name;
    TextView tv_third_name;
    TextView tv_address;
    TextView tv_birth_date;
    TextView tv_passport_number;
    TextView tv_passport_org;
    TextView tv_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);

        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), ListActivity.class);
                startActivity(i);
                finish();
            }
        });
        initialize();
        setValues();

    }



    private void initialize() {
        id = getIntent().getExtras().getString("employee_id");
        first_name = getIntent().getExtras().getString("employee_first_name");
        second_name = getIntent().getExtras().getString("employee_second_name");
        third_name = getIntent().getExtras().getString("employee_third_name");
        address = getIntent().getExtras().getString("employee_address");
        birth_date = getIntent().getExtras().getString("employee_date_of_birth");
        passport_number = getIntent().getExtras().getString("employee_passport_serial");
        passport_org = getIntent().getExtras().getString("employee_passport_company");
        role = getIntent().getExtras().getString("employee_role");
        phone_number = getIntent().getExtras().getString("employee_tel_number");
        password = getIntent().getExtras().getString("employee_password");
        photo = getIntent().getExtras().getString("employee_photo");

        iv_photo = findViewById(R.id.imageView);
        tv_id = findViewById(R.id.employeeId);
        tv_phone_number = findViewById(R.id.phoneNumber);
        tv_first_name = findViewById(R.id.first_name);
        tv_second_name = findViewById(R.id.second_name);
        tv_third_name = findViewById(R.id.third_name);
        tv_address = findViewById(R.id.address);
        tv_birth_date = findViewById(R.id.date_of_birth);
        tv_passport_number = findViewById(R.id.passport_serial_number);
        tv_passport_org = findViewById(R.id.passport_company);
        tv_role = findViewById(R.id.role);
    }

    private void setValues() {
        Bitmap bmp = base64ToBitmap(photo);
        iv_photo.setImageBitmap(bmp);
        tv_id.setText(id);
        tv_phone_number.setText(phone_number);
        tv_first_name.setText(first_name);
        tv_second_name.setText(second_name);
        tv_third_name.setText(third_name);
        tv_address.setText(address);
        tv_birth_date.setText(birth_date);
        tv_passport_number.setText(passport_number);
        tv_passport_org.setText(passport_org);

        if(role.equals("1")) {
            tv_role.setText("Инструктор");
        }else if(role.equals("2")){
            tv_role.setText("Координатор");
        }else {
            tv_role.setText("Регистратор");
        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
