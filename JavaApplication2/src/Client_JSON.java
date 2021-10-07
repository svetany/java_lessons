
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
import org.json.simple.JSONObject;

public class Client_JSON {

    static Logger logger = Logger.getLogger(Client_SAOP.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
        String site = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso/NumberToWords/JSON";
        Random rnd = new Random();
        try {
            //создадим сообщение
            JSONObject jo_req = new JSONObject();
            jo_req.put("ubiNum", String.valueOf(rnd.nextInt(1000000000)));

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(site);
            logger.info("message: " + jo_req);
            httpPost.setHeader("Host", "www.dataaccess.com");
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            HttpEntity stringEntity = new StringEntity(jo_req.toString());
            httpPost.setEntity(stringEntity);
            long startRequest = System.currentTimeMillis();
            CloseableHttpResponse http_response = httpClient.execute(httpPost);
            long endResponse = System.currentTimeMillis();
            HttpEntity entity = http_response.getEntity();
            String resp = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            boolean check_response = resp.matches("\"(.+)\"");
            long time_response = endResponse - startRequest;

            Pattern pattern = Pattern.compile("\"(.+) \"");
            Matcher matcher = pattern.matcher(resp);
            while (matcher.find()) {

                logger.info("Result:" + "\r\nУспешный ответ: " + check_response + "\r\nВремя ответа, ms:  " + time_response + "\r\nОтвет: " + matcher.group(1).trim());
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
