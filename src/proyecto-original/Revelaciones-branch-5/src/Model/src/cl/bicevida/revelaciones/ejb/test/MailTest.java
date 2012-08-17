package cl.bicevida.revelaciones.ejb.test;

import cl.bicevida.revelaciones.eeff.CargadorEeffVO;

import cl.bicevida.revelaciones.ejb.entity.EstadoFinanciero;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class MailTest
{
    public  void getNewStr(String str){
        str = "HOLA2";
    }
    
    public  void getNewList(List<String> list){
        /*list.add("c");
        list.add("d");*/
        list = new ArrayList<String>();
        list.add("1");
        list.add("2");
    }
    
    public  void getNewList2(CargadorEeffVO cargador){
        /*list.add("c");
        list.add("d");*/
        cargador.setEeffBorradoList(new ArrayList<EstadoFinanciero>());
        /*list = new ArrayList<String>();
        list.add("1");
        list.add("2");*/
    }
    
    public  void getNewList3(CargadorEeffVO cargador){
        cargador = new CargadorEeffVO();
        cargador.setEeffBorradoList(new ArrayList<EstadoFinanciero>());

    }
    
   public static void main(String [] args)
   {
        MailTest mt = new MailTest();
        
        CargadorEeffVO cargador = null;;
       
       mt.getNewList3(cargador);
       
        String str = "HOLA1";
        mt.getNewStr(str);
        
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
       
        mt.getNewList(list);
        
        System.out.println(str);
        
        for(String s : list){
            System.out.println(s);    
        }
       
      /*
      // Recipient's email ID needs to be mentioned.
      String to = "mauricio.barra@bicevida.cl";

      // Sender's email ID needs to be mentioned
      String from = "julio.benavente@bicevida.cl";

      // Assuming you are sending email from localhost
      String host = "correo.bicevida.cl";

      // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("Urgente");

         // Now set the actual message
         message.setText("Julio, favor ven a mi oficina.");

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }*/
   }
}
