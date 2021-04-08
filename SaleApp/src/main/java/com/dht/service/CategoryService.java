/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dht.service;

import com.dht.pojo.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class CategoryService {
    public List<Category> getCates(String kw) throws SQLException {
        if (kw == null)
            throw new SQLDataException();
        
        List<Category> cates;
        try (Connection conn = JdbcUtils.getConn()) {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM category");
            cates = new ArrayList<>();
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                
                cates.add(c);
            }
        }
        return cates;
    }
    
   public Category getCateById(int cateId) throws SQLException {
       Connection conn = JdbcUtils.getConn();
       String sql ="SELECT * FROM category WHERE id=?";
       PreparedStatement stm = conn.prepareStatement(sql);
       stm.setInt(1, cateId);
       
       ResultSet rs = stm.executeQuery();
       
       Category c = null;
       while (rs.next()) {
           c = new Category();
           c.setId(rs.getInt("id"));
           c.setName(rs.getString("name"));
           break;
       }
       
       return c;
   }
}
