package com.sidiq.intel.myshoppingmall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailProductActivity extends AppCompatActivity {
    private TextView tvName, tvPrice;
    private Button btnAddToCart;
    private ImageView imgDetail;
    private Spinner spnSize;
    private TextView tvDesc;

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

        getSupportActionBar().setTitle("Detail Produk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Product selectedProduct = getIntent().getParcelableExtra("product");
        tvName.setText(selectedProduct.getName());
        tvPrice.setText(selectedProduct.getPrice());
        Glide.with(DetailProductActivity.this)
                .load(selectedProduct.getImageUrl())
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
