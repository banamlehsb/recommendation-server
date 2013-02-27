/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pearson;


import agorithms.*;
import agorithms.TopNItemBased;
import agorithms.Utils;
import java.io.File;
/**
 *
 * @author hsb
 */
public class Exc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //name of movielens data set. dataset = {u1,u2,u3,u4,u5};
            String dataset = "u1";
            int maxNumber = 2000;
            int maxItem = 0;
            int maxUser = 0;
            int numUserItem[] = new int[2];
            double itemUser[][] = new double[maxNumber][maxNumber];
            Utils util = new Utils();
            
            TopNItemBased topN = new TopNItemBased();
            Pearson p = new Pearson();
            double eval[] = new double[2];
            
//            p.generatePearsonPredictedRating(dataset);
//            p.computeMSEPearson(dataset);
//            p.generateCosinePredictedRating(dataset);
//            p.computeMSECosine(dataset);
//            
////            
//              topNUser.generateTopN(dataset);
//            topNUser.computeHitRate(dataset);
            //generate top N file
//            topN.generateTopN(dataset);
////            //compute hit rate
//            topN.computeHitRate(dataset, eval);
              
            
            double HR = 0;
            double ARHR  = 0;
            double M[][] = new double[maxItem][maxItem];
            int numData = 6;
            for(int i=1; i<numData; i++)
            {
                dataset = "u"+i;
                System.out.print("--------dataset-------"+dataset +"\n");
                //topN.generateTopN(dataset);
                topN.computeHitRate(dataset, eval);
                HR += eval[0];
                ARHR += eval[1];
            }
            System.out.print("Avarage HR = " + HR/(numData-1) +"\n");
            System.out.print("Avarage ARHR = " + ARHR/(numData-1) +"\n");
            
//            
//            File base = new File("src/java/data/u1.base");
//            File training = new File("u8.base");
//            File testing = new File("u8.test");
//            itemUser = util.readFile(itemUser, numUserItem, base);
//            maxUser = numUserItem[0];
//            maxItem = numUserItem[1];
//            int listItem[] = new int[10];
//            int user = 10;
//            int N = 10;
//            double M[][] = new double[maxItem][maxItem];
//            //listItem = topN.topN(itemUser, M, user-1, maxUser, maxItem, N);
//            for(int x=0; x<10;x++)
//            {
//                System.out.print("User: " + user + "\n");
//                System.out.print("      Item: " + listItem[x] + "\n");
//            }
//            float trainingSet[][] = new float[maxNumber][maxNumber];
//            float testSet[][] = new float[maxNumber][maxNumber];
//            util.splitDataset(trainingSet, testSet, base);
//            
//            util.writeFile(trainingSet, training, maxUser, maxItem);
//            
//            util.writeFile(testSet, testing, maxUser, maxItem);
          
//            //double pearson = p.computeUserBasedPearson(1-1, 2-1, itemUser, maxUser, maxItem);
//            System.out.print(p.computeUserBasedPearson(1-1, 2-1, itemUser, maxUser, maxItem));
//            topNUser.topN(itemUser, 1-1, maxUser, maxItem);
//            //p.normalizeRating(itemUser, maxUser, maxItem);
//            p.generateCosinePredictedRating();
//            p.computeMSE();
//            System.out.print(" pearson = " + pearson);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
