package com.sidiq.intel.myshoppingmall;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sidiq.intel.myshoppingmall.adapter.ProductAdapter;
import com.sidiq.intel.myshoppingmall.db.CartHelper;
import com.sidiq.intel.myshoppingmall.db.CartItem;
import com.sidiq.intel.myshoppingmall.model.Item;
import com.sidiq.intel.myshoppingmall.preference.AppPreference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener,
        View.OnClickListener,
        FirebaseAuth.AuthStateListener{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_cart)
    ImageView imgCart;
    @BindView(R.id.tv_cart)
    TextView tvCart;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.lv_item)
    ListView lvItem;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private ProductAdapter adapter;
    private ArrayList<Item> listItem;
    private CartHelper mCartHelper;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private AppPreference appPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        imgCart.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCartHelper = new CartHelper(HomeActivity.this);
        appPreference = new AppPreference(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("product");

        adapter = new ProductAdapter(HomeActivity.this);
        listItem = new ArrayList<>();
        adapter.setListItem(listItem);

        lvItem.setOnItemClickListener(this);
        lvItem.setAdapter(adapter);

        lvItem.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        getProductData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView tvUsername = (TextView)navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        String username = TextUtils.isEmpty(appPreference.getUsername()) ? appPreference.getEmail() : appPreference.getUsername();
        tvUsername.setText(username);
    }

    private void getProductData() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                lvItem.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                if (listItem.size() > 0){
                    listItem.clear();
                }

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()){
                    Item product = eventSnapshot.getValue(Item.class);
                    listItem.add(product);
                }

                adapter.setListItem(listItem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("MyStoreOnline", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabaseReference.addValueEventListener(postListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(this);
    }

    private void updateCartQty() {
        ArrayList<CartItem> list = mCartHelper.getAll();
        tvCart.setVisibility(View.GONE);
        if (list != null) {
            if (list.size() > 0) {
                int cartQty = list.size();
                tvCart.setVisibility(View.VISIBLE);
                tvCart.setText(String.valueOf(cartQty));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartQty();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent mIntent = null;
        if (id == R.id.nav_category) {

        } else if (id == R.id.nav_history) {
            OrderHistoryActivity.start(this);
        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            showLogoutAlertDialog();
        }

        if (mIntent != null) {
            startActivity(mIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showLogoutAlertDialog() {
        AlertDialog mAlertDialog = new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin untuk logout?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppPreference appPreference = new AppPreference(HomeActivity.this);
                        appPreference.clear();

                        mFirebaseAuth.signOut();

                        LoginActivity.start(HomeActivity.this);
                        finish();
                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create();
        mAlertDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent mIntent = new Intent(HomeActivity.this, DetailProductActivity.class);
        mIntent.putExtra("product", listItem.get(i));
        startActivity(mIntent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_cart) {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            appPreference.setUserId(user.getUid());
        }
    }
}
