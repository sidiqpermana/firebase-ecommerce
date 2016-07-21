package com.sidiq.intel.myshoppingmall;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashscreenActivity extends AppCompatActivity {

    private AppPreference appPreference;
    private DelayAsync mDelayAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();
        appPreference = new AppPreference(SplashscreenActivity.this);

        mDelayAsync = new DelayAsync();
        mDelayAsync.execute();
    }

    @Override
    protected void onDestroy() {
        mDelayAsync.cancel(true);
        super.onDestroy();
    }

    class DelayAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            }catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = null;
            if (appPreference.getUserId().equals("")){
                intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
            }else{
                intent = new Intent(SplashscreenActivity.this, HomeActivity.class);
            }

            startActivity(intent);
            finish();
        }
    }


}
