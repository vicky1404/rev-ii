package cl.bicevida.revelaciones.ejb.cross;

import java.io.*;
import java.security.MessageDigest;

public class MD5CheckSum {

    /**
     * @param filename
     * @return
     * @throws Exception
     */
    public static byte[] createChecksum(String filename) throws Exception {
       InputStream fis =  new FileInputStream(filename);
       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;
       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);
       fis.close();
       return complete.digest();
   }

    /**
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] createChecksum(InputStream inputStream) throws Exception {       
       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;
       do{
           numRead = inputStream.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       }while (numRead != -1);
       inputStream.close();
       return complete.digest();
    } 


    /**
     * @param filename
     * @return
     * @throws Exception
     */
    public static String getMD5Checksum(String filename) throws Exception {
       byte[] b = createChecksum(filename);
       String result = "";
       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
   }

    /**
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String getMD5Checksum(InputStream inputStream) throws Exception {
       byte[] b = createChecksum(inputStream);
       String result = "";
       for (int i=0; i < b.length; i++) {
           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
       }
       return result;
    } 

   public static void main(String args[]) {
       try {
           System.out.println(getMD5Checksum("C:\\informe_revelaciones_12-06-2012_15-50.docx"));           
       }
       catch (Exception e) {
           e.printStackTrace();
       }
   }
}

