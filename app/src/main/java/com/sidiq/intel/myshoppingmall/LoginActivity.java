package com.sidiq.intel.myshoppingmall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sidiq.intel.myshoppingmall.preference.AppPreference;
import com.sidiq.intel.myshoppingmall.util.FacebookLoginUtil;
import com.sidiq.intel.myshoppingmall.util.GooglePlusLoginUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, OnCompleteListener<AuthResult>,
        FacebookLoginUtil.OnFacebookLoginSuccessListener{

    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.btn_login_google)
    Button btnLoginGoogle;
    @BindView(R.id.btn_login_facebook)
    Button btnLoginFacebook;
    @BindView(R.id.facebookLogin)
    LoginButton facebookLogin;

    private AppPreference appPreference;
    private FirebaseAuth mFirebaseAuth;
    private GooglePlusLoginUtil googlePlusLoginUtil;
    private FacebookLoginUtil facebookLoginUtil;
    private ProgressDialog progressDialog;

    private AuthCredential mAuthCredential = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Login");

        appPreference = new AppPreference(LoginActivity.this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        googlePlusLoginUtil = new GooglePlusLoginUtil(this);
        googlePlusLoginUtil.connect();
        facebookLoginUtil = new FacebookLoginUtil(facebookLogin, this);
        facebookLoginUtil.setOnFacebookLoginSuccessListener(this);

        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnLoginFacebook.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Please wait...");
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        boolean isLogin = false;
        switch (view.getId()) {
            case R.id.tv_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                break;

            case R.id.btn_login:
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "All fields are required",
                            Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    mAuthCredential = EmailAuthProvider.getCredential(email, password);
                    mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this);
                }

                break;

            case R.id.btn_login_google:
                googlePlusLoginUtil.signIn(this);
                break;

            case R.id.btn_login_facebook:
                facebookLoginUtil.doLogin();
                break;
        }

        if (intent != null) {
            startActivity(intent);
            if (isLogin) {
                finish();
            }
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (googlePlusLoginUtil != null) {
            googlePlusLoginUtil.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googlePlusLoginUtil.RC_SIGN_IN) {
            GoogleSignInAccount account = googlePlusLoginUtil.getSignInResult(data);
            if (account != null) {
                progressDialog.show();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(this, "Couldn't connect you to Google Account", Toast.LENGTH_LONG).show();
            }
        }
        facebookLoginUtil.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        mAuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(mAuthCredential).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        progressDialog.cancel();
        if (task.isSuccessful()) {
            appPreference.setEmail(task.getResult().getUser().getEmail());
            appPreference.setUserId(task.getResult().getUser().getUid());
            HomeActivity.start(this);
            Toast.makeText(this, "Login Success", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Log.d("Firebase", task.getException().getMessage());
            Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFacebookLoginSuccess(LoginResult loginResult) {
        progressDialog.show();
        mAuthCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        mFirebaseAuth.signInWithCredential(mAuthCredential).addOnCompleteListener(this);
    }
}
