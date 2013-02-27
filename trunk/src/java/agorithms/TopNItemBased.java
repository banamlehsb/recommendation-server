/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agorithms;

import java.sql.Statement;
import database.dbConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
/**
 *
 * @author hsb
 */
public class TopNItemBased {
    //k most similar items to item j ( 10 <= k <=30)
    int k = 20;
    //the number of items to be recommended
    int N = 10;
    Utils util = new Utils();
    //dbConnection con = new dbConnection();
    
    public float computeAverage(double itemUser[][], int user, int maxItem, float result[])
    {
        float average  = 0;
        int dem = 0;
        for(int i=0; i< maxItem; i++)
        {
            if(itemUser[user][i] != 0) {
                dem++;
                average += itemUser[user][i];
            }
        }
//        for(int i=0; i< maxItem; i++)
//        {
//            if(itemUser[i][user] != 0) {
//                dem++;
//                average += itemUser[i][user];
//            }
//        }
        result[0] = average;
        result[1] = dem;
        return (float) average/dem;
    }
    
    public float computeVariance(double itemUser[][], int user, float average, int maxItem)
    {
        float variance  = 0;
        for(int i=0; i< maxItem; i++)
        {
            if(itemUser[user][i] != 0) {
                variance += (float) (itemUser[user][i] - average)*(itemUser[user][i] - average);
            }
        }
//        for(int i=0; i< maxUser; i++)
//        {
//            if(itemUser[i][user] != 0) {
//                variance += (float) (itemUser[i][user] - average)*(itemUser[i][user] - average);
//            }
//        }
        return variance;
    }
    
    public void normalizeRating(double itemUser[][], int maxUser, int maxItem)
    {
        float average = 0;
        double temp = 0;
        float variance  = 0;
        float result[] = new float[2];
        for(int i=0; i< maxUser; i++)
        {
            average = computeAverage(itemUser, i, maxItem, result);
            variance = computeVariance(itemUser, i, average, maxItem);
            //System.out.print(" i " + i + " avg = "+average + "\n");
            for(int j=0; j< maxItem; j++)
            {
                if(itemUser[i][j] != 0) 
                {
                   //itemUser[i][j] = (float) itemUser[i][j] / average;
                   itemUser[i][j] = (float) ((itemUser[i][j] - average)/(Math.sqrt(variance)));
                    //itemUser[i][j] = (itemUser[i][j] - 1) / (5-1);
                    //System.out.print(" i " + i + " va= " + itemUser[i][j] + " avg = "+average + "\n");
                }
            }
        }
    }
    
