package com.sidiq.intel.myshoppingmall;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sidiq.intel.myshoppingmall.db.CartHelper;
import com.sidiq.intel.myshoppingmall.db.CartItem;
import com.sidiq.intel.myshoppingmall.model.Item;
import com.sidiq.intel.myshoppingmall.util.SampleData;
import com.sidiq.intel.myshoppingmall.util.Util;

import java.util.ArrayList;

public class DetailProductActivity extends AppCompatActivity
    implements View.OnClickListener{
    private TextView tvName, tvPrice;
    private Button btnAddToCart;
    private ImageView imgDetail;
    private Spinner spnSize;
    private TextView tvDesc;
    private ImageView imgThumbA, imgThumbB, imgThumbC, imgThumbD;
    private TextView tvCart, tvTitle;
    private ImageView imgCart;
    private Toolbar toolbar;

    private int currentImagePosition = 0;
    private Item selectedProduct;

    private CartHelper mCartHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        tvName = (TextView)findViewById(R.id.tv_name);
        tvPrice = (TextView)findViewById(R.id.tv_price);
        btnAddToCart = (Button)findViewById(R.id.btn_add_to_cart);
        imgDetail = (ImageView)findViewById(R.id.img_detil);
        spnSize = (Spinner)findViewById(R.id.spn_size);
        tvDesc = (TextView)findViewById(R.id.tv_desc);
        tvTitle = (TextView)findViewById(R.id.tv_title);
        tvCart = (TextView)findViewById(R.id.tv_cart);
        imgCart = (ImageView)findViewById(R.id.img_cart);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        imgCart.setOnClickListener(this);
        imgDetail.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);

        imgThumbA = (ImageView)findViewById(R.id.img_thumb_a);
        imgThumbB = (ImageView)findViewById(R.id.img_thumb_b);
        imgThumbC = (ImageView)findViewById(R.id.img_thumb_c);
        imgThumbD = (ImageView)findViewById(R.id.img_thumb_d);

        imgThumbA.setOnClickListener(this);
        imgThumbB.setOnClickListener(this);
        imgThumbC.setOnClickListener(this);
        imgThumbD.setOnClickListener(this);

        setSupportActionBar(toolbar);
        tvTitle.setText("Detail Produk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedProduct = getIntent().getParcelableExtra("product");
        tvName.setText(selectedProduct.getName());
        tvPrice.setText(Util.getCurrency(selectedProduct.getPrice()));
        Glide.with(DetailProductActivity.this)
                .load(selectedProduct.getImage())
                .into(imgDetail);

        String[] size = new String[]{
                "Pilih Ukuran",
                "38",
                "39", "40", "41", "42", "43", "44", "45"
        };

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(DetailProductActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                android.R.id.text1, size);
        spnSize.setAdapter(sizeAdapter);

        String desc = "As an engineer, the founders of Brodo, Yukka and Uta always try to think about how to make something better and easier, also about how to make the best solution from the complex problem.\n" +
                "\n" +
                "Brodo has been started when Yukka had problem to find the suitable size shoes for himself, which was 46. But then as the time going, Yukka and Uta found the shocking facts about fashion industry, especially in Indonesia. It's like about how the big country with abound of resources and world class producer doesn't have a 'made in Indonesia by Indonesian for Indonesian' label. Also it's about how the price in retail world is totally overpriced by 3 times, even 8 times.\n" +
                "\n" +
                "When you buy Brodo, you will not only buy a product but also a dream, idea, our vision, the Indonesian youth.\n" +
                "\n" +
                "Well, no doubt that Brodo will have so much chance to do mistake in the future. With no experience, too young, doesn't have big capital, too naive, and dream too high, but those things are the things that make us win.\n" +
                "\n" +
                "We choose to see the future with optimism because that is our mandate, the youth of Indonesia.";
        tvDesc.setText(desc);

        Glide.with(DetailProductActivity.this).load(SampleData.thumb[0]).into(imgThumbA);
        Glide.with(DetailProductActivity.this).load(SampleData.thumb[1]).into(imgThumbB);
        Glide.with(DetailProductActivity.this).load(SampleData.thumb[2]).into(imgThumbC);
        Glide.with(DetailProductActivity.this).load(SampleData.thumb[3]).into(imgThumbD);

        mCartHelper = new CartHelper(DetailProductActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String imageUrl = null;
        switch (view.getId()){
            case R.id.img_thumb_a:
                imageUrl = SampleData.realImages[0];
                currentImagePosition = 0;
                break;

            case R.id.img_thumb_b:
                imageUrl = SampleData.realImages[1];
                currentImagePosition = 1;
                break;

            case R.id.img_thumb_c:
                imageUrl = SampleData.realImages[2];
                currentImagePosition = 2;
                break;

            case R.id.img_thumb_d:
                imageUrl = SampleData.realImages[3];
                currentImagePosition = 3;
                break;

            case R.id.img_detil:
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < SampleData.realImages.length; i++){
                    list.add(SampleData.realImages[i]);
                }
                Intent mIntent = new Intent(DetailProductActivity.this, DetailImageActivity.class);
                mIntent.putExtra("url_images", list);
                mIntent.putExtra("position", currentImagePosition);
                startActivity(mIntent);
                break;

            case R.id.btn_add_to_cart:
                if (mCartHelper.isItemAlreadyExist(Integer.parseInt(selectedProduct.getProduct_id()))){
                    Toast.makeText(DetailProductActivity.this,
                            "This product is already in cart", Toast.LENGTH_SHORT).show();
                }else{
                    mCartHelper.create(Integer.parseInt(selectedProduct.getProduct_id()),
                            selectedProduct.getName(),
                            selectedProduct.getImage(),
                            1, selectedProduct.getPrice());
                    Toast.makeText(DetailProductActivity.this,
                            "This product is successfully added", Toast.LENGTH_SHORT).show();

                    updateCartQty();
                }
                break;

            case R.id.img_cart:
                Intent intent = new Intent(DetailProductActivity.this, CartActivity.class);
                startActivity(intent);
                break;

            default:
                imageUrl = null;
                break;
        }

        if (imageUrl != null){
            Glide.with(DetailProductActivity.this)
                    .load(imageUrl)
                    .into(imgDetail);
        }
    }

    private void updateCartQty(){
        ArrayList<CartItem> list = mCartHelper.getAll();
        tvCart.setVisibility(View.GONE);
        if (list !=null) {
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
}
