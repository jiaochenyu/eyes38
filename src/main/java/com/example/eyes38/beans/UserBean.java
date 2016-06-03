package com.example.eyes38.beans;

/**
 * Created by weixiao on 2016/6/2.
 */
public class UserBean {
    private String customer_id;//用户id
    private String username;//用户账号
    private String firstname;//用户姓名
    private String email;//用户邮箱
    private String sex;//用户性别

    public UserBean() {
    }

    public UserBean( String customer_id, String username, String firstname, String email, String sex) {
        this.customer_id = customer_id;
        this.username = username;
        this.firstname = firstname;
        this.email = email;
        this.sex = sex;
    }


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
