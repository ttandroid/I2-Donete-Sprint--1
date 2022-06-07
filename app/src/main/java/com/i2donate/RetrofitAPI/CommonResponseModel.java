package com.i2donate.RetrofitAPI;

public class CommonResponseModel {

    public int status;
    public String message;
    public Data data;

    public class Data{
        public String user_id;
        public String name;
        public String email;
        public String type;
        public String business_name;
        public String phone_number;
        public String gender;
        public String country;
        public String photo;
        public String terms;
        public String token;
    }
}
