package com.dht.saleapp;

import com.dht.pojo.Category;
import com.dht.pojo.Product;
import com.dht.service.CategoryService;
import com.dht.service.JdbcUtils;
import com.dht.service.ProductService;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {
    @FXML private ComboBox<Category> cbCategories;
    @FXML private TableView<Product> tbProducts;
    @FXML private TextField txtKeywords;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            CategoryService s = new CategoryService();
            List<Category> cates = s.getCates("");
            
            this.cbCategories.setItems(FXCollections.observableList(cates));
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        loadColumns();
        loadData("");
        
        this.txtKeywords.textProperty().addListener((obj) -> {
            loadData(this.txtKeywords.getText());
        });
    }
    
    private void loadColumns() {
        TableColumn colId = new TableColumn("Mã sản phẩm");
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        
        TableColumn colName = new TableColumn("Tên sản phẩm");
        colName.setPrefWidth(200);
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn colPrice = new TableColumn("Gía sản phẩm");
        colPrice.setPrefWidth(200);
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        
        this.tbProducts.getColumns().addAll(colId, colName, colPrice);
    }
    
    private void loadData(String kw) {
        try {
            Connection conn = JdbcUtils.getConn();
            ProductService s = new ProductService(conn);
            List<Product> products = s.getProducts(kw);
            this.tbProducts.setItems(FXCollections.observableList(products));
            
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
