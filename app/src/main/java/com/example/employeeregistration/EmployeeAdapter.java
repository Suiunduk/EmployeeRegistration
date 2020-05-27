package com.example.employeeregistration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        EmployeeViewHolder viewHolder = new EmployeeViewHolder(view);
        viewHolder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,employeeInfoList.get(viewHolder.getAdapterPosition()).getId(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, EmployeeInfoActivity.class);
                intent.putExtra( "employee_id", employeeInfoList.get(viewHolder.getAdapterPosition()).getId());
                intent.putExtra( "employee_first_name", employeeInfoList.get(viewHolder.getAdapterPosition()).getName());
                intent.putExtra( "employee_second_name", employeeInfoList.get(viewHolder.getAdapterPosition()).getSecond_name());
                intent.putExtra( "employee_third_name", employeeInfoList.get(viewHolder.getAdapterPosition()).getThird_name());
                intent.putExtra( "employee_address", employeeInfoList.get(viewHolder.getAdapterPosition()).getAddress());
                intent.putExtra( "employee_date_of_birth", employeeInfoList.get(viewHolder.getAdapterPosition()).getBirth_date());
                intent.putExtra( "employee_passport_serial", employeeInfoList.get(viewHolder.getAdapterPosition()).getPassport_number());
                intent.putExtra( "employee_passport_company", employeeInfoList.get(viewHolder.getAdapterPosition()).getPassport_org());
                intent.putExtra( "employee_password", employeeInfoList.get(viewHolder.getAdapterPosition()).getPassword());
                intent.putExtra( "employee_role", employeeInfoList.get(viewHolder.getAdapterPosition()).getRole());
                intent.putExtra( "employee_tel_number", employeeInfoList.get(viewHolder.getAdapterPosition()).getTel_number());
                intent.putExtra( "employee_photo", employeeInfoList.get(viewHolder.getAdapterPosition()).getPhoto());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        return viewHolder;
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

        TextView employeeId, first_name, second_name, third_name, address, date_of_birth,
                passport_serial, passport_company, role, tel_number;
        ImageView imageView;
        LinearLayout view_container;

        public EmployeeViewHolder(View itemView) {
            super(itemView);

            view_container = itemView.findViewById(R.id.container_view);
            employeeId = itemView.findViewById(R.id.employeeId);
            first_name = itemView.findViewById(R.id.first_name);
            second_name = itemView.findViewById(R.id.second_name);
            third_name = itemView.findViewById(R.id.third_name);
            address = itemView.findViewById(R.id.address);
            date_of_birth = itemView.findViewById(R.id.date_of_birth);
            passport_serial = itemView.findViewById(R.id.passport_serial_number);
            passport_company = itemView.findViewById(R.id.passport_company);
            role = itemView.findViewById(R.id.role);
            tel_number = itemView.findViewById(R.id.phoneNumber);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}