package com.android.socialmediaintegration.model;

public class Users {
    String profile_pic, U_name, U_id, U_email,U_password, phone;

    public  Users(){}

    public Users(String profile_pic, String u_name, String u_id, String u_email, String u_password, String phone) {
        this.profile_pic = profile_pic;
        U_name = u_name;
        U_id = u_id;
        U_email = u_email;
        U_password = u_password;
        this.phone = phone;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        U_name = u_name;
    }

    public String getU_id() {
        return U_id;
    }

    public void setU_id(String u_id) {
        U_id = u_id;
    }

    public String getU_email() {
        return U_email;
    }

    public void setU_email(String u_email) {
        U_email = u_email;
    }

    public String getU_password() {
        return U_password;
    }

    public void setU_password(String u_password) {
        U_password = u_password;
    }

    public String getphone() {
        return phone;
    }

    public void setPhone(String bday) {
        this.phone = phone;
    }
}
