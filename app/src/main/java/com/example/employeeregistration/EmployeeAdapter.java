package com.example.employeeregistration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {


    private Context context;
    private List<EmployeeInfo> employeeInfoList;

    public EmployeeAdapter(Context context, List<EmployeeInfo> employeeInfoList) {
        this.context = context;
        this.employeeInfoList = employeeInfoList;
    }


    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.employee_list, null);
        return new EmployeeViewHolder(view);
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    @Override
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {
        EmployeeInfo employee = employeeInfoList.get(position);

        //loading the image
        Bitmap bmp = base64ToBitmap(employee.getPhoto());
        holder.imageView.setImageBitmap(bmp);


        holder.employeeId.setText(employee.getId());
        holder.first_name.setText(employee.getName());
        holder.second_name.setText(employee.getSecond_name());
        holder.tel_number.setText(employee.getTel_number());
        if(String.valueOf(employee.getRole()).equals("1")) {
            holder.role.setText("Инструктор");
        }else if(String.valueOf(employee.getRole()).equals("2")){
            holder.role.setText("Координатор");
        }else {
            holder.role.setText("Регистратор");
        }

    }

    @Override
    public int getItemCount() {
        return employeeInfoList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {

        TextView employeeId, first_name, second_name, role, tel_number;
        ImageView imageView;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            employeeId = itemView.findViewById(R.id.employeeId);
            first_name = itemView.findViewById(R.id.first_name);
            second_name = itemView.findViewById(R.id.second_name);
            role = itemView.findViewById(R.id.role);
            tel_number = itemView.findViewById(R.id.phoneNumber);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}