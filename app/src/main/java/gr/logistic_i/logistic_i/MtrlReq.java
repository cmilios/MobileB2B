package gr.logistic_i.logistic_i;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MtrlReq {


    private String service;
    private String clientID;
    private String appID;
    private String sqlName;
    private String param1;
    private long param2;
    private String url;

    MtrlReq(String service, String clientID, String appID, String sqlName, String param1, long param2, String url) {
        this.service = service;
        this.clientID = clientID;
        this.appID = appID;
        this.sqlName = sqlName;
        this.param1 = param1;
        this.param2 = param2;
        this.url = url;
    }

    public String serMtrlOrders(){
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("service", service);
            jsonData.put("clientID", clientID);
            jsonData.put("appId", appID);
            jsonData.put( "SqlName", sqlName);
            jsonData.put("param1", param1 /*"2243"*/);
            jsonData.put("param2", param2);

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
                    HashMap<Integer, String> unitsList = new HashMap<>();
                    ArrayList<String> units = new ArrayList<>();
                    units.add(resarray.getJSONObject(i).getString("unit1"));
                    unitsList.put(resarray.getJSONObject(i).getInt("code1"), resarray.getJSONObject(i).getString("unit1"));

                    String mtrl;
                    if (resarray.getJSONObject(i).has("mtrl")){
                        mtrl = resarray.getJSONObject(i).getString("mtrl");
                    }
                    else{
                        mtrl = null;
                    }

                    if (resarray.getJSONObject(i).has("unit2")){
                        units.add(resarray.getJSONObject(i).getString("unit2"));
                        unitsList.put(resarray.getJSONObject(i).getInt("code2"), resarray.getJSONObject(i).getString("unit2"));                    }
                    else {
                        units.add(null);
                    }
                    if (resarray.getJSONObject(i).has("unit4")){
                        units.add(resarray.getJSONObject(i).getString("unit4"));
                        unitsList.put(resarray.getJSONObject(i).getInt("code4"), resarray.getJSONObject(i).getString("unit4"));                    }
                    else{
                        units.add(null);
                    }
                    String img;
                    if (resarray.getJSONObject(i).has("img")){
                        img = resarray.getJSONObject(i).getString("img");
                    }
                    else{
                        img = null;
                    }
                    String mtrcode;
                    if (resarray.getJSONObject(i).has("mtrcode")){
                        mtrcode = resarray.getJSONObject(i).getString("mtrcode");
                    }
                    else{
                        mtrcode = null;
                    }
                    String mtrname;
                    if(resarray.getJSONObject(i).has("mtrname")){
                        mtrname = resarray.getJSONObject(i).getString("mtrname");
                    }
                    else{
                        mtrname = null;
                    }
                    String sales;
                    if (resarray.getJSONObject(i).has("sales")){
                        sales = resarray.getJSONObject(i).getString("sales");
                    }
                    else{
                        sales = null;
                    }
                    String manufacturer;
                    if (resarray.getJSONObject(i).has("manufacturer")){
                        manufacturer = resarray.getJSONObject(i).getString("manufacturer");
                    }else{
                        manufacturer = null;
                    }
                    String mu21;
                    if ((resarray.getJSONObject(i).has("c21"))){
                        mu21 = resarray.getJSONObject(i).getString("c21");
                    }
                    else{
                        mu21 = null;
                    }
                    String mu41;
                    if (resarray.getJSONObject(i).has("c41")){
                        mu41 = resarray.getJSONObject(i).getString("c41");
                    }
                    else{
                        mu41 = null;
                    }
                    String mu21mode;
                    if (resarray.getJSONObject(i).has("c21mode")){
                        mu21mode = resarray.getJSONObject(i).getString("c21mode");
                    }
                    else{
                        mu21mode = null;
                    }
                    String mu41mode;
                    if (resarray.getJSONObject(i).has("c41mode")){
                        mu41mode = resarray.getJSONObject(i).getString("c41mode");
                    }
                    else{
                        mu41mode = null;
                    }

                    mtrList.add(new Mtrl(mtrl ,img, mtrcode, mtrname, sales, manufacturer, url,units, unitsList, mu21, mu41, mu21mode, mu41mode));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mtrList;
    }




}
