/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agorithms;


import agorithms.Utils;
import data.*;
import java.io.*;
import java.util.ArrayList; 
import java.lang.Object;
import java.text.DecimalFormat;
import java.util.*;
/**
 *
 * @author hsb
 */
public class Pearson {
   
    //compute cosine similarity between item1 and item2
    public double computeCosine(int item1, int item2, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
        double cosine = 0;
        double tu = 0;
        double mau = 0;

        try {
          float temp1 = 0;
          float temp2 = 0;
          
              for(int j=0; j< maxUser; j++)
              {
                  //check user purchased both item1 and item2
                  //if(itemUser[j][item1] !=0 && itemUser[j][item2] !=0) 
                  {
                      temp1 += itemUser[j][item1] * itemUser[j][item1];
                      temp2 += itemUser[j][item2] * itemUser[j][item2];
                      tu += itemUser[j][item1] * itemUser[j][item2];
                  }
              }
              mau = Math.sqrt(temp1) * Math.sqrt(temp2);
              cosine = tu / mau;
         if(tu == 0 || mau == 0)
              cosine = 0;
         DecimalFormat df = new DecimalFormat(".###");
         String str= df.format(cosine);
         cosine = Double.valueOf(str);    
         
        } catch (Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
        return cosine;
    }
    
    public double computeCosineUser(int user1, int user2, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
        double cosine = 0;
        double tu = 0;
        double mau = 0;

        try {
          float temp1 = 0;
          float temp2 = 0;
          
              for(int j=0; j< maxItem; j++)
              {
                  //check user purchased both item1 and item2
                  //if(itemUser[j][item1] !=0 && itemUser[j][item2] !=0) 
                  {
                      temp1 += itemUser[user1][j] * itemUser[user1][j];
                      temp2 += itemUser[user2][j] * itemUser[user2][j];
                      tu += itemUser[user1][j] * itemUser[user2][j];
                  }
              }
              mau = Math.sqrt(temp1) * Math.sqrt(temp2);
              cosine = tu / mau;
              
          if(tu == 0 || mau == 0)
              cosine = 0;
        } catch (Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
        return cosine;
    }
    
    public double computeItemBasedPearson(int item1, int item2, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
//        int maxItem = 1682;
//        int maxUser = 943;
        double itemAverage1 = 0;
        double itemAverage2 = 0;
        double pearson = 0;
        double tu = 0;
        double temp1 = 0;
        double temp2 = 0;
        //System.out.print("Start cosine -  read file");
        
        //System.out.print(maxUser + "max" + maxItem);
        itemAverage1 = computePerItemAverage(item1, item2, itemUser, maxUser, maxItem);
        itemAverage2 = computePerItemAverage(item2, item1, itemUser, maxUser, maxItem);
        //System.out.print(" itemAverage1 = " + itemAverage1 + "\n" + " itemAverage2 = " + itemAverage2 +"\n");
        for(int i = 0; i < maxUser; i++)
        {
          for(int j = 0; j < maxItem; j++)
          {
               if(itemUser[i][item1] !=0 && itemUser[i][item2] !=0 )
               {
                    temp1 += (itemUser[i][item1] - itemAverage1)*(itemUser[i][item1] - itemAverage1);
                    temp2 += (itemUser[i][item2] - itemAverage2)*(itemUser[i][item2] - itemAverage2);
                    tu += (itemUser[i][item1] - itemAverage1)*(itemUser[i][item2] - itemAverage2);
               }
               //System.out.print(itemUser[i][j] + "--");
          }
          //System.out.print("User = "+ (i+1) +  " Tu la " + tu + "\n");
          //System.out.print("User = "+ (i+1) +  " Mau la " + (Math.sqrt(temp1) * Math.sqrt(temp2)) + "\n");
          //break;
          
          
        }
        if(tu==0 || temp1 ==0 || temp2 == 0) {
            pearson = 0;
        }
        else {
            pearson = tu/(Math.sqrt(temp1) * Math.sqrt(temp2));
        }
        return pearson;
        
    }
    
    public double computePerItemAverage(int item1, int item2, double itemUser[][], int maxUser, int maxItem) 
    {
        double tu = 0;
        int dem = 0;
        double average = 0;
        for(int i= 0; i< maxUser; i++)
          {
              //for(int j=0; j< maxItem; j++)
              {
                  if(itemUser[i][item1] != 0 && itemUser[i][item2] != 0)
                  { 
                      tu += itemUser[i][item1];
                      dem++;
                  }
              }
          }
        //System.out.print("User :" + (tu) + " Result " + (dem) + "\n");
        average = tu/dem;
        if(tu ==0 || dem ==0) {
            average = 0;
        }
        
        return average;
    }
    
    
    public double computePerUserAverage(int user1, int user2, double itemUser[][], int maxItem) 
    {
        double tu = 0;
        int dem = 0;
        double average = 0;
        for(int j = 0; j < maxItem; j++)
          {
              if(itemUser[user1][j] != 0 && itemUser[user2][j] !=0)
              { 
                  tu += itemUser[user1][j];
                  dem++;
              }
          }
        //System.out.print("User :" + (user1+1) + " Result " + (result/dem) + "\n");
        average = tu/dem;
        if(tu ==0 || dem ==0) {
            average = 0;
        }
        
        return average;
    }
    
    
    
    
    
    public double computeUserBasedPearson(int user1, int user2, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
//        int maxItem = 1682;
//        int maxUser = 943;
        double userAverage1 = 0;
        double userAverage2 = 0;
        double pearson = 0;
        double tu = 0;
        double temp1 = 0;
        double temp2 = 0;
        //System.out.print("Start cosine -  read file");
        
        //System.out.print(maxUser + "max" + maxItem);
        userAverage1 = computePerUserAverage(user1, user2, itemUser, maxItem);
        userAverage2 = computePerUserAverage(user2, user1, itemUser, maxItem);
        //System.out.print(" userAverage1 = " + userAverage1 + "\n" + "userAverage2 = " + userAverage2 +"\n");
        //for(int i = 0; i < maxUser; i++)
        {
          for(int j = 0; j < maxItem; j++)
          {
               if(itemUser[user1][j] !=0 && itemUser[user2][j] !=0 )
               {
                    temp1 += (itemUser[user1][j] - userAverage1)*(itemUser[user1][j] - userAverage1);
                    temp2 += (itemUser[user2][j] - userAverage2)*(itemUser[user2][j] - userAverage2);
                    tu += (itemUser[user1][j] - userAverage1)*(itemUser[user2][j] - userAverage2);
               }
//               System.out.print(itemUser[i][j] + "--");
          }
          //System.out.print("User = "+ (i+1) +  " Tu la " + tu + "\n");
          //System.out.print("User = "+ (i+1) +  " Mau la " + (Math.sqrt(temp1) * Math.sqrt(temp2)) + "\n");
          //break;
          //System.out.print("\n");
          
        }
        if(tu==0 || temp1 ==0 || temp2 == 0) {
            pearson = 0;
        }
        else {
            pearson = tu/(Math.sqrt(temp1) * Math.sqrt(temp2));
        }
        return pearson;
        
    }
    TopNItemBased topN = new TopNItemBased();
    public double computeWeightedSum(int user, int item, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
        double userAverage = 0;
        double userAverage1 = 0;
        double tu = 0;
        double mau = 0;
        double pearson = 0;
        double predicted = 0;
        
        try {
            userAverage1 = computePerUserAverage(user, user, itemUser, maxItem);
            
            for(int j = item; j < maxItem; j++)
            {
              for(int i = 0; i < maxUser; i++)
              {
                  if(j==item && itemUser[i][j] !=0) {
                      userAverage = computePerUserAverage(i, user, itemUser, maxItem);
                      pearson = computeUserBasedPearson(user, i, itemUser, maxUser, maxItem);
                      
                      //pearson = computeCosine(item, j, itemUser, maxUser, maxItem);
                      //System.out.print("User =" + i + " Tu: " + (itemUser[i][j]) + "-" + userAverage + "*" + pearson +"\n");
                      tu += (itemUser[i][j]-userAverage) * pearson;
                      mau += Math.abs(pearson);
                      //System.out.print("Pearson = " + pearson + "\n");
                  }
              }
              userAverage = 0;
              break;
            }
            predicted = Float.parseFloat(userAverage1 + tu/mau + "");
            if(tu==0 || mau==0 || userAverage1 == 0) {
                predicted = 0;
            }
            
//            predicted = userAverage1 + tu/mau;
//            System.out.print("Tu = " + tu + "\n");
//            System.out.print("User1 "+ userAverage1 +"\n");
//            System.out.print("mau" + mau +"\n");
//            System.out.print("Predicted = "+predicted +"\n");
            
        } catch ( Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
        return predicted;
    }
    
    public void generatePearsonPredictedRating(String dataset)
    {
        int MaxNumber = 6100;
        int MinNumber = 4000;
        Utils util = new Utils();
//        int maxItem = 1591;
//        int maxUser = 462;
        int maxItem = 0;
        int maxUser = 0;
        double itemUser[][] = new double[MaxNumber][MinNumber];
        double itemUserTest[][] = new double[MaxNumber][MinNumber];
        int numUserItemBase[] = new int[2];
        int numUserItemTest[] = new int[2];
        //File base = new File("C:/Users/hsb/Documents/NetBeansProjects/Pearson/src/data/u1.base");
        File base = new File("src/data/" + dataset + ".test");
        //itemUser = readFile(itemUser, 943, 1682, base);
        util.readFile(itemUser, numUserItemBase, base);
        //File test = new File("C:/Users/hsb/Documents/NetBeansProjects/Pearson/src/data/u1.test");
        File test = new File("src/data/" + dataset + ".test");
        //itemUserTest = readFile(itemUserTest, 462, 1591, test);
        numUserItemTest[0] = 0;
        numUserItemTest[1] = 0;
        util.readFile(itemUserTest, numUserItemTest, test);
        maxUser = numUserItemTest[0];
        maxItem = numUserItemTest[1];
        //System.out.print("maxuser -" +maxUser + " max Item " +maxItem + "\n");
        double predict = 0;
        int result = 0;
        try {
            File myfile = new File(dataset + ".ps");
            FileWriter output = new FileWriter(myfile);
            for(int i = 0; i < maxUser; i++)
            {
              for(int j = 0; j < maxItem; j++)
              {
                  if(itemUserTest[i][j] != 0)
                  {
                      predict = computeWeightedSum(i, j, itemUser, numUserItemBase[0], numUserItemBase[1]);
                      
                      String s = (i+1) + "\t" + (j+1) + "\t" + Math.round(predict) + "\t" + "0";
                      BufferedWriter buffer=new BufferedWriter(output);
                      buffer.write(s);
                      buffer.write("\n");
                      buffer.flush();
                      //System.out.print((i+1) + "-" + (j+1) + "-" + predict + "\n");
                      //dem++;
//                      if(dem==10)
//                            break;
                  }
                   //System.out.print(i + " " + j );
              }
              //break;
              //dem = 0;
              
              
            }
            //System.out.print(dem);
        } catch ( Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
    }
    
    public void computeMSEPearson(String dataset)
    {
        int MaxNumber  = 2000;
        int maxItem = 0;
        int maxUser = 0;
        Utils util = new Utils();
        double[][] test = new double[MaxNumber][MaxNumber];
        double[][] result = new double[MaxNumber][MaxNumber];
        int numUserItem[] = new int[2];
        
        //File testArr = new File("/media/hsb/Study/UIT/Khoa luan/Pearson/src/data/u1.test");
        File testArr = new File("src/data/" + dataset + ".test");
        File resultArr = new File(dataset + ".ps");
        //test = readFile(test, maxUser, maxItem, testArr);
        //result = readFile(result, maxUser, maxItem, resultArr);
        util.readFile(test, numUserItem, testArr);
        maxUser = numUserItem[0];
        maxItem = numUserItem[1];
        util.readFile(result, numUserItem, resultArr);
        
        double MSE = 0;
        double RMSE  = 0;
        float MAE = 0;
        int dem = 0;
        for(int i = 0; i < maxUser; i++)
        {
          for(int j = 0; j < maxItem; j++)
          {
                if(test[i][j] !=0) {
                //MSE += Double.parseDouble(test[i][j] - result[i][j]+"")*Double.parseDouble((test[i][j] - result[i][j])+"");
                MSE += (test[i][j] - result[i][j])*(test[i][j] - result[i][j]);
                MAE += Math.abs(test[i][j] - result[i][j]);
                dem++;
                //System.out.print("Test= " + test[i][j] +" Result = " + result[i][j] + " MSE = " +  MSE + " Dem = " + dem + "\n");
                //System.out.print(test[i][j] - result[i][j] + "\n");
                
                //System.out.print(Double.parseDouble(test[i][j] - result[i][j]+"") + "\n");
                }
                
                //break;
          }
          //break;
        }
        MSE = MSE /dem;
        RMSE = Math.sqrt(MSE);
        MAE = MAE / dem;
        System.out.print("MSE == " + MSE + "\n");
        System.out.print("RMSE == " + RMSE + "\n");
        System.out.print("MAE == " + MAE + "\n");
    }
    
    public void computeMSECosine(String dataset)
    {
        int MaxNumber  = 2000;
        int maxItem = 0;
        int maxUser = 0;
        Utils util = new Utils();
        double[][] test = new double[MaxNumber][MaxNumber];
        double[][] result = new double[MaxNumber][MaxNumber];
        int numUserItem[] = new int[2];
        
        //File testArr = new File("/media/hsb/Study/UIT/Khoa luan/Pearson/src/data/u1.test");
        File testArr = new File("src/data/" + dataset + ".test");
        File resultArr = new File(dataset + ".cosine");
        //test = readFile(test, maxUser, maxItem, testArr);
        //result = readFile(result, maxUser, maxItem, resultArr);
        util.readFile(test, numUserItem, testArr);
        maxUser = numUserItem[0];
        maxItem = numUserItem[1];
        util.readFile(result, numUserItem, resultArr);
        
        double MSE = 0;
        double RMSE  = 0;
        float MAE = 0;
        int dem = 0;
        for(int i = 0; i < maxUser; i++)
        {
          for(int j = 0; j < maxItem; j++)
          {
                if(test[i][j] !=0) {
                //MSE += Double.parseDouble(test[i][j] - result[i][j]+"")*Double.parseDouble((test[i][j] - result[i][j])+"");
                MSE += (test[i][j] - result[i][j])*(test[i][j] - result[i][j]);
                MAE += Math.abs(test[i][j] - result[i][j]);
                dem++;
                //System.out.print("Test= " + test[i][j] +" Result = " + result[i][j] + " MSE = " +  MSE + " Dem = " + dem + "\n");
                //System.out.print(test[i][j] - result[i][j] + "\n");
                
                //System.out.print(Double.parseDouble(test[i][j] - result[i][j]+"") + "\n");
                }
                
                //break;
          }
          //break;
        }
        MSE = MSE /dem;
        RMSE = Math.sqrt(MSE);
        MAE = MAE / dem;
        System.out.print("MSE == " + MSE + "\n");
        System.out.print("RMSE == " + RMSE + "\n");
        System.out.print("MAE == " + MAE + "\n");
    }
    
    public double compute(int item1, int item2, float itemUser[][], int maxUser, int maxItem)
    {
        double result = 0;
        for(int i=item1; i< maxItem; i++)
          {
              for(int j=0; j< maxUser; j++)
              {
                  //if()
                  result += itemUser[item1][j] * itemUser[item2][j];
              }
              break;
          }
        return result;
    }
    
       
    
    public double computeSimpleWeighted(int user, int item, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
        double predicted = 0;
        double tu = 0;
        double mau = 0;
        double cosine  = 0;

          for(int j = 0; j < maxItem; j++)
          {
              if(itemUser[user][j] != 0 ) 
              {
                  //cosine = computeCosine(item, j, itemUser, maxUser, maxItem);
                  cosine = topN.computeConditionalProbability(itemUser, item, j, maxUser, maxItem);
                  tu += itemUser[user][j] * cosine;
                  mau += Math.abs(cosine);
              }
          }
             
        predicted = tu/mau;
        if(tu == 0 || mau == 0)
            predicted = 0;
//           System.out.print("Tu = " + tu + "\n");
//           System.out.print("mau" + mau +"\n");
        return predicted;
    }
    
    public double computeSimpleWeightedUser(int user, int item, double itemUser[][], int maxUser, int maxItem) throws FileNotFoundException
    {
        double predicted = 0;
        double tu = 0;
        double mau = 0;
        double cosine  = 0;

          for(int i = 0; i < maxUser; i++)
          {
              if(itemUser[i][item] != 0 ) 
              {
                  cosine = computeCosineUser(user, i, itemUser, maxUser, maxItem);
                  tu += itemUser[i][item] * cosine;
                  mau += Math.abs(cosine);
              }
          }
             
        predicted = tu/mau;
        if(tu == 0 || mau == 0) {
            predicted = 0;
        }
//           System.out.print("Tu = " + tu + "\n");
//           System.out.print("mau" + mau +"\n");
        return predicted;
    }
    
    public void generateCosinePredictedRating(String dataset)
    {
        int MaxNumber = 2000;
        Utils util = new Utils();
        int maxItem = 0;
        int maxUser = 0;
        double itemUser[][] = new double[MaxNumber][MaxNumber];
        double itemUserTest[][] = new double[MaxNumber][MaxNumber];
        int numUserItemBase[] = new int[2];
        int numUserItemTest[] = new int[2];
        //File base = new File("/media/hsb/Study/UIT/Khoa luan/Pearson/src/data/u1.base");
        File base = new File("src/data/" + dataset + ".base");
        //itemUser = readFile(itemUser, 943, 1682, base);
        util.readFile(itemUser, numUserItemBase, base);
        //File test = new File("/media/hsb/Study/UIT/Khoa luan/Pearson/src/data/u1.test");
        File test = new File("src/data/" + dataset + ".test");
        //itemUserTest = readFile(itemUserTest, 462, 1591, test);
        numUserItemTest[0] = 0;
        numUserItemTest[1] = 0;
        util.readFile(itemUserTest, numUserItemTest, test);
        maxUser = numUserItemTest[0];
        maxItem = numUserItemTest[1];
        //normalizeRating(itemUser, maxUser, maxItem);
        //System.out.print("maxuser -" +maxUser + " max Item " +maxItem + "\n");
        double predict = 0;
        int dem = 0;
        try {
            File myfile = new File(dataset + ".cosine");
            FileWriter output = new FileWriter(myfile);
            for(int i = 0; i < maxUser; i++)
            {
              for(int j = 0; j < maxItem; j++)
              {
                  if(itemUserTest[i][j] != 0)
                  {
                      predict = computeSimpleWeighted(i, j, itemUser, numUserItemBase[0], numUserItemBase[1]);
                      //predict = computeSimpleWeightedUser(i, j, itemUser, numUserItemBase[0], numUserItemBase[1]);
                      String s = (i+1) + "\t" + (j+1) + "\t" + Math.round(predict) + "\t" + "0";
                      BufferedWriter buffer=new BufferedWriter(output);
                      buffer.write(s);
                      buffer.write("\n");
                      buffer.flush();
                      //System.out.print((i+1) + "-" + (j+1) + "-" + predict + "\n");
                      //dem++;
//                      if(dem==10)
//                            break;
                  }
                   //System.out.print(i + " " + j );
              }
              //break;
              //dem = 0;
              
              
            }
            //System.out.print(dem);
        } catch ( Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
    }
    
}