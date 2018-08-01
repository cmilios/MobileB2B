package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Mtrl implements Parcelable {



    private String mtrl;
    private String imgURL;
    private String code;
    private String name;
    private String sales;
    private String manufacturer;
    private String price;
    private Drawable image;
    private String correspondingBase;
    private ArrayList<String> unitList;
    private String mu21;
    private String mu41;
    private String mu21mode;
    private String mu41mode;
    private DecimalFormat qtyformat = new DecimalFormat("#.##");




    Mtrl(String mtrl, String imgURL, String code, String name, String sales, String manufacturer,
         String correspondingBase, ArrayList<String> unitList, String mu21, String mu41, String mu21mode, String mu41mode) {
        this.mtrl = mtrl;
        this.imgURL = imgURL;
        this.code = code;
        this.name = name;
        this.sales = sales;
        this.manufacturer = manufacturer;
        this.correspondingBase = correspondingBase;
        this.unitList = unitList;
        this.mu21 = mu21;
        this.mu41 = mu41;
        this.mu21mode = mu21mode;
        this.mu41mode = mu41mode;


    }


    public void loadImage(){
        GsonWorker gsonWorker = new GsonWorker(correspondingBase);
        if (imgURL!=null){
            image = gsonWorker.getImage(correspondingBase, imgURL);

        }


    }

    public String getMu21() {
        return mu21;
    }


    public String getMu41() {
        return mu41;
    }

    public String getQuantityToFirstMtrUnit(int index, String qty, String wayOfTransformation){
        String qtyToFirstMtrUnit;

            if (index == 0) {
                return qty;
            } else if (index == 1) {
                if (wayOfTransformation.equals("/")) {
                    if (mu21 != null && !mu21.equals("0")) {
                        qtyToFirstMtrUnit = String.valueOf(qtyformat.format(Double.parseDouble(qty) * Double.parseDouble(mu21)));
                        return qtyToFirstMtrUnit;
                    }
                }
                else{
                    if (mu21 != null && !mu21.equals("0")) {
                        qtyToFirstMtrUnit = String.valueOf(qtyformat.format(Double.parseDouble(qty) / Double.parseDouble(mu21)));
                        return qtyToFirstMtrUnit;
                    }

                }
            }
            else {
                if (mu41 != null && !mu41.equals("0")) {
                    qtyToFirstMtrUnit = String.valueOf(qtyformat.format(Double.parseDouble(qty) * Double.parseDouble(mu41)));
                    return qtyToFirstMtrUnit;
                }
            }
        return  null;
    }




    public String getImgURL() {
        return imgURL;
    }

    public Drawable getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getMtrl() {
        return mtrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMu21mode() {
        return mu21mode;
    }


    public String getMu41mode() {
        return mu41mode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }



    private Mtrl(Parcel in) {
        mtrl = in.readString();
        imgURL = in.readString();
        code = in.readString();
        name = in.readString();
        sales = in.readString();
        manufacturer = in.readString();
        price = in.readString();
        correspondingBase = in.readString();
        if (in.readByte() == 0x01) {
            unitList = new ArrayList<>();
            in.readList(unitList, String.class.getClassLoader());
        } else {
            unitList = null;
        }
        mu21 = in.readString();
        mu41 = in.readString();
        mu21mode = in.readString();
        mu41mode = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getUnitList() {
        return unitList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mtrl);
        dest.writeString(imgURL);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(sales);
        dest.writeString(manufacturer);
        dest.writeString(price);
        dest.writeString(correspondingBase);
        if (unitList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(unitList);
        }
        dest.writeString(mu21);
        dest.writeString(mu41);
        dest.writeString(mu21mode);
        dest.writeString(mu41mode);

    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Mtrl> CREATOR = new Parcelable.Creator<Mtrl>() {
        @Override
        public Mtrl createFromParcel(Parcel in) {
            return new Mtrl(in);
        }

        @Override
        public Mtrl[] newArray(int size) {
            return new Mtrl[size];
        }
    };
}