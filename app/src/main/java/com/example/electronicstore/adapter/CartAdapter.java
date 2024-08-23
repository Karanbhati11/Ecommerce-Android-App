package com.example.electronicstore.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.electronicstore.R;
import com.example.electronicstore.entity.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Cart> cartList;
    private OnItemRemovedListener listener;

    public CartAdapter(List<Cart> cartList, OnItemRemovedListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.productName.setText(cart.getProductName());
        holder.productPrice.setText("$" + cart.getProductPrice());
        holder.productQuantity.setText(String.valueOf(cart.getQuantity()));
        Glide.with(holder.itemView.getContext()).load(cart.getProductImageUrl()).into(holder.productImage);

        holder.removeButton.setOnClickListener(v -> listener.onItemRemoved(cart));

        holder.reduceButton.setOnClickListener(v -> {
            int currentQuantity = cart.getQuantity();
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                cart.setQuantity(newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));
                listener.onItemQuantityChanged(cart);
            } else {
                listener.onItemRemoved(cart);
            }
        });

        holder.plusButton.setOnClickListener(v -> {
            int newQuantity = cart.getQuantity() + 1;
            cart.setQuantity(newQuantity);
            holder.productQuantity.setText(String.valueOf(newQuantity));
            listener.onItemQuantityChanged(cart);
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        ImageView reduceButton;
        ImageView plusButton;
        TextView productName;
        TextView productPrice;
        EditText productQuantity;
        Button removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageView);
            reduceButton = itemView.findViewById(R.id.reduceButton);
            plusButton = itemView.findViewById(R.id.plusButton);
            productName = itemView.findViewById(R.id.productNameTextView);
            productPrice = itemView.findViewById(R.id.productPriceTextView);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }

    public interface OnItemRemovedListener {
        void onItemRemoved(Cart cart);
        void onItemQuantityChanged(Cart cart);
    }
}
