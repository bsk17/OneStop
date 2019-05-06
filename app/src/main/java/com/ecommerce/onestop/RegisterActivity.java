// this activity will allow the user to register
package com.ecommerce.onestop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = findViewById(R.id.register_btn);
        InputName = findViewById(R.id.register_username_input);
        InputPhoneNumber = findViewById(R.id.register_phone_number_input);
        InputPassword = findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);


        // connecting to FireBase when  CreateAccountButton is pressed
        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this function will be used to link to FireBase
                createAccount();
            }
        });
    }

    // this function will be used to link to FireBase
    private void createAccount() {
        // we get the user details
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        // checking for empty
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter you name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Enter you Phone Number", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter you Password", Toast.LENGTH_SHORT).show();
        }

        // when everything is filled
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            // before accepting credentials we check for the phone number whether unique ot not
            validatePhoneNumber(name, phone, password);
        }
    }

    private void validatePhoneNumber(final String name, final String phone, final String password) {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if this phone number does not exist in the DB
                if (!(dataSnapshot.child("Users").child(phone).exists())){
                    // here we actually create the account
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    // inserting the data into DB
                    userdataMap.put("phone", phone);
                    userdataMap.put("name", name);
                    userdataMap.put("password", password);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // if our task is successful
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your account is created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        // after successful registration the user can now login
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                    }
                                    // if any error occurs
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "OOPS!! Some error occurred. Please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                // if the phone number does not exists
                else {
                    Toast.makeText(RegisterActivity.this, "This " +phone+" Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Try again using different number", Toast.LENGTH_SHORT).show();

                    // send the user back to main activity
                    Intent intent = new Intent(RegisterActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
