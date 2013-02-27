/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author user
 */
public class dbConnection {
    private Connection con;
    
    public dbConnection() {
        try {
            String user="root";
            String pass="";
            String driver="com.mysql.jdbc.Driver";
            String url="jdbc:mysql://localhost/bee20758_ebid";
            Class.forName(driver);
            con=DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(dbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the con
     */
    public Connection getCon() {
        return con;
    }

    public double[][] getItemItemFromDatabase() throws SQLException
    {
        int maxItem = 0;
        String sql = "select maSanPham1, maSanPham2, value from itemitem";
        ResultSet rs = null;
        Statement st = getCon().createStatement();
        rs = st.executeQuery(sql);
        maxItem = 2000;
        double itemitem[][] = new double[maxItem][maxItem];
        while(rs.next()) {
            int maSanPham1 = rs.getInt("maSanPham1");
            int maSanPham2 = rs.getInt("maSanPham2");
            double value = rs.getDouble("value");
            itemitem[maSanPham1][maSanPham2] = value;
        }
        return itemitem;
    }
    
    public double[][] getUserItemFromDatabase(int  numUserItem[]) throws SQLException
    {
        int maxItem = 0;
        String sql = "select maTaiKhoan, maSanPham, value from useritem";
        ResultSet rs = null;
        Statement st = getCon().createStatement();
        rs = st.executeQuery(sql);
        maxItem = 2000;
        numUserItem[0] = 0;
        numUserItem[1] = 0;
        double useritem[][] = new double[maxItem][maxItem];
        while(rs.next()) {
            int maTaiKhoan = rs.getInt("maTaiKhoan");
            int maSanPham = rs.getInt("maSanPham");
            if(maTaiKhoan > numUserItem[0]) numUserItem[0] = maTaiKhoan;
            if(maSanPham > numUserItem[1]) numUserItem[1] = maSanPham;
            double value = rs.getDouble("value");
            useritem[maTaiKhoan][maSanPham] = value;
        }
        return useritem;
    }
    
    public void setUserItemToDatabase()
    {
        
    }

}
