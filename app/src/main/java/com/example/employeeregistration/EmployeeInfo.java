package com.example.employeeregistration;

public class EmployeeInfo {
    private int id;
    private String tel_number;
    private String name;
    private String second_name;
    private String role;
    private String photo;

    public EmployeeInfo(int id, String tel_number, String name, String second_name, String role, String photo){
        this.id = id;
        this.tel_number = tel_number;
        this.name = name;
        this.second_name = second_name;
        this.role = role;
        this.photo = photo;
    }
    public String getId() {
        String idString = String.valueOf(id);
        return idString;
    }

    public String getTel_number() {
        return tel_number;
    }

    public String getName() {
        return name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getRole() {
        return role;
    }

    public String getPhoto() {
        return photo;
    }

}
