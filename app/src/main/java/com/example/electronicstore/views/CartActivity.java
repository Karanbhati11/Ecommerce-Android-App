package com.example.electronicstore.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.electronicstore.R;
import com.example.electronicstore.adapter.CartAdapter;
import com.example.electronicstore.databases.ProductDatabase;
import com.example.electronicstore.entity.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemRemovedListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Cart> cartList = new ArrayList<>();
    private ProductDatabase productDb;
    private TextView emptyCartTextView;
    private TextView totalAmountTextView;
    private LinearLayout totalAmountContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        setupToolbar();
        initializeViews();
        initializeDatabase();
        loadCartItems();
        setupRecyclerView();
        updateCartView();

        findViewById(R.id.checkoutButton).setOnClickListener(v -> proceedToCheckout());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void initializeViews() {
        emptyCartTextView = findViewById(R.id.emptyCartTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        totalAmountContainer = findViewById(R.id.totalAmountContainer);
    }

    private void initializeDatabase() {
        productDb = ProductDatabase.getInstance(this);
    }

    private void loadCartItems() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            cartList = productDb.productDao().getCartItemsForUser(currentUser.getUid());
        }
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(cartList, this); // No need for casting
        recyclerView.setAdapter(adapter);
    }

    private void updateCartView() {
        if (cartList.isEmpty()) {
            emptyCartTextView.setVisibility(TextView.VISIBLE);
            totalAmountContainer.setVisibility(TextView.GONE);
        } else {
            emptyCartTextView.setVisibility(TextView.GONE);
            totalAmountContainer.setVisibility(TextView.VISIBLE);
            updateTotalAmount();
        }
    }

    private void proceedToCheckout() {
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, CheckoutActivity.class));
        }
    }

    @Override
    public void onItemRemoved(Cart cart) {
        productDb.productDao().deleteCartItem(cart);
        cartList.remove(cart);
        adapter.notifyDataSetChanged();
        updateCartView();
    }

    @Override
    public void onItemQuantityChanged(Cart cart) {
        productDb.productDao().updateCartItem(cart);
        adapter.notifyItemChanged(cartList.indexOf(cart));
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        double totalAmount = 0.0;
        for (Cart cart : cartList) {
            totalAmount += cart.getProductPrice() * cart.getQuantity();
        }
        totalAmountTextView.setText(String.format("Total: $%.2f", totalAmount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
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
