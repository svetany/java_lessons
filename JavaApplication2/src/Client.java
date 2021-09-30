
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;
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
    static Random rnd = new Random();
    
    public static void main(String[] args) {
        try {
            String site = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso?op=NumberToWords";
            
            int rndNum = rnd.nextInt(1000);

            //создадим сообщение
            String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "  <soap:Body>\n"
                    + "    <NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">\n"
                    + "      <ubiNum>unsignedLong</ubiNum>\n"
                    + "    </NumberToWords>\n"
                    + "  </soap:Body>\n"
                    + "</soap:Envelope>";
            message = message.replace("unsignedLong", String.valueOf(rndNum));
            
            log.info("\r\nREQUEST:\r\n" + message); //выведем в log4j, что отослали 

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(site);
            
            httpPost.setHeader("Host", "www.dataaccess.com");
            httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
            HttpEntity stringEntity = new StringEntity(message);
            httpPost.setEntity(stringEntity);
            
            long startRequest = System.currentTimeMillis();
            CloseableHttpResponse response = httpClient.execute(httpPost);
            long endResponse = System.currentTimeMillis();
            
            HttpEntity entity = response.getEntity();
            String resp = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            boolean check_response = resp.contains("<m:NumberToWordsResult>");
            long time_response = endResponse - startRequest;
            
            log.info("Успешный ответ: " + check_response + "\r\nВремя ответа:  " + time_response + "\r\nОтвет: " + resp);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        
    }
    
}
