package com.example.aman.foodiebuzz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.aman.foodiebuzz.Common.Common;
import com.example.aman.foodiebuzz.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignInActivity extends AppCompatActivity {
MaterialEditText edtPhone,edtPassword;
Button btnSignIn;
    FirebaseDatabase database;
    DatabaseReference users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword=findViewById(R.id.edtPassword);
        edtPhone=findViewById(R.id.edtPhone);
        btnSignIn=findViewById(R.id.btnSignIn);

        //init Firbase
         database=FirebaseDatabase.getInstance();
         users=database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(edtPhone.getText().toString(),edtPassword.getText().toString());

            }
        });
    }

    private void signInUser(String phone, String password) {
        final ProgressDialog mDialog=new ProgressDialog(SignInActivity.this);
        mDialog.setMessage("Please Wait");
        mDialog.show();
        final String localPhone=phone;
        final String localPassword=password;
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(localPhone).exists()){
                    mDialog.dismiss();
                    User user=dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);

                    if(Boolean.parseBoolean(user.getIsStaff())){
                        if(user.getPassword().equals(localPassword)){

                            Intent signIn=new Intent(SignInActivity.this,Home.class);
                            Common.currentUser=user;
                            startActivity(signIn);
                            finish();
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "WrongPassword", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(SignInActivity.this, "Login With Admin", Toast.LENGTH_SHORT).show();

                    }
                }else {
                    mDialog.dismiss();
                    Toast.makeText(SignInActivity.this, "UserNotExists", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
