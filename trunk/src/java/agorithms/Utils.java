/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agorithms;

import parsers.MovielensVoteParser;
import data.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random; 
/**
 *
 * @author hsb
 */
public class Utils {
    
    //read movielens data files
    public int readFile(double itemUser[][], int numUserItem[], File file)
    {
        int MaxNumber = 2000;
        String line;
        Rating r = new Rating();
        int NumberRead = 0;
        int check  = 1;
        try {
          BufferedReader in = new BufferedReader( new FileReader( file ) );
          while ( ( ( line = in.readLine() ) != null ) && ( NumberRead < MaxNumber ) ) {
            check = MovielensVoteParser.parseMovielens( r, line, "\t");
            if(check == -1)
            {
                in.close();
                break;
            }
            if(r.getmUser() > numUserItem[0])
                numUserItem[0] = r.getmUser();
            if(r.getmItem() > numUserItem[1])
                numUserItem[1] = r.getmItem();
            if(r.getmVote() != 0)
                itemUser[r.getmUser()-1][r.getmItem()-1] = r.getmVote();
            else itemUser[r.getmUser()-1][r.getmItem()-1] = 0;
          }
          in.close();
        } catch ( IOException ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
        //System.out.print("max user" + numUserItem[0] + "--" + numUserItem[1] + "\n");
        
        return check;
    }
    
    public double[][] readFile(double itemUser[][], File file)
    {
        int MaxNumber = 2000;
        String line;
        Rating r = new Rating();
        int NumberRead = 0;
        String delimiter = "\t";
        int i = 0;
        int j = 0;
        double val = 0;
        try {
          BufferedReader in = new BufferedReader( new FileReader( file ) );
          while ( ( ( line = in.readLine() ) != null ) && ( NumberRead < MaxNumber ) ) {
            //MovielensVoteParser.parseMovielens( r, line, "\t");
            
            String[] dat = line.split(delimiter);
            //if(r.getmVote() != 0)
            {
                i = Integer.parseInt(dat[0]);
                j = Integer.parseInt(dat[1]);
                val = Double.parseDouble(dat[2]);
                itemUser[i-1][j-1] = val;
            }
          }
          
        } catch ( IOException ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
        //System.out.print("max user" + numUserItem[0] + "--" + numUserItem[1] + "\n");
        
        return itemUser;
    }
    
    public void verifyFile(double itemUser[][])
    {
        
    }
    
    public void writeFile(double itemUser[][], File myfile, int maxUser, int maxItem) throws FileNotFoundException
    {
        try {
            FileWriter output = new FileWriter(myfile);
            for(int i = 0; i < maxUser; i++)
            {
              for(int j = 0; j < maxItem; j++)
              {
                      if(itemUser[i][j] !=0 ) {
                          String s = (i+1) + "\t" + (j+1) + "\t" + itemUser[i][j] + "\t" + "0";
                          BufferedWriter buffer=new BufferedWriter(output);
                          buffer.write(s);
                          buffer.write("\n");
                          buffer.flush();
                      }
              }
            }
        } catch ( Exception ioe ) {
          ioe.printStackTrace();
          // nothing else you can do? Just abort for now!
          System.err.println( "[Error] could parse the file" );
        }
    }
    
    public void splitDataset(double trainingSet[][], double testSet[][], File myfile) throws FileNotFoundException
    {
        int MaxNumber = 2000;
        int maxItem = 0;
        int maxUser = 0;
        int numUserItem[] = new int[2];
        double itemUser[][] = new double[MaxNumber][MaxNumber];
        readFile(itemUser, numUserItem, myfile);
        maxUser = numUserItem[0];
        maxItem = numUserItem[1];
        Random rd = new Random();
        Random r = new Random();
        int j = 0;
        int totalCount = 0;
        int colCount = 0;
        int temp[] = new int[maxItem];
        int dem = 0;
        int l = 0;
            for(int i = 0; i < maxUser; i++)
            {
                //while(totalCount<80000) 
        {
                ArrayList<Integer> numbers = new ArrayList<Integer>();
                for(int k = 0; k < maxItem; k++) 
                {
                    if(itemUser[i][k] !=0 ) {
                        dem++;
                        numbers.add(k);
                    }
                    
                }
                 Collections.shuffle(numbers);
                //System.out.print((dem*(80/100)) + "\n");
                //while(colCount <  (int)((dem*80)/100) )
                for(int k = 0; k < ((dem*80)/100); k++)  
                {
                    //j = rd.nextInt(maxItem);
                    j = numbers.get(k);
                    //System.out.print("j: "+ j +"\n");
                    if(itemUser[i][j] !=0 ) {
                        trainingSet[i][j] = itemUser[i][j];
                        //totalCount++;
                        colCount++;
                        //System.out.print("Dem " + colCount + "   "+ i + " vs "+ j + " = " + itemUser[i][j] +"\n");
                    }
                }
                System.out.print("User: "+i + " dem: "+dem + " count: "+colCount +"\n");
                dem  = 0;
                colCount = 0;
                //break;
        }
            }
        
        for(int i = 0; i < maxUser; i++)
        {
            for(int k = 0; k < maxItem; k++)
            {
                if(itemUser[i][k] != trainingSet[i][k] && itemUser[i][k]!=0)
                {
                    testSet[i][k] = itemUser[i][k];
                }
            }
        }
    }
    
    // get the kth largest values in M matrix
    public double getKthLargest(double[][] itemUser, int k, int j, int maxItem)
    {

        int pos = 0;
        double temp = 32767;
        while (pos < k) {
            double max  = -32767;
                
            for(int i = 0; i < maxItem; i++)
            {
              if(itemUser[i][j] < temp && itemUser[i][j] > max) {
                    max = itemUser[i][j];
                }

            }
            if(max != 0) {
                if(pos == 0 || max != -32767)
                {
                    pos++;
                    temp = max;
                }
            } else 
            {
                break;
            }
        }
        
        if(pos < k) {
            temp = 0;
        }
        return temp;
    }
    
    // get the kth largest values in X matrix
    public double getKthLargest(double[] itemUser, int k, int maxItem)
    {
        int pos = 0;
        double temp = 32767;
        while (pos < k) {
            double max  = -32767;
            for(int i = 0; i < maxItem; i++)
                {
                  
                      if(itemUser[i] < temp && itemUser[i] > max)  max = itemUser[i];
                      
                }
            if(max != 0) {
                if(pos == 0 || max != -32767)
                {
                    pos++;
                    temp = max;
                }
            } else 
            {
                break;
            }
           
        }
        
        if(pos < k) {
            temp = 0;
        }
        return temp;
    }
    
    //multiply matrix M and matrix U
    public double[] multiplyMatrix(double M[][], double U[], int maxItem)
    {
        double X[] = new double[maxItem];
        for(int i=0; i < maxItem; i++)
        {
            {
                    X[i]=0;
                    for(int k = 0 ; k < maxItem; k++) {
                        X[i] += M[i][k] * U[k];
                }
            }
        }
        
        return X;
    }
    
    public int getKthLargestPosition(double[] itemUser, int k, int maxUser, int maxItem)
    {
        
        int pos = 0;
        double temp = 32767;
        int result  = 0;
        int i = 0;
        while (pos < k) {
            double max  = -32767;
            for(i = 0; i < maxItem; i++)
                {
                      if(itemUser[i] < temp && itemUser[i] > max)  
                      {
                          max = itemUser[i];
                          result = i;
                      }
                  
                }
            
            if(max != 0) {
                if(pos == 0 || max != -32767)
                {
                    pos++;
                    temp = max;
                }
            } else 
            {
                break;
            }
        }
        return result;
    }
}