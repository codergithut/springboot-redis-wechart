package wechart.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/21
 * @description
 */
@Service
public class HttpClientServer {

    static final String OK = "\"message\":\"成功\"";

    static final String STAY = "\"client_status\":\"in\"";

//    @Autowired
//    HttpClient httpClient;

    static CloseableHttpClient httpClient = HttpClients.createDefault();


    public  static String  getConnect(String url,String username,String password) throws IOException {

        final String AUTHENKEY = "Authorization";

        final String BASICKEY = "Basic ";

        HttpResponse response = null;


        HttpGet httpGet = new HttpGet(url);


        if(username != null && password != null) {

            Base64 token = new Base64();

            String authenticationEncoding = token.encodeAsString(new String(username + ":" + password).getBytes());

            httpGet.setHeader(AUTHENKEY, BASICKEY + authenticationEncoding);

        }


        String responseContent = "";

        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpEntity entity = response.getEntity();

        responseContent = EntityUtils.toString(entity, "UTF-8");

        return responseContent;
    }

    public static String getContent(String url) throws IOException {

        return getConnect(url,null , null);

    }


    public static String httpPostWithJSON(String url, JSONObject jsonObject) throws IOException {

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpClient client = HttpClients.createDefault();
        String respContent = null;
        StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        //        表单方式
        //        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        //        pairList.add(new BasicNameValuePair("name", "admin"));
        //        pairList.add(new BasicNameValuePair("pass", "123456"));
        //        httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));

        try {
            HttpResponse resp = client.execute(httpPost);
            if(resp.getStatusLine().getStatusCode() == 200) {
                HttpEntity he = resp.getEntity();
                respContent = EntityUtils.toString(he,"UTF-8");
            }
        } catch (Exception e) {

        } finally{
            client.close();
        }



        return respContent;
    }


    public static boolean addFence(String desc, String name, String valid_time, String center, String radius) throws IOException {

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("name", name);
        jsonParam.put("center", center);
        jsonParam.put("enable", "true");
        jsonParam.put("radius", radius);
        jsonParam.put("valid_time", valid_time);


        jsonParam.put("repeat", "Mon,Tues,Wed,Thur,Fri,Sat,Sun");
        jsonParam.put("time", "00:00,11:59;13:00,20:59");
        jsonParam.put("desc", desc);
        jsonParam.put("alert_condition", "enter;leave");

        String s = httpPostWithJSON("http://restapi.amap.com/v4/geofence/meta?key=d60f35ea1c1bb8e5f2c472fbf9278405", jsonParam);

        if(s.contains(OK)) {
            return true;
        }
        return false;
    }

    public static boolean IsInCompany(String diu, String location) throws IOException {
        String url = "http://restapi.amap.com/v4/geofence/status?key=d60f35ea1c1bb8e5f2c472fbf9278405&diu=" + diu + "&locations=" + location + "," + (new Date().getTime()/1000);

        String contents = getContent(url);

        if(contents.contains(STAY)) {
            return true;
        }

        return false;

    }

    public static void main(String[] args) throws Exception {

        //System.out.println(addFence("测试围栏描述","测试围栏名称", "2019-05-19", "115.672126,38.817129", "100" ));

        System.out.println(IsInCompany("358568072860640", "115.672126,38.817129"));

        //System.out.println(getContent("http://restapi.amap.com/v4/geofence/status?key=d60f35ea1c1bb8e5f2c472fbf9278405&diu=358568072860640&locations=115.672126,38.817129,1484816232"));

    }
}
