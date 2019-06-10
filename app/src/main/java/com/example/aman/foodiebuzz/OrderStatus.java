package com.example.aman.foodiebuzz;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.aman.foodiebuzz.Common.Common;
import com.example.aman.foodiebuzz.Interface.ItemClickListener;
import com.example.aman.foodiebuzz.Model.Request;
import com.example.aman.foodiebuzz.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class OrderStatus extends AppCompatActivity {
    public RecyclerView recyclerView;
    public  RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    MaterialSpinner spinner;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();
    }
    private void loadOrders() {
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests
        )
        {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {
                try{
                    viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                    viewHolder.txtOrderPhone.setText(model.getPhone());

                    viewHolder.txtOrderAddress.setText(model.getAddress());
                    viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {

                        }
                    });

                }catch (Exception e){}}
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

       @Override
    public boolean onContextItemSelected(MenuItem item) {
           if(item.getTitle().equals(Common.UPDATE)){
               showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
           }
           else  if(item.getTitle().equals(Common.DELETE)){
               deleteOrder(adapter.getRef(item.getOrder()).getKey());
           }
           return super.onContextItemSelected(item);
       }

    private void showUpdateDialog(String key, final Request item) {
      final   AlertDialog.Builder alertDialog=new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please Choose Status");

        LayoutInflater inflater=this.getLayoutInflater();
    final    View view=inflater.inflate(R.layout.update_order_layout,null);
        spinner=view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","Approved","Delivered");
       alertDialog.setCancelable(false);
       alertDialog.setView(view);
       final String localKey=key;
       alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
               item.setStatus(String.valueOf(spinner.getSelectedIndex()));

               requests.child(localKey).setValue(item);
           }
       });
       alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
           }
       });
       alertDialog.show();

    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
    }
    private String convertCodeToStatus(String status){
        if(status.equals("0")){
            return "Placed";
        }
        else if(status.equals("1")){
            return "On My Way";
        }
        else{
            return "Shipped";
        }
    }
}
