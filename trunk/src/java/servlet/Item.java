/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;

import agorithms.*;
import agorithms.TopNItemBased;
import agorithms.Utils;
//import database.dbConnection;
import com.mysql.jdbc.Statement;
import database.dbConnection;
import java.io.File;
import java.sql.ResultSet;
import org.json.JSONArray;

/**
 *
 * @author hsb
 */
public class Item extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    RequestDispatcher rd;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            TopNItemBased topNItem = new TopNItemBased();            
            Utils util = new Utils();
            int maxNumber = 2000;
            double userItem[][] = new double[maxNumber][maxNumber];
            double itemItem[][] = new double[maxNumber][maxNumber];
            int maxItem = 0;
            int maxUser = 0;
            int numUserItem[] = new int[2];
//            int listItem[] = new int[10];
            int N = 10;
            int act = 0;
            int checkFileContent = 0;
            String userItemParam[] = new String[20];
            //String userItemParam ="";
            String action = "";
            String countParam = "";
            String userParam = "";
            String clientID = "";
            int checkFileExists = 1;
//            countParam = request.getParameter("count");
//            int count = Integer.parseInt(countParam);

//            for (int i = 0; i < count; i++) {
//                
//            }
            userItemParam = request.getParameterValues("userItem");
            JSONObject record = new JSONObject();
            JSONObject list = new JSONObject();
//            JSONArray arr = new JSONArray(param);
//            out.print(arr.toString());
//            for (int i = 0; i < arr.length(); i++) {
//                record = arr.getJSONObject(i);
//                System.arraycopy(arr, act, list, act, act);
//                list.put(record.getString("maTaiKhoan"), i);
//            }
            action = request.getParameter("action");
            userParam = request.getParameter("user");
            clientID = request.getParameter("clientID");
            
            File data = new File(getServletContext().getRealPath("/upload/" + clientID + ".txt"));

            
            
            if("buildModel".equals(action)) {
                if(userItemParam != null)
                {
                    int z = 0;
                    String param = "";
                    int user = 0;
                    int item = 0;
                    double val = 0;
                    int dem  = 0;
                    for (int i = 0; i < userItemParam.length; i++) {
                        param += userItemParam[i];
                       dem++;
                       JSONArray arr = new JSONArray(userItemParam[i]);
                        while (z < arr.length())
                        {
                            record = arr.getJSONObject(z);
                            user = record.getInt("maTaiKhoan");
                            item = record.getInt("maSanPham");
                            val = record.getDouble("value");
                            userItem[user][item] = val;
                            if (user > maxUser) {
                                maxUser = user;
                            }
                            if (item > maxItem) {
                                maxItem = item;
                            }
                            z++;
                        }
                        z=0;
                    }
                    util.writeFile(userItem, data, maxUser, maxItem);
                } else {
                    if(!data.exists()) {
                        out.println("Data file does not exists.");
                        checkFileExists = 0;
                    }
                }
                
                if(checkFileExists == 1)
                {
                    File model = new File(getServletContext().getRealPath("/WEB-INF/classes/data/" + clientID + "Model.txt"));
                    File recommendation = new File(getServletContext().getRealPath("/WEB-INF/classes/data/" + clientID + "Recommendation.txt"));
                    checkFileContent = util.readFile(userItem, numUserItem, data);
                    if( checkFileContent == -1) {
                        out.println("File content wrong format.");
                    } else {
                        maxUser = numUserItem[0] + 1;
                        maxItem = numUserItem[1] + 1;
//                        itemItem = topNItem.buildModel(userItem, maxUser, maxItem);
                        topNItem.generateTopN(data, recommendation);
//                        util.writeFile(itemItem, model, maxUser, maxItem);
                        out.println("Build model sucessfully");
                    }
                }
            }
            
            if("getRecommendation".equals(action)){
                File recommendation = new File(getServletContext().getRealPath("/WEB-INF/classes/data/" + clientID +  "Recommendation.txt"));
                int userId = Integer.parseInt(userParam);
                if(!recommendation.exists()) {
                    out.println("Model file does not exists.");
                } else {
                    util.readFile(itemItem, numUserItem, recommendation);
                    maxUser = numUserItem[0];
                    maxItem = numUserItem[1];
                    int listItem[] = new int[maxItem];
                    for (int j=0; j < 10; j++)
                        listItem[j] = (int) itemItem[userId][j];
                    //listItem = topNItem.topN(userItem, itemItem, userId-1 , maxUser, maxItem, N, model);
                    JSONObject json = new JSONObject();
                    JSONObject items = new JSONObject();
                    for (int i = 0; i < N; i++) {
                        items.put( i + "" , new Integer(listItem[i]));
                    }
                    json.put("maTaiKhoan", new Integer(userId));
                    json.put("clientID", clientID);
                    json.put("listItem", items);
                    out.print(json.toString());
                }
            }
            
            
            
