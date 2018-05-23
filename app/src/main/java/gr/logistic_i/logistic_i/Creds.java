package gr.logistic_i.logistic_i;

public class Creds {

    String name = new String();
    String pass = new String();
    String curl = new String();

    public Creds(String name, String pass, String curl) {
        this.name = name;
        this.pass = pass;
        this.curl = curl;
    }

    public void initLogin(){
        //todo make login dialog with db
    }
}
