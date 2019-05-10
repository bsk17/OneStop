package com.ecommerce.onestop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button ApplyChangesBtn, DeleteProductBtn;
    private EditText name, description, price;
    private ImageView imageView;


    // to receive product id sent from previous activity
    private String productId;

    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        ApplyChangesBtn = findViewById(R.id.apply_changes_button);
        DeleteProductBtn = findViewById(R.id.delete_product_btn);
        name = findViewById(R.id.product_name_maintain);
        description = findViewById(R.id.product_description_maintain);
        price = findViewById(R.id.product_price_maintain);
        imageView = findViewById(R.id.product_image_maintain);

        // this function is to initially display the items
        displaySpecificProductInfo();

        // to change the details of product
        ApplyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });

        // to delete the product from the list
        DeleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });

    }

    // function of delete button
    private void deleteProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(AdminMaintainProductsActivity.this, "Product deleted successfully...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // function of apply button
    private void applyChanges() {

        String pName = name.getText().toString();
        String pDescription = description.getText().toString();
        String pPrice = price.getText().toString();

        if (pName.equals("")){
            Toast.makeText(this, "Write Product Name...", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals("")){
            Toast.makeText(this, "Write Product Description...", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals("")){
            Toast.makeText(this, "Write Product Price...", Toast.LENGTH_SHORT).show();
        }
        else{
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("price",pPrice);
            productMap.put("nmae",pName);
            productMap.put("description",pDescription);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes applied successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }
    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String pName = dataSnapshot.child("nmae").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    name.setText(pName);
                    price.setText(pPrice);
                    description.setText(pDescription);
                    Picasso.get().load(pImage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