    //build model (page 7 - Item-Based Top-N Recommendation Algorithms.pdf)
    //itemUser: itemUser matrix 
    //maxUser: the number of user of itemUser matrix
    //maxItem: the number of item of itemUser matrix
    public double[][] buildModel(double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException, Exception
    {
        double M[][] = new double[maxItem][maxItem];
        Pearson p = new Pearson();
        double max = 0;
        // line 1 - page 7 - Item-Based Top-N Recommendation Algorpithms.pdf
        for(int j=0; j< maxItem; j++)
        {
            for(int i=0; i< maxItem; i++)
            {
                if(i != j ) {
                    M[i][j] = p.computeCosine(j, i, itemUser, maxUser, maxItem);
                    //System.out.print(i + " vs " + j + " = "+M[i][j] +"\n");
                    //M[i][j] = p.computeItemBasedPearson(j, i, itemUser, maxUser, maxItem);
                    //M[i][j] = computeConditionalProbability(itemUser, i, j, maxUser, maxItem);
                }
                else {
                    M[i][j] = 0;
                }
            }
            //break;
        }
        
//        System.out.print("2"+"\n");
        // line 2 - page 7 - Item-Based Top-N Recommendation Algorithms.pdf
//        String sql2 = "insert into itemitem(maSanPham1,maSanPham2,value) values(?,?,?)";
//        PreparedStatement st2 = con.getCon().prepareStatement(sql2);
        for(int j=0; j< maxItem; j++)
        {
            // get the kth largest values in M matrix
            //int dem = 0;
            max = util.getKthLargest(M, k, j, maxItem); 
            for(int i=0; i < maxItem; i++)
            {
                if(M[i][j] < max  ) {
                    M[i][j] = 0;
                }
//                if(M[i][j] !=0) {
//                    st2.setInt(1, i);
//                    st2.setInt(2, j);
//                    st2.setDouble(3, M[i][j]);
//                    st2.executeUpdate();
//                }
            }
        }
        
        //File f = new File("builModel.txt");
        //util.writeFile(M, f, maxUser, maxItem);
        return M;
    }
    
    
    // ApplyModel - page 11 - Item-Based Top-N Recommendation Algorithms.pdf
    
    public double[] applyModel(double[][] M, double[] U, int user, int N, int maxItem) throws SQLException
    {
        // X: m × 1 vector whose non-zero entries correspond to the top-N items that were recommended.
        double X[] = new double[maxItem];
        double max  = 0;
        //System.out.print("3.2.1"+"\n");
        // line 1 - page 11 - Item-Based Top-N Recommendation Algorithms.pdf
        X = util.multiplyMatrix(M, U, maxItem);
        //System.out.print("3.2.2"+"\n");
        // line 2 - page 11 - Item-Based Top-N Recommendation Algorithms.pdf
        int dem = 0;
        for(int j = 0; j < maxItem; j++) {
            if(U[j] == 1 ) {
                X[j] = 0;
                
            } else 
            dem++;
        }
       // System.out.print("dem = " + dem + "\n");
        //System.out.print("3.2.3"+"\n");
//        for(int j = 0; j < maxItem; j++) {
//            {
//                System.out.print(X[j] + "\n");
//            }
//        }
        //System.out.print("3.2.3.3"+"\n");
        // get the kth largest values in M matrix
        max = util.getKthLargest(X, N, maxItem);
         for (int j = 0; j < maxItem; j++) {
            if (X[j] < max) {
                X[j] = 0;
            }
        }
        //System.out.print("3.2.4"+"\n");
        // line 3 - page 11 - Item-Based Top-N Recommendation Algorithms.pdf
//        String sql = "insert into recommendation(maTaiKhoan,maSanPham) values(?,?)";
//        PreparedStatement st = con.getCon().prepareStatement(sql);
//        for(int j = 0; j < maxItem; j++) {
//            if(X[j] < max) {
//                X[j] = 0; 
//                if(X[j] !=0) {
//                    st.setInt(1, user);
//                    st.setInt(2, j);
//                    st.executeUpdate();
//                }
//            }
//        }
        //System.out.print("3.2.5"+"\n");
        return X;
    }
    
    // get m × 1 vector U that stores the items that have already been purchased by the active user
    // The active user’s information in vector U is encoded by 
    // setting Ui = 1 if the user has purchased the ith item and zero otherwise
    
    public double[] getItemPurchased(double itemUser[][], int user, int maxItem)
    {
        double U[] = new double[maxItem];
        for(int i = 0; i < maxItem; i++)
        {
            if(itemUser[user][i] > 0) {
                U[i] = 1;
            }
            else {
                U[i] = 0;
            }
        }
        return U;
    }
    
       
    public double computeConditionalProbability(double itemUser[][], int ith, int jth, int maxUser, int maxItem)
    {
        double result = 0;
        double alpha = 0.5;
        double row = 0;
        int freqI = 0;
        int freqJ = 0;

        for(int i=0; i< maxUser; i++)
        {
            if(itemUser[i][ith] != 0 ) {
                    freqI++;
                }
            if(itemUser[i][jth] != 0 ) {
                freqJ++;
            }
            
//            if(itemUser[i][ith] != 0 &&  itemUser[i][jth] != 0) { //
//                    row++;// += itemUser[i][ith];
//            }
            
            if(itemUser[i][ith] != 0 &&  itemUser[i][jth] != 0) { //
                    row += itemUser[i][ith];
            }
            
        }
        
        
        
        result = row/(freqJ * Math.pow(freqI, alpha));
        if(row == 0 || freqI == 0 || freqJ == 0) {
            result = 0;
        }
        DecimalFormat df = new DecimalFormat(".###");
        String str= df.format(result);
        result= Double.valueOf(str);
        
        
        return result;
    }
    
    // generate N recommended items for specific user.
    public int[] topN(double itemUser[][], double itemitem[][], int user, int maxUser, int maxItem, int N, File f) throws FileNotFoundException, Exception
    {
        
        double M[][] = new double[maxItem][maxItem];
        double U[] = new double[maxItem];
        double X[] = new double[maxItem];
        int listItem[] = new int[N];
        //File f = new File("/media/hsb/Study/UIT/Khoa luan/RecommendationSystem/builModel.txt");
//        File f = new File(getServletContext().getRealPath("/WEB-INF/classes/data/u1.base"));
//        File f = new File("/builModel.txt");
            
        M = util.readFile(M, f);
        //M = buildModel(itemUser, maxUser, maxItem);
//        System.out.print("User = " +maxUser + " Item= " + maxItem +"\n");
        //System.out.print("3.1"+"\n");
//         for(int i=0; i < maxUser; i++)
//            { 
//                for(int j=0; j < maxItem; j++)
//                    System.out.print(itemUser[i][j] + "\n");
//            }
        U = getItemPurchased(itemUser, user, maxItem);
        //System.out.print("3.2"+"\n");
        X = applyModel(itemitem, U, user, N, maxItem);
        //System.out.print("3.3"+"\n");
        // The output of the algorithm is an m × 1 vector x 
        // whose non-zero entries correspond to the top-N items that were recommended
        int item  = 0;
        for(int k = 0; k < N; k++)
        {
            //if(X[x]!=0) 
            {
                item = util.getKthLargestPosition(X, k+1, maxUser, maxItem);
                listItem[k] = item;
                //System.out.print("Item: " + (k+1) + " Value= " + X[k] + "\n");
            }
        }  
        //System.out.print("3.4"+"\n");
        return listItem;
    }
    
    
    //
    public void generateTopN(File source, File dest)
    {
        int MaxNumber = 2000;
        Utils util = new Utils();
        int maxItem = 0;
        int maxUser = 0;
        int maxUserBase = 0;
        double itemUser[][] = new double[MaxNumber][MaxNumber];
        double temp[][] = new double[MaxNumber][MaxNumber];
        double itemUserTest[][] = new double[MaxNumber][MaxNumber];
        int numUserItemBase[] = new int[2];
        int numUserItemTest[] = new int[2];
        numUserItemTest[0] = 0;
        numUserItemTest[1] = 0;
        //Training set 
        
        //read training set file, return itemUser matrix with maxUser row and maxItem column
        // rating = itemUser[user][item]
        util.readFile(itemUser, numUserItemBase, source);
        
        //Testing set
        //read testing set file, return itemUserTest matrix with maxUser row and maxItem column
        // rating = itemUserTest[user][item]
        //get number of user, and number of item in matrix
        maxUser = numUserItemBase[0];
        maxItem = numUserItemBase[1];
        maxUserBase = 943;
        maxItem = 1682;
        // result matrix
        double X[] = new double[maxItem];
        // M matrix: item-item
        double M[][] = new double[maxItem][maxItem];
        // U matrix: purchased item
        double U[] = new double[maxItem];
        
        int item = 0;
        int x = 0;
        try {
            // result file
            FileWriter output = new FileWriter(dest);
            //build model 
            temp = itemUser;
            normalizeRating(itemUser, maxUserBase, maxItem);
            M = buildModel(itemUser, maxUserBase, maxItem);
            
            //with every user, generate N=10 items and save them in myfile.
            for(int i = 0; i < maxUser; i++)
            {
              //get purchased item from i user.
              U = getItemPurchased(temp, i, maxItem);
                    {
                        //return result matrix with N=10 non-zero item
                        X = applyModel(M, U, i, N, maxItem);
                        //write to file.
                        for(int k = 0; k < N; k++)
                        {
                            //if(X[k]!=0)
                            {
                                item = util.getKthLargestPosition(X, k+1, maxUser, maxItem);
                                String s = (i+1) + "\t" + (k+1) + "\t" + (item+1) + "\t" + "0";
                                BufferedWriter buffer=new BufferedWriter(output);
                                buffer.write(s);
                                buffer.write("\n");
                                buffer.flush();
                                x++;
                            }
                        } 
                        x = 0;
                    }
                }
        } catch ( Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
    }
    
    //compute hit rate.
    public void computeHitRate(String dataset, double eval[])
    {
        int MaxNumber  = 2000;
        int maxItem = 0;
        int maxUser = 0;
        Utils util = new Utils();
        double[][] test = new double[MaxNumber][MaxNumber];
        double[][] result = new double[MaxNumber][MaxNumber];
        int numUserItem[] = new int[2];
        
        //compare between testing set and result set        
        File testArr = new File("src/java/data/" + dataset + ".test");
        File resultArr = new File(dataset + ".rowCond");
        // read file and return matrix
        util.readFile(test, numUserItem, testArr);
        
        maxUser = numUserItem[0];
        maxItem = numUserItem[1];
//        maxUser = numUserItem[0];
        //maxItem = 1682;
        System.out.append(maxUser + " vs "+maxItem +"\n");
        util.readFile(result, numUserItem, resultArr);
        
        double HitRate = 0;
        double ARHR = 0;
        int hits = 0;
        double temp = 0;
        
        //The number of hits is the number of items in the test set 
        //that were also present in the top-N recommended items returned for each user
        for(int i = 0; i < maxUser; i++)
        {
          for(int j = 0; j < maxItem; j++)
          {
                if(test[i][j]!=0) 
                {
                  
                  for(int x = 0; x < 10; x++) 
                  {
                      //compare item in test set and item in result set.
                      //System.out.print(i + " " + (j+1) + " = " + result[i][x] + "\n");
                      if( (j+1) == result[i][x])
                      {
                          temp += (double) 1 / (x+1);
                          hits++;
                          //System.out.print(i+1 + " " + (j+1) + " = " + result[i][x] + " pos = " + (x+1) + "\n");
                          //break;
                      }
                      
                  }
                }
          }
        }
        
        ARHR = (double) temp/(maxUser*10);
        maxUser = 943;
        HitRate = (double) hits/(maxUser*10);
        DecimalFormat df = new DecimalFormat(".###");
        String str= df.format(HitRate);
        HitRate = Double.valueOf(str);
        eval[0] = HitRate;
        str= df.format(ARHR);
        ARHR = Double.valueOf(str);
        eval[1] = ARHR;
        System.out.print("ARHR == " + ARHR + "\n");
        System.out.print("ARHR == " + temp + "\n");
        System.out.print("ARHR == " + hits + "\n");
        System.out.print("HitRate == " + HitRate + "\n");
    }
}