/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 *
 * @author Admin
 */
public class ProductTester {
    private static Connection conn;
    
    @BeforeAll
    public static void setUpClass() {
        try {
            conn = JdbcUtils.getConn();
        } catch (SQLException ex) {
            Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterAll
    public static void tearDownClass() {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductTester.class.getName()).log(Level.SEVERE, null, ex);
            };
    }
    
    @Test
    public void testWithKeyword() throws SQLException {
        ProductService s = new ProductService(conn);
        List<Product> products = s.getProducts("iphone");
        
        products.forEach(p -> {
            Assertions.assertTrue(p.getName().toLowerCase().contains("iphone"));
        });
    }
    
    @Test
    public void testWithUnknowKeyword() throws SQLException {
        ProductService s = new ProductService(conn);
        List<Product> products = s.getProducts("273627uiesdsjd#$%^");
        
        Assertions.assertEquals(0, products.size());
    }
    
    @Test
    public void testException() throws SQLException {
        Assertions.assertThrows(SQLDataException.class, () -> {
            ProductService s = new ProductService(conn);
             List<Product> products = s.getProducts(null);
        });
    }
    
    @Test
    @DisplayName("...")
    @Tag("add-product")
    public void testAddProductWithInvalidCateId() {
        ProductService s = new ProductService(conn);
        
        Product p = new Product();
        p.setName("ABC");
        p.setPrice(new BigDecimal(100));
        p.setCategoryId(999);
        
        Assertions.assertFalse(s.addProduct(p));
    }
    
    @Test
    @DisplayName("...")
    @Tag("add-product")
    public void testAddProductWithInvalidName() {
        ProductService s = new ProductService(conn);
        
        Product p = new Product();
        p.setName(null);
        p.setPrice(new BigDecimal(100));
        p.setCategoryId(1);
        
        Assertions.assertFalse(s.addProduct(p));
    }
    
    @Test
    @DisplayName("...")
    @Tag("add-product")
    public void testAddProduct() {
        ProductService s = new ProductService(conn);
        
        Product p = new Product();
        p.setName("ABC");
        p.setPrice(new BigDecimal(99999));
        p.setCategoryId(2);
        
        Assertions.assertTrue(s.addProduct(p));
    }
    
    @ParameterizedTest
    @CsvSource({"P1,99,9999,false", ",99,9999,false", "P1,99,2,true"})
    public void testDataSource(String name, BigDecimal price, int cateId, boolean expected) {
        ProductService s = new ProductService(conn);
        
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCategoryId(cateId);
        
        Assertions.assertEquals(expected, s.addProduct(p));
    }
}
