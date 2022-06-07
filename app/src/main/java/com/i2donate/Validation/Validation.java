package com.i2donate.Validation;

public class Validation {
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
    public static String mobilepattern = "[1-9][0-9]{9}";
    public static  String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
//            "((?=.*\\d)(?=.*[a-zA-Z])(?=.*[a-zA-Z0-9])(?=.*[@#$%]).{6,20})";
  //  public static String PASSWORD_PATTERN="(?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{6,50})$";
}