package com.example.electronicstore.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.electronicstore.R;
import com.example.electronicstore.databases.ProductDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CheckoutActivity extends AppCompatActivity {
    private EditText userNameEditText, userAddressEditText, userPhoneEditText;
    private EditText cardNumberEditText, cardNameEditText, expiryDateEditText, cvvEditText;
    private Button checkoutButton;
    private ProductDatabase productDb;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        productDb = ProductDatabase.getInstance(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        userNameEditText = findViewById(R.id.userNameEditText);
        userAddressEditText = findViewById(R.id.userAddressEditText);
        userPhoneEditText = findViewById(R.id.userPhoneEditText);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardNameEditText = findViewById(R.id.cardNameEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);

        // Set maximum length for phone number and card number
        userPhoneEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        cardNumberEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});

        checkoutButton = findViewById(R.id.checkoutButton);
        checkoutButton.setOnClickListener(v -> validateAndCheckout());
    }

    private void validateAndCheckout() {
        if (TextUtils.isEmpty(userNameEditText.getText())) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userAddressEditText.getText())) {
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userPhoneEditText.getText()) || userPhoneEditText.getText().length() != 10) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cardNumberEditText.getText()) || cardNumberEditText.getText().length() != 16) {
            Toast.makeText(this, "Please enter a valid 16-digit card number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cardNameEditText.getText())) {
            Toast.makeText(this, "Please enter the name on your card", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(expiryDateEditText.getText())) {
            Toast.makeText(this, "Please enter the expiry date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cvvEditText.getText()) || cvvEditText.getText().length() != 3) {
            Toast.makeText(this, "Please enter a valid 3-digit CVV", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear the cart for the current user after a successful checkout
        if (currentUser != null) {
            productDb.productDao().clearCartForUser(currentUser.getUid());
        }

        // Proceed with checkout process
        Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CheckoutActivity.this, ThankYouActivity.class);
        startActivity(intent);
        finish();
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
