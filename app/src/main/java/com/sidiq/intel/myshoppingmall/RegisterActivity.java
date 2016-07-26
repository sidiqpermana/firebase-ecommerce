package com.sidiq.intel.myshoppingmall;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener, OnCompleteListener<AuthResult> {

    @BindView(R.id.edt_username)
    EditText edtUsername;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.cb_toc)
    CheckBox cbToc;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private ProgressDialog progressDialog;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(this);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //home merujuk pada back button yang ada di action bar
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register){
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
                    || TextUtils.isEmpty(email)){
                Toast.makeText(RegisterActivity.this, "All fields are required",
                        Toast.LENGTH_SHORT).show();
            }else{
                if (cbToc.isChecked()){
                    progressDialog.show();
                    mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this);
                }else{
                    Toast.makeText(RegisterActivity.this,
                            "You have to check the ToC", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        progressDialog.cancel();
        if (task.isSuccessful()){
            Toast.makeText(this, "Register Success. Please Login", Toast.LENGTH_LONG).show();
            LoginActivity.start(this);
            finish();
        }else{
            Toast.makeText(this, "Register Failed. Please try again", Toast.LENGTH_LONG).show();
            Log.d("Firebase", task.getException().getMessage());
        }
    }
}
