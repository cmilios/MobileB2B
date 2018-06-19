package gr.logistic_i.logistic_i;

import android.content.Context;
import android.os.AsyncTask;

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

public class GetSqlDataTask extends AsyncTask<Object,Void,Void> {

    Context context;
    private Boolean stateToken = false;
    JSONObject json  = new JSONObject();
    JSONObject jsonResponse = new JSONObject();
    StringBuilder responseItem = new StringBuilder();


    public GetSqlDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        if(obj[1] == "SqlData"){
            if (obj[4] == "Orders"){
                makeJson(obj[1].toString(), obj[2].toString(), obj[3].toString(), obj[4].toString(),obj[5].toString(), obj[6].toString());
                makeSqlReq(obj[0].toString(), json);


            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (stateToken){
            // todo return response to obj to bind on adapter

        }
    }


    public void makeSqlReq(String url, JSONObject jsonData){
        HttpURLConnection con = null;


        // Make a connection with the API
        URL furl = null;
        try {
            furl = new URL(url);
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

            //makes parceable json the response request
            jsonResponse = new JSONObject(responseItem.toString());







        } catch (Exception e) {
            return;
        }
        finally {
            con.disconnect();
        }





    }

    public void makeJson(String service, String clientID, String appID, String sqlName, String fromDate, String toDate){

        try {
            json.put("service", service);
            json.put("clientID", clientID);
            json.put("appId", appID);
            json.put( "SqlName", sqlName);
            json.put("param1", fromDate);
            json.put("param2", toDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void parseResponse(JSONObject jsonRes){
        try {
            int size = jsonRes.getInt("totalcount");
            if(size>0){
                //todo parse json as gmaps project
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
