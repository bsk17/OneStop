package com.ecommerce.onestop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecommerce.onestop.Model.Cart;
import com.ecommerce.onestop.Prevalent.Prevalent;
import com.ecommerce.onestop.ViewHolders.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button NextButton;
    private TextView txtTotalAmount;

    private int OverAllPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        NextButton = findViewById(R.id.next_btn);
        txtTotalAmount = findViewById(R.id.total_price);

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // displaying total price
                txtTotalAmount.setText("Your Total Amount = Rs "+ String.valueOf(OverAllPrice));

                // sending the total amount to next activity
                Intent intent = new Intent(CartActivity.this, ConfirmOrderActivity.class);
               // intent.putExtra("Total Price", String.valueOf(OverAllPrice));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Products"), Cart.class)
                .build();
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtProductQuantity.setText("Quantity = "+model.getQuantity());
                holder.txtProductPrice.setText("Price = "+model.getPrice());
                holder.txtProductName.setText(model.getPname());

                // calculating the price 1 by 1 then adding to total
                // price * quantity
                int oneProductTotalPrice = ((Integer.valueOf(model.getPrice()))) * Integer.valueOf(model.getQuantity());

                // adding for overall price
                OverAllPrice = OverAllPrice + oneProductTotalPrice;


                // to edit the cart items
                // when the user click on the cart item then 2 options will appear
                // and we will add function to those two options
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Delete"
                                };

                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options: ");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                              // i=0 means edit, i=1 means Delete
                              if (i == 0){
                                  Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                  // in productDetails activity only the selected item should open
                                  // using pid
                                  intent.putExtra("pid", model.getPid());
                                  startActivity(intent);
                              }
                              if (i == 1){
                                  // removing the item from cart list
                                  cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                          .child("Products").child(model.getPid())
                                          .removeValue()
                                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                                              @Override
                                              public void onComplete(@NonNull Task<Void> task) {
                                                  if (task.isSuccessful()){
                                                      Toast.makeText(CartActivity.this,
                                                              "Item Removed From Cart...",
                                                              Toast.LENGTH_SHORT).show();
                                                      Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                      startActivity(intent);
                                                  }
                                              }
                                          });
                              }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cart_items_layout, viewGroup, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
