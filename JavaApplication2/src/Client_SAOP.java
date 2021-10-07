
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

public class Client_SAOP {

    static Logger logger = Logger.getLogger(Client_SAOP.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        String site = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso?op=NumberToWords";
        Random rnd = new Random();
        try {
            //создадим сообщение
            String message = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
                    + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n"
                    + "  <soap:Body>\n"
                    + "    <NumberToWords xmlns=\"http://www.dataaccess.com/webservicesserver/\">\n"
                    + "      <ubiNum>unsignedLong</ubiNum>\n"
                    + "    </NumberToWords>\n"
                    + "  </soap:Body>\n"
                    + "</soap:Envelope>";
            message = message.replace("unsignedLong", Integer.toString(rnd.nextInt(1000000000)));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(site);
            logger.info("message:\r\n" + message);
            httpPost.setHeader("Host", "www.dataaccess.com");
            httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
            HttpEntity stringEntity = new StringEntity(message);
            httpPost.setEntity(stringEntity);
            long startRequest = System.currentTimeMillis();
            CloseableHttpResponse http_response = httpClient.execute(httpPost);
            long endResponse = System.currentTimeMillis();
            HttpEntity entity = http_response.getEntity();
            String resp = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            boolean check_response = resp.contains("<m:NumberToWordsResult>");
            long time_response = endResponse - startRequest;
            String res = "\r\nУспешный ответ: " + check_response + "\r\nВремя ответа, ms:  " + time_response + "\r\nОтвет:\r\n" + resp;
            Pattern pattern = Pattern.compile("<m:NumberToWordsResult>(.+) </m:NumberToWordsResult>");
            Matcher matcher = pattern.matcher(resp);
            while (matcher.find()) {
                System.out.println(matcher.group(1).trim());
            }
            logger.info("Result: " + res);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
