package com.dht.saleapp;

import com.dht.pojo.Category;
import com.dht.pojo.Product;
import com.dht.service.CategoryService;
import com.dht.service.JdbcUtils;
import com.dht.service.ProductService;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable {
    @FXML private ComboBox<Category> cbCategories;
    @FXML private TableView<Product> tbProducts;
    @FXML private TextField txtKeywords;
    @FXML private TextField txtName;
    @FXML private TextField txtPrice;
    
    
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
        
        this.tbProducts.setRowFactory(obj -> {
            TableRow row = new TableRow();
            
            row.setOnMouseClicked(evt -> {
                try {
                    Product p = this.tbProducts.getSelectionModel().getSelectedItem();
                    txtName.setText(p.getName());
                    txtPrice.setText(p.getPrice().toString());
                    
                    Category c = new CategoryService().getCateById(p.getCategoryId());
                    
                    this.cbCategories.getSelectionModel().select(c);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            return row;
        });
    }
    
    public void addProduct(ActionEvent evt) {
        Product p = new Product();
        p.setName(txtName.getText());
        p.setPrice(new BigDecimal(txtPrice.getText()));
        Category c = this.cbCategories.getSelectionModel().getSelectedItem();
        p.setCategoryId(c.getId());
        
        Connection conn;
        try {
            conn = JdbcUtils.getConn();
            
            ProductService s = new ProductService(conn);
            if (s.addProduct(p) == true) {
                 Utils.getAlertBox("SUCCESSFUL", Alert.AlertType.INFORMATION).show();
                 loadData("");
            } else
                 Utils.getAlertBox("FAILED", Alert.AlertType.WARNING).show();
            
             conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
                
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
        
        TableColumn colAction = new TableColumn();
        colAction.setCellFactory((obj) -> {
            Button btn = new Button("Xóa");
            
            btn.setOnAction(evt -> {
                Utils.getAlertBox("Bạn chắc chắn xóa không?", Alert.AlertType.CONFIRMATION)
                        .showAndWait().ifPresent(bt -> {
                    if (bt == ButtonType.OK) {
                        try {
                            TableCell cell = (TableCell)((Button) evt.getSource()).getParent();
                            Product p = (Product) cell.getTableRow().getItem();
                            
                            try (Connection conn = JdbcUtils.getConn()) {
                                ProductService s = new ProductService(conn);
                                if (s.deleteProduct(p.getId()) == true) {
                                    Utils.getAlertBox("SUCCESSFUL", Alert.AlertType.INFORMATION).show();
                                    this.loadData("");
                                } else {
                                    Utils.getAlertBox("FAILED", Alert.AlertType.ERROR).show();
                                }
                            }
                            
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                });
            });
            
            TableCell cell = new TableCell();
            cell.setGraphic(btn);
            return cell;
        });
        
        this.tbProducts.getColumns().addAll(colId, colName, colPrice, colAction);
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
