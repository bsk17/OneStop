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

import com.ecommerce.onestop.Model.AdminOrders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.nio.charset.Charset;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList = findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(ordersRef, AdminOrders.class)
                .build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrdersViewHolder holder, final int position, @NonNull final AdminOrders model) {
                        holder.orderUserName.setText("Name: "+ model.getName());
                        holder.orderUserPhoneNumber.setText("Phone: "+ model.getPhone());
                        holder.orderUserAddressCity.setText("Address: "+ model.getAddress()+","+model.getCity());
                        holder.orderUsserTotalPrice.setText("Total Amount: Rs "+ model.getTotalAmount());
                        holder.orderDateTime.setText("Date & Time: "+ model.getDate() + "  "+ model.getTime());

                        // passing the phone number to retrieve data in next activity
                        holder.ShowOrderProductsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // to get the id
                                String userId = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("uid", userId);
                                startActivity(intent);
                            }
                        });

                        // to remove orders when shipped by pressing the listView item
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // displaying dialogue box
                                CharSequence options[] = new CharSequence[]
                                        {
                                                "YES",
                                                "NO"
                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Product Shipped ?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // i=0 means yes i=1 means no
                                        if (i == 0){
                                            // remove the product from list
                                            // to get the id
                                            String userId = getRef(position).getKey();
                                            removeOrder(userId);
                                        }
                                        else {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.orders_layout, viewGroup, false);
                        return new AdminOrdersViewHolder(view);
                    }
                };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    // this function will be called when the user press yes
    // it wil delete the item from the list
    private void removeOrder(String userId) {
        // we have already created reference
        ordersRef.child(userId).removeValue();
    }

    // creating the viewHolder for adapter
    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder{

        // the items of ordersLayout
        public TextView orderUserName, orderUserPhoneNumber, orderUserAddressCity, orderUsserTotalPrice, orderDateTime;
        public Button ShowOrderProductsBtn;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            orderUserName = itemView.findViewById(R.id.order_user_name);
            orderUserPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            orderUserAddressCity = itemView.findViewById(R.id.order_address_city);
            orderUsserTotalPrice = itemView.findViewById(R.id.order_total_price);
            orderDateTime = itemView.findViewById(R.id.order_date_time);
            ShowOrderProductsBtn = itemView.findViewById(R.id.show_order_products);
        }
    }
}
