package com.example.electronicstore.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.electronicstore.R;
import com.example.electronicstore.databases.ProductDatabase;
import com.example.electronicstore.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProductDatabase productDb;
    Product mTempProduct = new Product();
    private List<Product> productList = new ArrayList<>();

    TextView clicktext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating db instance
        productDb = ProductDatabase.getInstance(this);
        clicktext = findViewById(R.id.clicktext);
        clicktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Proceed to next activity after products are created
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Creating products method call
//       createProducts();


    }

    // Function to create new products
    public void createNewProduct(Product product) {
        mTempProduct = product;
        productDb.productDao().insertProduct(mTempProduct);
        productList.add(mTempProduct);
    }

    // Function to create initial products
    public void createProducts() {
        Product p = new Product();

        p.setName("Sony 4K Ultra HD TV");
        p.setDescription("Experience the true beauty of 4K HDR with the Sony Ultra HD TV. Enjoy breathtaking picture quality and immersive sound.");
        p.setPrice(599.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/1fcedc41-d83c-4ff5-83a1-68d13e96c6e4.d38b207c2e2c1f794fc49a95818612d7.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Apple MacBook Pro");
        p.setDescription("The new MacBook Pro is faster and more powerful than ever, yet remarkably thin and light.");
        p.setPrice(1299.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/32b7a184-7dd8-4bdf-9a4a-3f93943be753.9c4a16ff28789caa03fffc0f57a8615a.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Bose QuietComfort 35 II");
        p.setDescription("Bose QuietComfort 35 II Wireless Bluetooth Headphones, Noise-Cancelling, with Alexa voice control.");
        p.setPrice(299.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/7a87a09f-618e-4eee-b780-1fe16c48c4dc.fd1f2aa79d9e50b43206990d6bc563de.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Canon EOS Rebel T7");
        p.setDescription("Capture stunning photos and videos with the Canon EOS Rebel T7 DSLR Camera.");
        p.setPrice(449.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/f613fb16-07f6-4920-8a4b-961f879a42c0.60b91e019b196da8ca97a83cfaa2bbab.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Samsung Galaxy S21");
        p.setDescription("Meet Galaxy S21 5G. All-new camera and powerful performance in a sleek design.");
        p.setPrice(799.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/2d643099-c36c-4894-a03b-c07e05b44152.0c08187cb9a5c371c112acbf1b2e5bd8.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Fitbit Charge 4");
        p.setDescription("Fitbit Charge 4 Fitness and Activity Tracker with Built-in GPS, Heart Rate, Sleep & Swim Tracking.");
        p.setPrice(149.99);
        p.setImageUrl("https://i5.walmartimages.ca/images/Enlarge/619/394/6000205619394.jpg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("GoPro HERO9");
        p.setDescription("More power. More clarity. More stability. The revolutionary GoPro HERO9 Black action camera.");
        p.setPrice(399.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/f344d5ef-18b4-4f05-9c16-9172524eb652.7a011a384f13c9a11a7448d3eb4cf107.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);

        p.setName("Nintendo Switch");
        p.setDescription("Nintendo Switch is designed to fit your life, transforming from home console to portable system in a snap.");
        p.setPrice(299.99);
        p.setImageUrl("https://i5.walmartimages.com/asr/125cbd1e-7556-4434-8a3d-14ecf1aac94a.926921f5650c6fd18185a4a6dc8dab03.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF");
        createNewProduct(p);
    }
}
