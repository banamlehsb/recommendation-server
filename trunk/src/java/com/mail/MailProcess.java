/*
 * MailProcess.java
 *
 * Created on August 14, 2009, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 *
 * @author Le Thanh Huy
 */
public class MailProcess {
    
    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final String SMTP_PORT = "465";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    // gmail cua ban vd: noname@gmail.com thi chi ghi noname la OK
    private static final String SMTP_AUTH_USER = "handsomeboyhi2007@gmail.com";
    // password cho mail
    private static final String SMTP_AUTH_PWD  = "09910122";
    
    
    public void sendSSLMail(String from, String dsNguoiNhan[], String tieuDe,
            String noiDung) throws MessagingException {
        
        // Khoi tao gia tri cac thuoc tinh
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");
        
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            }
        });
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);
        
        InternetAddress addressFrom = new InternetAddress(from);
        InternetAddress[] addressTo = new InternetAddress[dsNguoiNhan.length];
        for (int i = 0; i < dsNguoiNhan.length; i++) {
            addressTo[i] = new InternetAddress(dsNguoiNhan[i]);
        }
        
        // Gan cac thuoc tinh vao trong message
        msg.setFrom(addressFrom);
        msg.setRecipients(Message.RecipientType.TO, addressTo);        
        msg.setSubject(ConvertBetweenUnicodeNCR.NCR2Unicode(tieuDe),"UTF-8");        
        msg.setText(noiDung);
        msg.setSentDate(new Date());
        msg.setHeader("Content-Type", "text/html; charset=UTF-8");
        //msg.setHeader("Content-Transfer-Encoding", "quoted-printable");
        msg.saveChanges();
        Transport.send(msg);
        System.out.println("Gui message thanh cong");
    }
// Pthuc main run ktra tai cho
    public static void main(String[] args) {
        String str = "banamlehsb@gmail.com,    mail2@gmail.com, mail3@gmail.com,";
        str = str.replaceAll(" ","");
        String[] arr = str.split(",");
        
        for (int i = 0; i < arr.length; i++) {
            System.out.println("***"+arr[i]);
        }
        /*
       String[] to = {"lthanhhuy@cit.ctu.edu.vn", "thanhhuy27@gmail.com"};
        try {
            new MailProcess().sendSSLMail("thanhhuy27@yahoo.com", to, "Tieu de", "Noi dung");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
         */
    }

    public void sendUserInfo(String from, String dsNguoiNhan[],
            String str) throws MessagingException {

        // Khoi tao gia tri cac thuoc tinh
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.port", SMTP_PORT);
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
            }
        });
        session.setDebug(true);
        MimeMessage msg = new MimeMessage(session);

        InternetAddress addressFrom = new InternetAddress(from);
        InternetAddress[] addressTo = new InternetAddress[dsNguoiNhan.length];
        for (int i = 0; i < dsNguoiNhan.length; i++) {
            addressTo[i] = new InternetAddress(dsNguoiNhan[i]);
        }

        // Gan cac thuoc tinh vao trong message
        msg.setFrom(addressFrom);
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        msg.setText(str);

        msg.setSentDate(new Date());
        msg.setHeader("Content-Type", "text/html; charset=UTF-8");
        //msg.setHeader("Content-Transfer-Encoding", "quoted-printable");
        msg.saveChanges();
        Transport.send(msg);
        System.out.println("Gui message thanh cong");
    }
}
