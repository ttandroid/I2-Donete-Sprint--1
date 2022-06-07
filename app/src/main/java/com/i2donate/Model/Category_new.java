package com.i2donate.Model;

import java.util.List;

public class Category_new {

    String category_id;
    String category_code;
    String category_name;
    List<subcategorynew>Subcategory;

    public List<subcategorynew> getSubcategory() {
        return Subcategory;
    }

    public void setSubcategory(List<subcategorynew> sub_category) {
        this.Subcategory = sub_category;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