//            out.println("Dem: "+ dem);
            
            
            
//                z=0;
//            out.println("total: " +z );
            
           // File f = new File("userItem.txt");
//            File f = new File("/media/hsb/Study/UIT/Khoa luan/RecommendationSystem/src/java/data/user.txt");
//            util.writeFile(userItem, f, maxUser+1, maxItem+1);
//            JSONObject json = new JSONObject();
//            JSONArray jArr = new JSONArray();
            
//            for(int i=0; i< maxUser+1; i++) {
//                for(int j=0; j< maxItem+1; j++)
//                {
//                    if(userItem[i][j] !=0)
//                    {
//                        
//                        JSONObject items = new JSONObject();
//                        items.put("maTaiKhoan", new Integer(i));
//                        items.put("maSanPham", new Integer(j));
//                        items.put("value", new Double(userItem[i][j]));
//                        //json.put("", items);
//                        jArr.put(items);
////                          s += "maTK: " + i + " maSP: " + j + " val: " + userItem[i][j] + "--";
//                    }
//                }
//            }
//            out.print(jArr.toString());
            
//            out.print(userItem.toString());
            
            //userItem1 = util.readFile(userItem, numUserItem, base);
            //act = Integer.parseInt(action);

//            dbConnection db = new dbConnection();
//            userItem = db.getUserItemFromDatabase(numUserItem);
             
//            maxUser = numUserItem[0]+1;
//            maxItem = numUserItem[1]+1;
             
//            
//            out.println("write 11");
//            dbConnection db = new dbConnection();
//            
//            ResultSet rs = null;
//            String sql = "select nk.maPhien, maTaiKhoan, round(max(giaDau)/max, 1) *10 as rate from nhat_ki_dau_gia nk, ( SELECT maPhien, max(giaDau) as max FROM nhat_ki_dau_gia GROUP BY maPhien) AS t " +
//                           " where nk.maPhien = t.maPhien group by maTaiKhoan, nk.maPhien ";
//                
//            select p.maSanPham, maTaiKhoan, round(max(giaDau)/max, 1) *10 as rate from nhat_ki_dau_gia nk, phien_dau_gia p, ( SELECT maPhien, max(giaDau) as max FROM nhat_ki_dau_gia GROUP BY maPhien) AS t where nk.maPhien = t.maPhien and p.maPhien = nk.maPhien  group by maTaiKhoan, nk.maPhien
                    
//            Statement st = (Statement) db.getCon().createStatement();
//            rs = st.executeQuery(sql);
//            out.println("12");
//            File myfile = new File(getServletContext().getRealPath("/WEB-INF/classes/data/ebid.txt"));
//            FileWriter output = new FileWriter(myfile);
//            //FileWriter output = new File(getServletContext().getRealPath( clientID + ".txt"));
//            int dem = 0;
//            while(rs.next()) {
//                int maPhien = rs.getInt("maPhien");
//                int maTaiKhoan = rs.getInt("maTaiKhoan");
//                int rate = rs.getInt("rate");
//                String s = maPhien + "\t" + maTaiKhoan + "\t" + rate + "\t" + "0";
//                BufferedWriter buffer=new BufferedWriter(output);
//                  buffer.write(s);
//                  buffer.write("\n");
//                  buffer.flush();
//                dem++;  
//            }
//            out.println("dem: "+dem);
//            maxUser = maxUser +1;
//            maxItem = maxItem +1;
             
//            out.println("maxUser2  :"+ maxUser + " maxItem: "+maxItem +"\n");
            //Build model
            
            //Get recommendation
            
            
            
            
//            
//            File base = new File("C:/xampp/RecommendationSystem/src/java/data/u1.base");
//            userItem = util.readFile(userItem, numUserItem, base);
//            maxUser = numUserItem[0];
//            maxItem = numUserItem[1];
            
//            for(int i=0; i < maxUser; i++)
//            { 
//                for(int j=0; j < maxUser; j++)
//                    System.out.print(userItem[i][j] + "\n");
//            }
            
            
//            for(int i=0; i < maxItem; i++)
//            { 
//                for(int j=0; j < maxItem; j++)
//                    System.out.print(itemItem[i][j] + "\n");
//            }
        
        }
        catch (Exception ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         processRequest(request, response); 
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
            
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
