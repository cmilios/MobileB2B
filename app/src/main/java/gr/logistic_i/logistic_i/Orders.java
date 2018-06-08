package gr.logistic_i.logistic_i;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orders implements Serializable {

    private ArrayList<String> arpar;
    private ArrayList<String> type;
    private ArrayList<Date> date;
    private ArrayList<String> qty;
    private ArrayList<String> company;

    public void setArpar(ArrayList<String> arpar) {
        this.arpar = arpar;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public void setDate(ArrayList<Date> date) {
        this.date = date;
    }

    public void setQty(ArrayList<String> qty) {
        this.qty = qty;
    }

    public void setCompany(ArrayList<String> company) {
        this.company = company;
    }

    public ArrayList<String> getArpar() {

        return arpar;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public ArrayList<Date> getDate() {
        return date;
    }

    public ArrayList<String> getQty() {
        return qty;
    }

    public ArrayList<String> getCompany() {
        return company;
    }
}
