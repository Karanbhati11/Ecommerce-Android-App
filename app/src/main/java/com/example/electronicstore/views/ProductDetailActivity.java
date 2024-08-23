package com.example.electronicstore.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.electronicstore.R;
import com.example.electronicstore.databases.ProductDatabase;
import com.example.electronicstore.entity.Cart;
import com.example.electronicstore.entity.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productName, productPrice, productDescription;
    private ImageView productImage;
    private EditText productQuantity;
    private Product product;
    private ProductDatabase productDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
            startActivity(intent);
        });

        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productImage = findViewById(R.id.productImage);
        productQuantity = findViewById(R.id.productQuantity);
        Button addToCartButton = findViewById(R.id.addToCartButton);
        Button minusButton = findViewById(R.id.minusButton);
        Button plusButton = findViewById(R.id.plusButton);

        int productId = getIntent().getIntExtra("product_id", -1);
        productDb = ProductDatabase.getInstance(this);
        product = productDb.productDao().getProductById(new int[]{productId}).get(0);

        productName.setText(product.getName());
        productPrice.setText("$" + product.getPrice());
        productDescription.setText(product.getDescription());
        Glide.with(this).load(product.getImageUrl()).into(productImage);

        minusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(productQuantity.getText().toString());
            if (quantity > 1) {
                productQuantity.setText(String.valueOf(quantity - 1));
            }
        });

        plusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(productQuantity.getText().toString());
            productQuantity.setText(String.valueOf(quantity + 1));
        });

        addToCartButton.setOnClickListener(v -> {
            int quantity;
            try {
                quantity = Integer.parseInt(productQuantity.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            addToCart(quantity);
        });
    }

    private void addToCart(int quantity) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please log in to add items to your cart.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        // Get cart items for this user and this product
        Cart existingCartItem = productDb.productDao().getCartItemForUserByProductId(userId, product.getId());

        if (existingCartItem != null) {
            // Update quantity if the product is already in the cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            productDb.productDao().updateCartItem(existingCartItem);
        } else {
            // Create a new cart item
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(product.getId());
            cartItem.setProductName(product.getName());
            cartItem.setProductPrice(product.getPrice());
            cartItem.setProductDescription(product.getDescription());
            cartItem.setProductImageUrl(product.getImageUrl());
            cartItem.setQuantity(quantity);
            productDb.productDao().insertCartItem(cartItem);
        }

        // Show a confirmation message
        Toast.makeText(this, product.getName() + " "+ " added to Cart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
