package com.android.socialmediaintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.socialmediaintegration.databinding.ActivityUserDataBinding;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class user_data extends AppCompatActivity {
    ActivityUserDataBinding binding;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient ;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(user_data.this);
        builder.setTitle("Are you want to exit from the app")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        if(account != null){
            String name = account.getDisplayName();
            String email = account.getEmail();
            Uri photo = account.getPhotoUrl();

            binding.userName.setText(name);
            binding.userEmail.setText(email);
            Glide.with(this).load(photo).into(binding.profileImage);


        }
        else {
            Intent intent = getIntent();
            String Fname = intent.getStringExtra("fn");
            String Lname = intent.getStringExtra("ln");
            String email = intent.getStringExtra("email");
            String img = intent.getStringExtra("img");

            binding.userName.setText(Fname + " "+ Lname);
            binding.userEmail.setText(email);
            Glide.with(this).load(img).into(binding.profileImage);
        }
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                mGoogleSignInClient = GoogleSignIn.getClient(user_data.this,gso);
                mGoogleSignInClient.signOut();
                startActivity(new Intent(user_data.this,MainActivity.class));
            }
        });
    }
}