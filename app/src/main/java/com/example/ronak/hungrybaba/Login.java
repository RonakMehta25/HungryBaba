package com.example.ronak.hungrybaba;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    private GoogleSignInOptions gso;
    private CallbackManager callback;
    private FacebookCallback<LoginResult> fcallback=new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;
        @Override
        public void onSuccess(LoginResult loginResult) {


             AccessToken access=loginResult.getAccessToken();
             Profile profile=Profile.getCurrentProfile();

           // TextView name = (TextView) findViewById(R.id.displayName);
              if(profile!=null) {
                  Intent intent=new Intent("com.example.ronak.hungrybaba.MainActivity");
                  intent.putExtra("userId", profile.getId());
                  startActivity(intent);
                //  name.setText("Hello"+profile.getName());
              }
            else
              {

                  mProfileTracker = new ProfileTracker() {
                      @Override
                      protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                          //TextView name = (TextView) findViewById(R.id.displayName);
                          //name.setText("Hello"+profile2.getName());
                          Intent intent=new Intent("com.example.ronak.hungrybaba.MainActivity");
                          intent.putExtra("userId", profile2.getId());
                          startActivity(intent);
                          mProfileTracker.stopTracking();
                      }
                  };
                  mProfileTracker.startTracking();
              }
                //name.setVisibility(View.VISIBLE);
                //Toast.makeText(Login.this,"Hello"+profile.getName(),Toast.LENGTH_LONG);

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        isLoggedIn();
        callback=CallbackManager.Factory.create();
        LoginButton log=(LoginButton)findViewById(R.id.login_button);
        log.registerCallback(callback, fcallback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        /*Google signin*/
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener(this);
    }

    private void isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
       /* if(accessToken!=null)
        {
            Intent intent=new Intent("com.example.ronak.hungrybaba.MainActivity");
            intent.putExtra("userId", Profile.getCurrentProfile().getId());

            startActivity(intent);
        }*/
        if(accessToken!=null) {
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                Intent intent = new Intent("com.example.ronak.hungrybaba.MainActivity");
                intent.putExtra("userId", profile.getId());
                startActivity(intent);
                //  name.setText("Hello"+profile.getName());
            } else {

                ProfileTracker mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                        //TextView name = (TextView) findViewById(R.id.displayName);
                        //name.setText("Hello"+profile2.getName());
                        Intent intent = new Intent("com.example.ronak.hungrybaba.MainActivity");
                        intent.putExtra("userId", profile2.getId());
                        startActivity(intent);
                        //mProfileTracker.stopTracking();
                    }
                };
                //mProfileTracker.startTracking();
            }
        }
    }

    public void onViewCreated(View view,Bundle savedInstanceState)
    {


    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        callback.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }
    private void signIn() {
        //Creating an intent
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        //Starting intent for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //If signin
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //Calling a new function to handle signin
            handleSignInResult(result);
        }
    }*/
    private void handleSignInResult(GoogleSignInResult result) {
        //If the login succeed
        if (result.isSuccess()) {
            //Getting google account
            GoogleSignInAccount acct = result.getSignInAccount();
           // TextView name = (TextView) findViewById(R.id.displayName);
           // name.setText(acct.getDisplayName());
            Intent intent=new Intent("com.example.ronak.hungrybaba.MainActivity");
            //intent.putExtra("userId", Profile.getCurrentProfile().getId());
            startActivity(intent);
        } else {
            //If login fails
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            //Calling signin
            signIn();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
