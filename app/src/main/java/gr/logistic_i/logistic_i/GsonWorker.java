package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GsonWorker {

    private String url;
    private Boolean state = false;
    private String authenticateClID = new String();
    private String refID = new String();
    private Boolean authenticationFlag = false;
    private ArrayList<Order> sqlResponse = new ArrayList<>();
    private ArrayList<MtrLine> mtrLines = new ArrayList<>();
    private ArrayList<Mtrl> mtrList = new ArrayList<>();

    public GsonWorker(String url) {
        this.url = url;
    }

    public void makeLogin(Creds creds){

        String jsonData = creds.serObjLogin();
        String loginResponse = getJSON(url, jsonData);
        try {
            JSONObject resObj = new JSONObject(loginResponse);
                if (state){
                    String loginClID = resObj.getString("clientID");
                    UserData us = new UserData();

                    us = us.desirializeJsonStr(resObj.toString());
                    makeAuthenticate(us);
                }




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void makeAuthenticate(UserData us){

        String authUser = us.serObj();
        refID = us.getRefId();
        String authenticateResponse = getJSON(url, authUser);
        try {
            JSONObject resObj = new JSONObject(authenticateResponse);
                if(state){
                    authenticateClID = resObj.getString("clientID");
                    authenticationFlag = true;
                }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getSqlOrders(SqlRequest sqlRequest){
        String sqlOrders = getJSON(url, sqlRequest.serSqlData());
        try {
            JSONObject resObj = new JSONObject(sqlOrders);
            if(state){
                sqlResponse = sqlRequest.parseResponse(resObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getMtrLines(MtrLinesReq mtrLinesReq){
        String lines = getJSON(url, mtrLinesReq.serObj());
        try {
            JSONObject resObj = new JSONObject(lines);
            if (state){
                mtrLines = mtrLinesReq.parseResponse(resObj);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getFOI(MtrlReq mtrlReq){
        String lines = getJSON(url, mtrlReq.serMtrlOrders());
        try{
            JSONObject resObj = new JSONObject(lines);
            if(state){
                mtrList = mtrlReq.parseResponse(resObj);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String calculatePrice(JSONObject jsonObject){

        String lines = getJSON(url,jsonObject.toString());
            if(state){
                return lines;
            }
        return null;

    }


    public String getWayOfTransformation(JSONObject jsonObject){
       String lines = getJSON(url,jsonObject.toString());
       if (state){
           return lines;
       }
       return null;

    }

    public String setData(JSONObject jsonObject){
        String lines = getJSON(url, jsonObject.toString());

        if (state){
            return lines;
        }
        return  null;

    }



    private String getJSON(String furl, String jsonData) {
        state = false;
        HttpURLConnection conn = null;

        // Create a StringBuilder for the final URL
        StringBuilder finalURL = new StringBuilder("https://");
        // Append API URL
        finalURL.append(furl);
        // Append endpoint
        finalURL.append("/s1services");


        // Create a StringBuilder to store the JSON string
        StringBuilder result = new StringBuilder();
        try {
            // Make a connection with the API
            URL url = new URL(finalURL.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.addRequestProperty("Accept", "application/json");
            conn.addRequestProperty("Content-Type", "application/json; charset="+ "windows-1253");
            conn.setDoInput(true);


            conn.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(jsonData);
            wr.flush();
            wr.close();

            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));

            // Read the first line
            String line = reader.readLine();

            // Append the first line to the builder
            result.append(line);

            // Read the remaining stream until we are done
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            if (result.toString()!=null) {
                JSONObject res = new JSONObject(result.toString());
                state = res.getBoolean("success");
            }


        } catch (Exception e) {
            // Usually indicates a lack of Internet connection
            return null;
        } finally {
            // Close connection
            if (conn != null)
                conn.disconnect();
        }

        // Return the JSON string
        return result.toString();
    }

    public Drawable getImage(String furl, String imgURL){
        state = false;
        HttpURLConnection conn = null;

        // Create a StringBuilder for the final URL
        StringBuilder finalURL = new StringBuilder("https://");
        // Append API URL
        finalURL.append(furl);
        // Append endpoint
        finalURL.append("/s1services");
        finalURL.append("/?filename=");
        finalURL.append(imgURL);


        // Create a StringBuilder to store the JSON string
        try {
            // Make a connection with the API
            URL url = new URL(finalURL.toString());
            conn = (HttpURLConnection) url.openConnection();


            // Begin streaming the JSON
            InputStream in = (InputStream) url.getContent();
            return Drawable.createFromStream(in, "src name");


        } catch (Exception e) {
            // Usually indicates a lack of Internet connection
            return null;
        } finally {
            // Close connection
            if (conn != null)
                conn.disconnect();
        }


    }


    public String getAuthenticateClID() {
        return authenticateClID;
    }

    public String getRefID() {
        return refID;
    }

    public Boolean getAuthenticationFlag() {
        return authenticationFlag;
    }

    public ArrayList<Order> getSqlResponse() {
        return sqlResponse;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<MtrLine> getMtrLines() {
        return mtrLines;
    }

    public ArrayList<Mtrl> getMtrList() {
        return mtrList;
    }
}
