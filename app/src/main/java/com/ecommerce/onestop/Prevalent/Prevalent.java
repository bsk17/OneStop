// this class will be used later for forgot password

// we will be using paper library from github to save the user information fo remember me
package com.ecommerce.onestop.Prevalent;

import com.ecommerce.onestop.Model.Users;

public class Prevalent {

    // we declare public because these variables are used by other class
    public static Users currentOnlineUser;
    public static final String UserPhoneKey = "UserPhone";
    public static final String UserPasswordKey = "UserPassword";
}
