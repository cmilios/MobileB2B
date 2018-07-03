package gr.logistic_i.logistic_i;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MtrlReq {


    String service;
    String clientID;
    String appID;
    String sqlName;
    String refid;

    public MtrlReq(String service, String clientID, String appID, String sqlName, String refid) {
        this.service = service;
        this.clientID = clientID;
        this.appID = appID;
        this.sqlName = sqlName;
        this.refid = refid;
    }

    public String serMtrlOrders(){
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("service", service);
            jsonData.put("clientID", clientID);
            jsonData.put("appId", appID);
            jsonData.put( "SqlName", sqlName);
            jsonData.put("param1", /*refid*/ "2243");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    public ArrayList<Mtrl> parseResponse(JSONObject jsonRes){
        ArrayList<Mtrl> mtrList = new ArrayList<>();
        try {
            int size = jsonRes.getInt("totalcount");
            if(size>0){

                JSONArray resarray = jsonRes.getJSONArray("rows");
                for(int i=0; i<resarray.length();i++){
                    mtrList.add(new Mtrl(resarray.getJSONObject(i).getString("img"),
                            resarray.getJSONObject(i).getString("mtrcode"),
                            resarray.getJSONObject(i).getString("mtrname"),
                            resarray.getJSONObject(i).getString("sales"),
                            resarray.getJSONObject(i).getString("manufacturer")
                    ));
                }



            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mtrList;


    }




}
