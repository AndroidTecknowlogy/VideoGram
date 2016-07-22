package com.androidtecknowlogy.videogram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidtecknowlogy.videogram.MainActivity;
import com.androidtecknowlogy.videogram.R;
import com.androidtecknowlogy.videogram.util.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

/**
 * Created by nezspencer on 7/13/16.
 */
public class SignInFragment extends Fragment {


    private GoogleApiClient mGoogleApiClient;
    private static final int SIGN_IN_GOOGLE=3322;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_signin,container,false);
        SignInButton signInButton=(SignInButton)view.findViewById(R.id.google_sign_in);

        /*GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient=new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();*/




        signInButton.setScopes(MainActivity.gso.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(MainActivity.mGoogleApiClient);
                startActivityForResult(signInIntent,SIGN_IN_GOOGLE);
            }
        });

        changeSignInText(signInButton);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==SIGN_IN_GOOGLE && resultCode==getActivity().RESULT_OK)
        {
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            parseSignInResult(result);

        }
    }

    protected void changeSignInText(SignInButton signInButton) {

        for(int count = 0; count < signInButton.getChildCount(); count ++) {
            View signInView = signInButton.getChildAt(count);

            if(signInView instanceof TextView) {
                TextView text = (TextView) signInView;
                text.setText("Sign in to VideoGram");
                text.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                        "fonts/Acme-Regular.ttf"));
                return;
            }
        }
    }
    /**extracts all needed details stored by google after successful sign in
     * @param googleSignInResult GoogleSignInResult object containing all required details*/
    public void parseSignInResult(GoogleSignInResult googleSignInResult)
    {
        if (googleSignInResult!=null && googleSignInResult.isSuccess()
                && googleSignInResult.getSignInAccount()!=null)
        {
            String username=googleSignInResult.getSignInAccount().getDisplayName();
            String email=googleSignInResult.getSignInAccount().getEmail();
            String imageUrl=googleSignInResult.getSignInAccount().getPhotoUrl()==null? Constants.EMPTY
                    : googleSignInResult.getSignInAccount().getPhotoUrl().toString();

            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                    .putString(Constants.USERNAME,username)
                    .putString(Constants.USER_EMAIL,email)
                    .putString(Constants.PHOTO_URL,imageUrl)
                    .apply();

            Picasso.with(getActivity()).load(imageUrl).into(MainActivity.imageView);
            MainActivity.txt_username.setText(username);
            MainActivity.txt_userEmail.setText(email);

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,
                    new MainActivityFragment()).commit();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getActivity()!=null && ((AppCompatActivity)getActivity()).getSupportActionBar()!=null)
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}
