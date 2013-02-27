/*
 * ConvertBetweenUnicodeNCR.java
 *
 * Created on August 15, 2009, 11:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.mail;

/**
 *
 * @author Le Thanh Huy
 */
public class ConvertBetweenUnicodeNCR {
    
    /**
     * Creates a new instance of ConvertBetweenUnicodeNCR
     */
    public ConvertBetweenUnicodeNCR() {
    }
    
    public static String NCR2Unicode(String str) {
        String ostr = new String();
        int i1=0;
        int i2=0;
        
        while(i2<str.length()) {
            i1 = str.indexOf("&#",i2);
            if (i1 == -1 ) {
                ostr += str.substring(i2, str.length());
                break ;
            }
            ostr += str.substring(i2, i1);
            i2 = str.indexOf(";", i1);
            if (i2 == -1 ) {
                ostr += str.substring(i1, str.length());
                break ;
            }
            
            String tok = str.substring(i1+2, i2);
            
            try {
                int radix = 10 ;
                if (tok.trim().charAt(0) == 'x') {
                    radix = 16 ;
                    tok = tok.substring(1,tok.length());
                }
                
                ostr += (char) Integer.parseInt(tok, radix);
            } catch (NumberFormatException exp) {
                ostr += '?' ;
            }
            
            i2++ ;
        }
        return ostr ;
    }
    
    public static String unicode2NCR(String str) {
        String ostr = new String();
        
        for(int i=0; i<str.length(); i++) {
            char ch = str.charAt(i);
            
            if ((ch >= 0x0020) && (ch <= 0x007e))
                ostr += ch;
            else {
                ostr += "&#" ;
                String num = Integer.toString(str.charAt(i));
                
                ostr += (num + ";");
            }
        }
        return (ostr);
    }    
}