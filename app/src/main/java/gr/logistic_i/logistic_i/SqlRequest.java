package gr.logistic_i.logistic_i;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SqlRequest {

    private String service;
    private String clientID;
    private String appID;
    private String sqlName;
    private String fromDate;
    private String toDate;
    private String refid;

    SqlRequest(String service, String clientID, String appID, String sqlName, String fromDate, String toDate, String refid) {
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
            jsonData.put("param3", refid );
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
                            resarray.getJSONObject(i).getString("SUMAMNT"),
                            resarray.getJSONObject(i).getString("state")
                    ));
                }



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderlist;


    }
}
