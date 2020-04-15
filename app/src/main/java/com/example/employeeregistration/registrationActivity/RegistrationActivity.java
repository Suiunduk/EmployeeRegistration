package com.example.employeeregistration.registrationActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.employeeregistration.ListActivity;
import com.example.employeeregistration.R;
import com.example.employeeregistration.MainActivity;
import com.example.employeeregistration.WebReq;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends MainActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;

    @BindView(R.id.registration_photo)
    ImageView imgProfile;

    EditText phoneNum;
    EditText firstName;
    EditText secondName;
    EditText thirdName;
    EditText address;
    EditText chooseDate;
    Spinner passportCode;
    EditText passportSerialNum;
    EditText passportCompany;
    EditText setPassword;
    EditText confirmPassword;
    Spinner role;
    Bitmap profilePhoto = null;

    final Calendar c = Calendar.getInstance();
    private int year;
    private int month;
    private int day;

    private BottomNavigationView bottomNavigationView;

    public RegistrationActivity(){

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeFields();
        init();
        ButterKnife.bind(this);

        onClickDatePicker();

        ImagePickerActivity.clearCache(this);
        bottomNavigationView.setSelectedItemId(R.id.go_to_registration_button);
        onClickListButton();
        onClickSignupButton();
    }
    //Initialization of fields
    private void initializeFields(){
        bottomNavigationView = findViewById(R.id.nav_view);
        phoneNum = findViewById(R.id.registration_phone_number);
        firstName = findViewById(R.id.registration_first_name);
        secondName = findViewById(R.id.registration_last_name);
        thirdName = findViewById(R.id.registration_fathers_name);
        address = findViewById(R.id.registration_address);
        passportSerialNum = findViewById(R.id.passport_serial_number);
        passportCompany = findViewById(R.id.passport_company);
        setPassword = findViewById(R.id.registration_set_password);
        confirmPassword = findViewById(R.id.registration_confirm_password);
        role = findViewById(R.id.registration_role);
        passportCode = findViewById(R.id.passport_code);
        chooseDate = findViewById(R.id.registration_date_of_birth);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        context = getApplicationContext();
    }

    /*
    Profile Photo Loading methods
    ------------------------------------------------------------------------------------------------
    */
    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);


        GlideApp.with(this).load(url)
                .into(imgProfile);

        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));

    }


    @OnClick({R.id.image_pick_button})
    void onImagePickClick() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(RegistrationActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 4);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 300);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 400);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(RegistrationActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 3); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 4);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    profilePhoto = bitmap;
                    // loading profile image from local cache
                    assert uri != null;
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                RegistrationActivity.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    /*
    ------------------------------------------------------------------------------------------------
    Profile photo loading methods
     */



    /*
    Go to list button
    ----------------------------------------------------------------------------------------
     */
    public void onClickListButton(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.go_to_list_button:
                        Intent intent = new Intent(RegistrationActivity.this, ListActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    /*
    Date picker methods
    ----------------------------------------------------------------------------------------
     */
    private void onClickDatePicker(){
        findViewById(R.id.date_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                RegistrationActivity.this,
                R.style.datePickerStyle,
                //R.style.DatePickHeaderColor,
                //R.style.DatePickWidth,
                //R.style.DatePickHeight,
                new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        chooseDate.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    /*
    Date picker methods
    ----------------------------------------------------------------------------------------
     */

    public void init() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        sharedPrefEditor = sharedPreferences.edit();
    }

    private void onClickSignupButton(){
        findViewById(R.id.add_employee_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupValidation();
            }
        });
    }
    private String getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }/*
    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap!=null) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }*/

    private void signupValidation(){
        String phoneNumString = phoneNum.getText().toString();
        String firstNameString = firstName.getText().toString();
        String secondNameString = secondName.getText().toString();
        String thirdNameString = thirdName.getText().toString();
        String addressString = address.getText().toString();
        String birthDateString = chooseDate.getText().toString();
        String passportSerialNumString =passportCode.getSelectedItem().toString() + passportSerialNum.getText().toString();
        String passportCompanyString = passportCompany.getText().toString();
        String setPasswordString = setPassword.getText().toString();
        String confirmPasswordString = confirmPassword.getText().toString();
        String roleString = role.getSelectedItem().toString();
        String profilePhotoByte;

        if(phoneNumString.length() != 13){
            Toast.makeText(getApplicationContext(),"Телефонный номер введён неправильно",Toast.LENGTH_SHORT).show();
            return;
        }
        if(firstNameString.length() == 0){
            Toast.makeText(getApplicationContext(),"Введите имя сотрудника",Toast.LENGTH_SHORT).show();
            return;
        }
        if(secondNameString.length() == 0){
            Toast.makeText(getApplicationContext(),"Введите фамилию сотрудника",Toast.LENGTH_SHORT).show();
            return;
        }
        if(addressString.length() == 0){
            Toast.makeText(getApplicationContext(),"Адрес проживания обязателен",Toast.LENGTH_SHORT).show();
            return;
        }
        if(birthDateString.length() == 0){
            Toast.makeText(getApplicationContext(),"Дата рождения обязателен",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passportSerialNumString.length() != 9){
            Toast.makeText(getApplicationContext(),"Серийный номер паспорта неправильный",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passportCompanyString.length() == 0){
            Toast.makeText(getApplicationContext(),"Введите орган выдавший паспорт",Toast.LENGTH_SHORT).show();
            return;
        }
        if(setPasswordString.length() < 5){
            Toast.makeText(getApplicationContext(),"Пароль должен содержать больше 5 символов",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!confirmPasswordString.equals(setPasswordString)){
            Toast.makeText(getApplicationContext(),"Пароли не совпадают",Toast.LENGTH_SHORT).show();
            return;
        }
        if(profilePhoto == null){
            Toast.makeText(getApplicationContext(),"Выберите фото",Toast.LENGTH_SHORT).show();
            return;
        }
        if(roleString.equals("Инструктор")){
            roleString ="1";
        }
        if(roleString.equals("Координатор")){
            roleString = "2";
        }
        if(roleString.equals("Регистратор")){
            roleString = "3";
        }

        profilePhotoByte = getBytesFromBitmap(profilePhoto);
        //Toast.makeText(getApplicationContext(),"ВСЁ ПРАВИЛЬНО!",Toast.LENGTH_SHORT).show();

        //all inputs are validated now perform login request
        RequestParams params = new RequestParams();
        params.add("type","signup");
        params.add("tel_number",phoneNumString);
        params.add("name",firstNameString);
        params.add("second_name",secondNameString);
        params.add("third_name",thirdNameString);
        params.add("address",addressString);
        params.add("birth_date",birthDateString);
        params.add("passport_number",passportSerialNumString);
        params.add("passport_org",passportCompanyString);
        params.add("password",setPasswordString);
        params.add("role",roleString);
        params.add("photo", profilePhotoByte);

        WebReq.post(getApplicationContext(), "api.php", params, new RegistrationActivity.ResponseHandler());


    }

    private class ResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            super.onSuccess(statusCode, headers, response);
            Log.d("response ",response.toString()+" ");
            try {
                if (response.getBoolean("error")){
                    // failed to login
                    Toast.makeText(getApplicationContext(),response.getString("message"),Toast.LENGTH_SHORT).show();
                }else{
                    // successfully logged in
                    JSONObject user = response.getJSONObject("user");
                    //save login values
                    sharedPrefEditor.putBoolean("login",true);
                    sharedPrefEditor.putString("id",user.getString("id"));
                    sharedPrefEditor.putString("name",user.getString("name"));
                    sharedPrefEditor.putString("tel_number",user.getString("tel_number"));
                    sharedPrefEditor.apply();
                    sharedPrefEditor.commit();

                    Toast.makeText(getApplicationContext(),"Сотрудник успешно добавлен!",Toast.LENGTH_SHORT).show();

                    //Move to home activity
                    /*
                    intent = new Intent(context,HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                     */
                }
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
