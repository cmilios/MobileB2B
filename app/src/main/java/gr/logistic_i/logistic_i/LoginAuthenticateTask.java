package gr.logistic_i.logistic_i;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginAuthenticateTask extends AsyncTask<Object,Void,Void> {

    private StringBuilder responseItem = new StringBuilder();
    private String service = new String();
    private String url = new String();
    private String clientID = new String();
    private Boolean authState = false;
    private Context context;
    LoginActivity activity = new LoginActivity();
    MainMenuActivity mActivity;


    //constructor for changing activities and sharing data on post execute

    public LoginAuthenticateTask(Context context, LoginActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public LoginAuthenticateTask(Context context, MainMenuActivity mActivity){
        this.context = context;
        this.mActivity = mActivity;
    }



    @Override
    protected Void doInBackground(Object... obj) {
        url = (String)obj[0];
        service = (String)obj[1];
        if (obj[1] == "login"){
            String authToken = makeLogin((String)obj[0], obj[2].toString());
            responseItem = new StringBuilder();
            Boolean authState = makeAuthenticate((String)obj[0], authToken);
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (authState){
            Intent i = new Intent(context, MainMenuActivity.class);
            i.putExtra("url", url);
            i.putExtra("clID", clientID);

            context.startActivity(i);






        }
        else{
            Toast.makeText(context, "Wrong Credentials!", Toast.LENGTH_LONG).show();
            activity.setAllToNormal();
        }
    }

    public String makeLogin(String url, String jsonData){

        service = "login";

        //Build url to connect to
        StringBuilder finalURL = new StringBuilder("https://");
        finalURL.append(url);
        finalURL.append("/s1services");

        HttpURLConnection con = null;


        // Make a connection with the API
        URL furl = null;
        try {
            furl = new URL(finalURL.toString());
            con = (HttpURLConnection) furl.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Accept", "application/json");
            con.addRequestProperty("Content-Type", "application/json; charset=windows-1253");
            con.setDoInput(true);

            //Request Parameters you want to send
            String urlParameters = jsonData.toString();

            // Send post request
            con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));

            // Read the first line
            String line = reader.readLine();

            // Append the first line to the builder
            responseItem.append(line);



            // Read the remaining stream until we are done
            while ((line = reader.readLine()) != null) {
                responseItem.append(line);
            }

            //makes sure not to create any exception on deserialization
            if(responseItem.toString() != null){
                UserData us = new UserData();
                us = us.desirializeJsonStr(responseItem.toString());
                clientID = us.getClientID();
                String authUser = us.serObj();
                return  authUser;

            }
            else return null;





        } catch (Exception e) {
            return null;
        }
        finally {
            con.disconnect();
        }


    }

    public Boolean makeAuthenticate(String url, String authToken){

        service = "authenticate";

        //Build url to connect to
        StringBuilder finalURL = new StringBuilder("https://");
        finalURL.append(url);
        finalURL.append("/s1services");

        HttpURLConnection con = null;


        // Make a connection with the API
        URL furl = null;
        try {
            furl = new URL(finalURL.toString());
            con = (HttpURLConnection) furl.openConnection();
            con.setRequestMethod("POST");
            con.addRequestProperty("Accept", "application/json");
            con.addRequestProperty("Content-Type", "application/json; charset=windows-1253");
            con.setDoInput(true);

            //Request Parameters you want to send
            String urlParameters = authToken;

            // Send post request
            con.setDoOutput(true);// Should be part of code only for .Net web-services else no need for PHP
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // Begin streaming the JSON
            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "windows-1253"));

            // Read the first line
            String line = reader.readLine();

            // Append the first line to the builder
            responseItem.append(line);





            // Read the remaining stream until we are done
            while ((line = reader.readLine()) != null) {
                responseItem.append(line);
            }

            JSONObject obj = new JSONObject(responseItem.toString());
            authState = obj.getBoolean("success");
            return authState;

        } catch (Exception e) {
            return null;
        }
        finally {
            con.disconnect();
        }




    }

    public ArrayList<String> getValues(){
        ArrayList<String> lista = new ArrayList<>();
        lista.add(url);
        lista.add(clientID);
        return lista;
    }

}
