package gr.logistic_i.logistic_i;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Creds {

    String name = new String();
    String pass = new String();
    String curl = new String();

    public Creds(String name, String pass, String curl) {
        this.name = name;
        this.pass = pass;
        this.curl = curl;
    }

    public void initLogin() throws MalformedURLException {
        //todo make login dialog with db
//        URL url = new URL(curl);
//        HttpURLConnection httpurl = new HttpURLConnection(url) {
//            @Override
//            public void disconnect() {
//
//            }
//
//            @Override
//            public boolean usingProxy() {
//                return false;
//            }
//
//            @Override
//            public void connect() throws IOException {
//
//            }
//        };
    }
}
