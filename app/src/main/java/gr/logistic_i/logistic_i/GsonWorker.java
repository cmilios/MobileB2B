package gr.logistic_i.logistic_i;

import android.net.sip.SipSession;

import com.google.gson.JsonObject;

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

    String url = new String();
    String jsonData = new String();
    Boolean state = false;
    String loginClID = new String();
    String authenticateClID = new String();
    String refID = new String();
    Boolean authenticationFlag = false;

    public GsonWorker(String url) {
        this.url = url;
    }

    public void makeLogin(Creds creds){

        jsonData = creds.serObjLogin();
        String loginResponse = getJSON(url, jsonData);
        try {
            JSONObject resObj = new JSONObject(loginResponse);
                if (state){
                    loginClID = resObj.getString("clientID");
                    UserData us = new UserData();

                    us = us.desirializeJsonStr(resObj.toString());
                    makeAuthenticate(us);
                }




        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void makeAuthenticate(UserData us){

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

    public String getJSON(String furl, String jsonData) {
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
            conn.addRequestProperty("Content-Type", "application/json; charset=windows-1253");
            conn.setDoInput(true);

            String urlParameters = jsonData.toString();

            conn.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

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


    public String getLoginClID() {
        return loginClID;
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
}
