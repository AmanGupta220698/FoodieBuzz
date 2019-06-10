package com.example.aman.foodiebuzz.Common;


import com.example.aman.foodiebuzz.Model.User;

/**
 * Created by Aman on 4/25/2019.
 */

public class Common {
    public static User currentUser;

    public static final String UPDATE="Update";
    public static final String DELETE="Delete";
    public static final String  convertCodeToStatus(String status){
        if(status.equals("0")){
            return "Placed";
        }
        else if(status.equals("1")){
            return "Order Approved";
        }
        else{
            return "Delivered";
        }
    }
}
