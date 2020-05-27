package com.example.employeeregistration;

public class EmployeeInfo {
    private int id;
    private String tel_number;
    private String name;
    private String second_name;
    private String third_name;
    private String address;
    private String birth_date;
    private String passport_number;
    private String passport_org;
    private String password;
    private String role;
    private String photo;

    public EmployeeInfo(int id, String tel_number, String name, String second_name, String third_name, String address, String birth_date, String passport_number, String passport_org, String password,  String role, String photo){
        this.id = id;
        this.tel_number = tel_number;
        this.name = name;
        this.second_name = second_name;
        this.third_name = third_name;
        this.address = address;
        this.birth_date = birth_date;
        this.passport_number = passport_number;
        this.passport_org = passport_org;
        this.password = password;
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

    public String getThird_name() {
        return third_name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getPassport_number() {
        return passport_number;
    }

    public String getPassport_org() {
        return passport_org;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhoto() {
        return photo;
    }

}
