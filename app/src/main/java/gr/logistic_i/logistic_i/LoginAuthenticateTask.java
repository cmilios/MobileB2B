package gr.logistic_i.logistic_i;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAuthenticateTask extends AsyncTask<Object,Void,Void> {

    private StringBuilder responseItem = new StringBuilder();
    private String service = new String();
    private Boolean authState = false;
    private Context context;

    //constructor for changing activities on post execute
    public LoginAuthenticateTask(Context context) {
        this.context = context.getApplicationContext();
    }


    @Override
    protected Void doInBackground(Object... obj) {
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
            context.startActivity(new Intent(context, MainMenuActivity.class));
        }
        else{
            //todo needs improvement doesn't work
            Toast.makeText(context, "Wrong Credentials!", Toast.LENGTH_LONG);
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

            UserData us = new UserData();
            us = us.desirializeJsonStr(responseItem.toString());
            String authUser = us.serObj();



            return  authUser;
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

}
