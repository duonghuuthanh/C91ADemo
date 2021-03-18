/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Category;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Admin
 */
public class CategoryTester {
    @Test
    @DisplayName("Kiểm thử số lượng danh mục >= 3")
    public void testQuantity() throws SQLException {
        CategoryService s = new CategoryService();
        List<Category> cates = s.getCates("");
        
        Assertions.assertTrue(cates.size() >= 3);
    }
    
    @Test
    @DisplayName("Kiểm thử tên danh mục phải duy nhất")
    public void testUniqueName() throws SQLException {
        CategoryService s = new CategoryService();
        List<Category> cates = s.getCates("");
        
        List<String> names = new ArrayList<>();
        cates.forEach(c -> names.add(c.getName()));
        
        Set<String> temp = new HashSet<>(names);
        
        Assertions.assertEquals(names.size(), temp.size());
    }
    
    @Test
    @DisplayName("Kiểm tra ném đúng loại ngoại lệ mong muốn")
    public void testException() {
        Assertions.assertThrows(SQLDataException.class, () -> {
            CategoryService s = new CategoryService();
            s.getCates(null);
        });
    }
    
    @Test
    public void testTimeout() {
        Assertions.assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            CategoryService s = new CategoryService();
            s.getCates("");
        });
    }
}
