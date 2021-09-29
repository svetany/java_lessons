import java.net.*;
import java.io.*;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.Logger;

public class Client {
static Logger log = Logger.getLogger(Client.class.getName());
   public static void main(String [] args) throws IOException, InterruptedException {
    Date timeA = new Date(); // фиксируем время начала кода
    long startFullCode = timeA.getTime();
    String site = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso?op=NumberToWords";
    HttpURLConnection connection = null;
    URL url;
    OutputStream OUT = null;
    InputStreamReader IN = null;
    BufferedReader buf = null;
    StringBuilder SB = new StringBuilder();
    int rndNum = (int)(Math.random()*10000);
    

    try{
        //создадим сообщение
        StringBuilder SOAP = new StringBuilder();
        SOAP.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        SOAP.append("<soap:Envelope ");
        SOAP.append("xmlns:soap=");
        SOAP.append("\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n");
        SOAP.append("<soap:Body>\r\n");
        SOAP.append("<NumberToWords ");
        SOAP.append("xmlns=");
        SOAP.append("\"http://www.dataaccess.com/webservicesserver/\">\r\n");
        SOAP.append("<ubiNum>");
        SOAP.append(rndNum).toString();
        SOAP.append("</ubiNum>\r\n");
        SOAP.append("</NumberToWords>\r\n");
        SOAP.append("</soap:Body>\r\n");
        SOAP.append("</soap:Envelope>");
        
        byte[] post_message = SOAP.toString().getBytes("UTF-8"); //и превратим его в поток байтов
        //System.out.println("\r\n"+ "RESPONSE:\r\n" + SOAP.toString()); //выведем, что отослали
        log.debug("RESPONSE:\r\n" + SOAP.toString()); //выведем, что отослали в log4j
        
        url = new URL(site); //создаем url, помещаем адрес в url
        connection = (HttpURLConnection)url.openConnection(); //создаем http connection
        connection.setRequestMethod("POST"); //метод отправки
        connection.setDoOutput(true); //позволяет отсылать сообщения
        connection.setDoInput(true); //позволяет принимать сообщения
        connection.setUseCaches(false); //чтобы не закрыавть потом потоки, и чтобы данные не кешировались
        //заголовки:
        connection.addRequestProperty("Host", "www.dataaccess.com");
        connection.addRequestProperty("Content-Type", "text/xml; charset=utf-8");
        connection.addRequestProperty("Content-Length", "length");
        //таймауты
        connection.setConnectTimeout(250);
        connection.setReadTimeout(250);
        
        connection.connect(); //установили коннект
        
//можно отсылать сообщения, работаем с аутпутом:
        try {
           OUT = connection.getOutputStream();
           OUT.write(post_message); //передать поток байтов
       } catch (IOException e) {
           System.err.println(e.getMessage());
       }
        Date timeC = new Date();
        long startRequest = timeC.getTime(); // фиксируем время после отправки
    // смотрим ответ сообщения, работаем с инпут потоком:
       if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){ // проверяем, соответсвует ли полученный ответ значению http_OK (который равен 200)
          IN = new InputStreamReader(connection.getInputStream());
          buf = new BufferedReader(IN);
          String line;
          Date timeD = new Date(); //// фиксируем время ответа
        long endRequest = timeD.getTime();
           //System.out.println("\r\n"+"Request Time, ms: " + (endRequest - startRequest));
           log.info("Request Time, ms: " + (endRequest - startRequest));
          while((line = buf.readLine()) != null){
              SB.append(line); // добавляем сюда сообщение
             
          }        
       }else{
           //System.err.println("ERROR: " + connection.getResponseCode());
           log.error("ERROR: " + connection.getResponseCode());
       }
      
     //System.out.println("\r\n" + "REQUEST: \r\n" + SB); //выводим полученную после отправки строку, т.е. ответ soap формата    
     log.debug("REQUEST: " + SB); //выводим полученную после отправки строку, т.е. ответ soap формата в log4j   
    
    } catch (MalformedURLException e){
     } catch (IOException e) {
         System.err.println(e.getMessage());
     }     
     Date timeB = new Date(); // фиксируем время всего кода
    long endFullCode = timeB.getTime();
     //System.out.println("\r\n" + "Full Code Time, ms: " + (endFullCode - startFullCode));
     log.info("Full Code Time, ms: " + (endFullCode - startFullCode));
     
   }
   
    }
   
