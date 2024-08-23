package com.example.electronicstore.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.electronicstore.entity.Cart;
import com.example.electronicstore.entity.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getProducts();

    @Insert
    void insertProduct(Product product);

    @Insert
    void insertCartItem(Cart cart);

    @Query("SELECT * FROM cart")
    List<Cart> getCartItems();

    @Query("SELECT * FROM cart WHERE productName = :productName")
    List<Cart> getCartItemsByProductName(String productName);

    @Update
    void updateCartItem(Cart cart);

    @Delete
    void deleteCartItem(Cart cart);

    @Query("SELECT * FROM product WHERE id IN (:productIds)")
    List<Product> getProductById(int[] productIds);

    @Query("SELECT * FROM cart WHERE userId = :userId AND productId = :productId LIMIT 1")
    Cart getCartItemForUserByProductId(String userId, int productId);

    @Query("SELECT * FROM cart WHERE userId = :userId")
    List<Cart> getCartItemsForUser(String userId);

    @Query("DELETE FROM cart WHERE userId = :userId")
    void clearCartForUser(String userId);
}
