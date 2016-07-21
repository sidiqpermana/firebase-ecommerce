package com.sidiq.intel.myshoppingmall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.sidiq.intel.myshoppingmall.api.request.PostRegisterRequest;
import com.sidiq.intel.myshoppingmall.api.response.BaseResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity
        implements PostRegisterRequest.OnPostRegisterRequestListener,
        View.OnClickListener {

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
    private PostRegisterRequest postRegisterRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        btnRegister.setOnClickListener(this);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Register");
        progressDialog.setMessage("Please wait...");

        postRegisterRequest = new PostRegisterRequest();
        postRegisterRequest.setOnPostRegisterRequestListener(this);
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
                    RequestParams mRequestParams = new RequestParams();
                    mRequestParams.put("username", username);
                    mRequestParams.put("password", password);
                    mRequestParams.put("email", email);

                    postRegisterRequest.setPostRequestParams(mRequestParams);
                    progressDialog.show();
                    postRegisterRequest.callApi();
                }else{
                    Toast.makeText(RegisterActivity.this,
                            "You have to check the ToC", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onPostRegisterSuccess(BaseResponse response) {
        progressDialog.cancel();

        Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

        startActivity(intent);
        finish();
    }

    @Override
    public void onPostRegisterFailure(String errorMessage) {
        progressDialog.cancel();

        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        postRegisterRequest.cancelRequest();
        super.onDestroy();
    }
}
