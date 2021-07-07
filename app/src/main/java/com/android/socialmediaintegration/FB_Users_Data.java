package com.android.socialmediaintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.android.socialmediaintegration.databinding.ActivityFBUsersDataBinding;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FB_Users_Data extends AppCompatActivity {
    ActivityFBUsersDataBinding Binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = ActivityFBUsersDataBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
        if(userr!= null){
            String name, email;
            Uri photo;
            name= userr.getDisplayName();
//            email=userr.getPhoneNumber();
            photo=userr.getPhotoUrl();

            Binding.userName.setText(name);
//            Binding.userEmail.setText(email);
            Glide.with(this).load(photo).into(Binding.profileImage);


            Binding.logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.signOut();
                    LoginManager.getInstance().logOut();
                    startActivity(new Intent(FB_Users_Data.this,MainActivity.class));
                }
            });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FB_Users_Data.this);
        builder.setTitle("Close App").setMessage("Are you really want to exit App?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent in = new Intent(Intent.ACTION_MAIN);
                        in.addCategory(Intent.CATEGORY_HOME);
                        startActivity(in);
                        finish();
                        System.exit(0);
                    }
                })
                .setNegativeButton("No",null).setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();

    }
}