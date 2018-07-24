package gr.logistic_i.logistic_i;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MtrLinesReq {
    private  String service;
    private  String clientID;
    private  String appID;
    private  String sqlName;
    private  String findoc;

    MtrLinesReq(String service, String clientID, String appID, String sqlName, String findoc) {
        this.service = service;
        this.clientID = clientID;
        this.appID = appID;
        this.sqlName = sqlName;
        this.findoc = findoc;
    }

    public String serObj() {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("service", service);
            jsonData.put("clientID", clientID);
            jsonData.put("appId", appID);
            jsonData.put("SqlName", sqlName);
            jsonData.put("param1", findoc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonData.toString();


    }

    public ArrayList<MtrLine> parseResponse(JSONObject jsonRes) {

        ArrayList<MtrLine> lines = new ArrayList<>();
        try {
            int size = jsonRes.getInt("totalcount");
            if (size > 0) {

                JSONArray resarray = jsonRes.getJSONArray("rows");
                for (int i = 0; i < resarray.length(); i++) {
                    lines.add(new MtrLine(
                            resarray.getJSONObject(i).getString("code"),
                            resarray.getJSONObject(i).getString("code"),
                            resarray.getJSONObject(i).getString("descr"),
                            resarray.getJSONObject(i).getString("qty"),
                            resarray.getJSONObject(i).getString("qty1"),
                            resarray.getJSONObject(i).getString("prc"),
                            resarray.getJSONObject(i).getString("disc"),
                            resarray.getJSONObject(i).getString("kathaxia"),
                            resarray.getJSONObject(i).getString("axiafpa"),
                            resarray.getJSONObject(i).getInt("mtrunit"),
                            null
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lines;
    }
}



