package gr.logistic_i.logistic_i;

import android.content.Context;
import android.os.AsyncTask;

public class GetSqlDataTask extends AsyncTask<Object,Void,Void> {

    private Context context;
    private Boolean stateToken = false;


    public GetSqlDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Object... obj) {
        if(obj[1] == "SqlData"){
            //todo make network request

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


    public void makeSqlReq(String url, String jsonData){


    }
}
