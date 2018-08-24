package gr.logistic_i.logistic_i;


import android.arch.persistence.room.Room;
import android.content.res.Configuration;
import android.os.StrictMode;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

public class App extends android.app.Application {

    private static final String DATABASE_NAME = "APPDB";
    private AppDB appDB;


    //Basic log in variables used in every network request
    private String url;
    private String clientID;
    private String appID;
    private String refID;

    //New order formed through the use of Application
    private ArrayList<MtrLine> mtrLines = new ArrayList<>();

    //Key that defines if findoc exists or order is new
    private String key;

    //Last Intent Activity name
    private String cameFrom;

    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        Fresco.initialize(this);
        TypefaceProvider.registerDefaultIconSets();



        appDB = Room.databaseBuilder(getApplicationContext(),
                AppDB.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public ArrayList<MtrLine> getMtrLines() {
        return mtrLines;
    }

    public void addLineToList(MtrLine line){
        mtrLines.add(line);
    }

    public void setMtrLines(ArrayList<MtrLine> mtrLines) {
        this.mtrLines = mtrLines;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(String cameFrom) {
        this.cameFrom = cameFrom;
    }

    public AppDB getAppDB() {
        return appDB;
    }

    public void setMtrldb(AppDB appDB) {
        this.appDB = appDB;
    }
}
