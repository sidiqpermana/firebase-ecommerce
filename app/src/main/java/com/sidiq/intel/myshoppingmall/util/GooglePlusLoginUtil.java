package com.sidiq.intel.myshoppingmall.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.sidiq.intel.myshoppingmall.R;

public class GooglePlusLoginUtil implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final int RC_SIGN_IN = 110;
    private Context context;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    public GooglePlusLoginUtil(Context context) {
        this.context = context;

        String googleServerId = context.getString(R.string.default_web_client_id);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(googleServerId)
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void signIn(Fragment fragment) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public GoogleSignInAccount getSignInResult(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            if (account != null) {
                Log.d("MyStoreOnline", String.valueOf(account.getDisplayName())
                        + ", " + String.valueOf(account.getEmail())
                        + "," + String.valueOf(account.getId())
                        + ", " + String.valueOf(account.getPhotoUrl())
                        + ", " + String.valueOf(account.getIdToken()));
            }
            return account;
        } else {
            Log.d("MyStoreOnline",String.valueOf(result.getStatus().getStatusMessage()));
            return null;
        }
    }


    public void signOut(ResultCallback<Status> callback) {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(callback);
        }
    }

    public void revokeAccess(ResultCallback<Status> callback) {
        if (googleApiClient.isConnected()) {
            Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(callback);
        }

    }
}