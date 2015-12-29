package daimaroo.bash;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.*;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Mostafa on 15/12/27.
 */
public class JsonSender {

    /**Get A File <Be Careful> And Convert This File To JSOn
     * Object With It's Name , Absolute Path ,And Content Of This File
     * @param file
     * @return
     * @throws IOException
     */
    public static JSONObject jsonFileObject(File file) throws IOException {
        String fst=FileHandler.convertFileToString(file);
        JSONObject jfile=new JSONObject();
        jfile.put("requesttype","upfile");

        jfile.put("file-name",file.getName());
        jfile.put("file-content",fst);
        System.out.println(jfile.toString());
        return jfile;
    }

    /**Get An ID and A Pass And Convert Them To The JSON Object
     *
     * @param ID
     * @param pass
     * @return
     */
    public static JSONObject jsonLoginObject(String ID,String pass){
        JSONObject jlogin=new JSONObject();
        jlogin.put("id",ID);
        jlogin.put("requesttype","login");
        jlogin.put("pass",pass);
        return jlogin;
    }



    /**Get A HahMap Object And Convert This Instance Of HashMap To JSON Object
     * and Return This Object
     * be CArefull That Your Keys of HashMap Be An Instance Of String
     * @param hashMap
     * @return
     */
    public JSONObject jsonHashMapObject(HashMap<String,String> hashMap){
        JSONObject jhash=new JSONObject();
        for(String obj:hashMap.keySet()){
            jhash.put(obj,hashMap.get(obj));
        }
        return jhash;
    }


    /**Get FirstName LastName And ... then convert Them To An Object With
     * Key Value <fname> <lname> And ... Then Return This Object
      * @param firstname
     * @param lastname
     * @param username
     * @param pass
     * @param website
     * @param email
     * @return
     */
    public static JSONObject jsonSignUpObject(String firstname,String lastname,String username,String pass,String website,String email){
        JSONObject jup=new JSONObject();
        jup.put("fname",firstname);
        jup.put("requesttype","signup");
        jup.put("lname",lastname);
        jup.put("username",username);
        jup.put("website",website);
        jup.put("email",email);
        return jup;
    }

    /**Second Type Of Converting Some SignUp InformationTo JSON Object
     *
     * @param firstname
     * @param lastname
     * @param username
     * @param pass
     * @return
     */
    public static JSONObject jsonSignUpObject(String firstname,String lastname,String username,String pass){
        JSONObject jup=new JSONObject();
        jup.put("requesttype","signup");
        jup.put("fname",firstname);
        jup.put("lname",lastname);
        jup.put("username",username);
        return jup;
    }

    /**This Method Send An JSON Object To Specific URL
     * Enter Your JSON Object And URL Then Fire It!
     * @param URL
     * @param jo
     * @return
     * @throws IOException
     */
    public static String sendJsonTo(String URL,JSONObject jo) throws IOException {
        CloseableHttpClient httpClient= HttpClientBuilder.create().build();
        HttpEntity entity=null;
        String responseFromServer=null;
        try {

            HttpPost request = new HttpPost(URL);
            StringEntity params = new StringEntity(jo.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response=httpClient.execute(request);
            entity = response.getEntity();
            responseFromServer= EntityUtils.toString(entity);
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseFromServer;
            // handle exception here
        } finally {
            httpClient.close();
        }
        System.out.println("Json Was Send To The Server");
        return responseFromServer;
    }

}
