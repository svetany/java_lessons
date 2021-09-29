import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.apache.log4j.Logger;

import org.apache.hc.client5.http.impl.sync.CloseableHttpClient;
import org.apache.hc.client5.http.impl.sync.HttpClients;
import org.apache.hc.client5.http.methods.CloseableHttpResponse;
import org.apache.hc.client5.http.methods.HttpPost;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.entity.ContentType;
import org.apache.hc.core5.http.entity.EntityUtils;
import org.apache.hc.core5.http.entity.StringEntity;

public class Client {
static Logger log = Logger.getLogger(Client.class.getName());
   public static void main(String [] args) throws IOException, InterruptedException, ParseException {
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
        
        //byte[] post_message = SOAP.toString().getBytes("UTF-8"); //и превратим его в поток байтов
        log.debug("REQUEST:\r\n" + SOAP.toString()); //выведем в log4j, что отослали 
              
        url = new URL(site); //создаем url, помещаем адрес в url
        
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(site);
        httpPost.setHeader("Host", "www.dataaccess.com");
        httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
        //httpPost.setHeader("Content-Length", "length");
        HttpEntity stringEntity = new StringEntity(SOAP.toString());
        httpPost.setEntity(stringEntity);
        Date timeC = new Date();
        long startRequest = timeC.getTime(); // фиксируем время после отправки
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Date timeD = new Date(); //// фиксируем время ответа
        HttpEntity entity = response.getEntity();
        String resp = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        long endResponse = timeD.getTime();
        log.info("\r\n RESPONSE:"+"\r\n ------------------------------------------------\r\n" + resp + "\r\n------------------------------------------------");
        log.info("Response time, ms: " + (endResponse - startRequest));
       
        /*
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
           log.info("Time, ms: " + (endRequest - startRequest));
          while((line = buf.readLine()) != null){
              SB.append(line); // добавляем сюда сообщение
             
          }        
       }else{
           //System.err.println("ERROR: " + connection.getResponseCode());
           log.error("ERROR: " + connection.getResponseCode());
       }
      
     //System.out.println("\r\n" + "REQUEST: \r\n" + SB); //выводим полученную после отправки строку, т.е. ответ soap формата    
     log.debug("RESPONSE: " + SB); //выводим полученную после отправки строку, т.е. ответ soap формата в log4j   
    */
    } catch (MalformedURLException e){
     } catch (IOException e) {
         log.fatal(e.getMessage());

     }     
  
    
     
   }
   
    }
   