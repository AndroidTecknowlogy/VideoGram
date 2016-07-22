package com.androidtecknowlogy.videogram;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtecknowlogy.videogram.fragment.SignInFragment;
import com.androidtecknowlogy.videogram.helper.BezelImageView;
import com.androidtecknowlogy.videogram.model.VideoObject;
import com.androidtecknowlogy.videogram.util.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView
        .OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    public static ArrayList<VideoObject> videoUris;
    public static ArrayList<Bitmap> videoThumbnails;
    public static FirebaseStorage mFirebaseStorage;

    public static GoogleApiClient mGoogleApiClient;
    public static GoogleSignInOptions gso;
    public static BezelImageView imageView;
    public static TextView txt_username;
    public static TextView txt_userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_home);

        mFirebaseStorage=FirebaseStorage.getInstance();

        //imageView=(ImageView)findViewById(R.id.user_image);
        /*txt_username=(TextView)findViewById(R.id.username);
        txt_userEmail=(TextView)findViewById(R.id.user_email);*/

        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                //.enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageView=(BezelImageView) navigationView.getHeaderView(0).findViewById(R.id.user_image);
        txt_userEmail=(TextView)navigationView.getHeaderView(0).findViewById(R.id.user_email);
        txt_username=(TextView)navigationView.getHeaderView(0).findViewById(R.id.username);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame,new SignInFragment())
                .commit();
        videoUris=new ArrayList<>();
        videoThumbnails=new ArrayList<>();

        mGoogleApiClient.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==R.id.nav_profile)
        {
            Toast.makeText(this,"yet to implement profile",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.nav_settings){
            Toast.makeText(this,"yet to implement settings",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.nav_log_out && mGoogleApiClient.isConnected())
        {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess())
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                                new SignInFragment()).commit();
                }
            });
        }

        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        String photoUrl= PreferenceManager.getDefaultSharedPreferences(this).getString(Constants
                .PHOTO_URL,null);
        String name=PreferenceManager.getDefaultSharedPreferences(this).getString(Constants
                .USERNAME,"anonymous");
        String email=PreferenceManager.getDefaultSharedPreferences(this).getString(Constants
                .USER_EMAIL,"none");



        if (photoUrl!=null && photoUrl.equalsIgnoreCase(Constants.EMPTY)){

            Picasso.with(this).load(photoUrl).into(imageView);
            txt_userEmail.setText(email);
            txt_username.setText(name);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.e("Con"," "+mGoogleApiClient.isConnected());

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Con"," "+mGoogleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
