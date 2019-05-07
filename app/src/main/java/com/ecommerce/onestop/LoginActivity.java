// this activity will allow the user to login
package com.ecommerce.onestop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.onestop.Model.Users;
import com.ecommerce.onestop.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private EditText InputNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;

    private String parentDBName = "Users";

    private CheckBox chkBoxRememberMe;

    private TextView AdminLink, NotAdminLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton = findViewById(R.id.login_btn);
        InputNumber = findViewById(R.id.login_phone_number_input);
        InputPassword = findViewById(R.id.login_password_input);
        loadingBar = new ProgressDialog(this);
        chkBoxRememberMe = findViewById(R.id.remember_me_chkb);
        AdminLink = findViewById(R.id.admin_panel_link);
        NotAdminLink = findViewById(R.id.not_admin_panel_link);

        // we are using paper library which will allow us to store information
        // Paper is a fast NoSQL-like storage for Java/Kotlin objects on Android with automatic schema migration support.
        // initialising paper
        Paper.init(this);

        // login button function
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this method will allow the user to login
                loginUser();
            }
        });

        // functionality of I am Admin link
        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                // parentDBName is the link of DB in which we will store the data
                parentDBName = "Admins";
            }
        });

        // if a user is not an admin and click on admin then he should come back to normal user
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                // parentDBName is the link of DB in which we will store the data
                parentDBName = "Users";
            }
        });
    }

    // this method will allow the user to login
    private void loginUser() {
        String phone = InputNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // checking for empty
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter you Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter you Password", Toast.LENGTH_SHORT).show();
        }

        // when everything is filled let the user login
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // allowing the access
            allowAccessToAccount(phone, password);
        }
    }

    // this function will be responsible for verifying th account details
    private void allowAccessToAccount(final String phone, final String password) {

        // here we pass the information to the prevalent class and store our information
        if (chkBoxRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }

        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if data exists we let the user login
                if (dataSnapshot.child(parentDBName).child(phone).exists()){
                    // send the value to Users class object name user
                    // note - we do this so that we can use the data later on
                    Users userData = dataSnapshot.child(parentDBName).child(phone).getValue(Users.class);
                    // retrieve the data from that class and check with the data entered
                    if (userData.getPhone().equals(phone)){
                        if (userData.getPassword().equals(password)){
                            // if the user is an admin he should go to the admin panel which is AdminActivity
                           if (parentDBName.equals("Admins")){
                               Toast.makeText(LoginActivity.this, "Welcome Admin, Logged in Successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               // send the user to adminCategory activity
                               Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                               startActivity(intent);
                           }
                           // if a normal user
                           else if(parentDBName.equals("Users")){
                               Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                               loadingBar.dismiss();

                               // send the user to home activity
                               Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                               // we send th user name to User class so that it can be used to display
                               Prevalent.currentOnlineUser = userData;
                               startActivity(intent);
                           }
                        }
                    }
                    // if the password is not correct
                    else{
                        loadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }

                }
                // we don't allow
                else{
                    Toast.makeText(LoginActivity.this, "Account with this " + phone+ " does not exists ", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "You need to create a new account", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
