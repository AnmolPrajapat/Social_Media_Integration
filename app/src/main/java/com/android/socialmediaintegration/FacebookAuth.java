package com.android.socialmediaintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.socialmediaintegration.model.Users;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.util.Arrays;

public class FacebookAuth extends MainActivity {
    CallbackManager callbackManager;
    GraphRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }
//    outside onCreate
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // Pass the activity result back to the Facebook SDK
    callbackManager.onActivityResult(requestCode, resultCode, data);
     }


     AccessTokenTracker tokenTracker = new AccessTokenTracker() {
         @Override
         protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
             loaduserProfile(currentAccessToken);
         }
     };
    private void loaduserProfile(AccessToken newAccessToken){
        request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    String img_url = "https://graph.facebook.com/"+id + "/picture?type=normal";

                    Intent i = new Intent(FacebookAuth.this,user_data.class);
                    i.putExtra("fn", first_name);
                    i.putExtra("ln", last_name);
                    i.putExtra("email", email);
                    i.putExtra("img",img_url);
                    startActivity(i);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields","first_name, last_name,email,id");
        request.setParameters(parameter);
        request.executeAsync();

    }

    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);

                            Users users = new Users();
                            users.setProfile_pic(user.getPhotoUrl().toString());
                            users.setU_name(user.getDisplayName());
                            users.setU_id(user.getUid());
                            users.setU_email(user.getEmail());
//                            users.setU_password(user.updatePassword());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);
//
//                            Intent i = new Intent(FacebookAuth.this,user_data.class);
//                            startActivity(i);
//                            Toast.makeText(FacebookAuth.this, "Logging into facebook account", Toast.LENGTH_SHORT).show();


                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(FacebookAuth.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

}