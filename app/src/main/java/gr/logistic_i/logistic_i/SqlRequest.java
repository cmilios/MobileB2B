package gr.logistic_i.logistic_i;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SqlRequest {

    String service;
    String clientID;
    String appID;
    String sqlName;
    String fromDate;
    String toDate;
    String refid;

    public SqlRequest(String service, String clientID, String appID, String sqlName, String fromDate, String toDate, String refid) {
        this.service = service;
        this.clientID = clientID;
        this.appID = appID;
        this.sqlName = sqlName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.refid = refid;
    }

    public String serSqlData(){
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("service", service);
            jsonData.put("clientID", clientID);
            jsonData.put("appId", appID);
            jsonData.put( "SqlName", sqlName);
            jsonData.put("param1", fromDate);
            jsonData.put("param2", toDate);
            jsonData.put("param3", /*refid*/ "2243");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    public ArrayList<Order> parseResponse(JSONObject jsonRes){
        ArrayList<Order> orderlist = new ArrayList<>();
        try {
            int size = jsonRes.getInt("totalcount");
            if(size>0){

                JSONArray resarray = jsonRes.getJSONArray("rows");
                for(int i=0; i<resarray.length();i++){
                    orderlist.add(new Order(resarray.getJSONObject(i).getString("code"),
                            resarray.getJSONObject(i).getString("FINDOC"),
                            resarray.getJSONObject(i).getString("fincode"),
                            resarray.getJSONObject(i).getString("TRNDATE"),
                            resarray.getJSONObject(i).getString("NAME"),
                            resarray.getJSONObject(i).getString("SUMAMNT")
                    ));
                }



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderlist;


    }
}
