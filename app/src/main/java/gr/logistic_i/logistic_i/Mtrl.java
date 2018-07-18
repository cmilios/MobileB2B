package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Mtrl implements Parcelable {

    private String imgURL;
    private String code;
    private String name;
    private String sales;
    private String manufacturer;
    private String price;
    private Drawable image;
    private String mtrunit;
    private String correspondingBase;
    private ArrayList<String> unitList;
    private String mu21;
    private String mu31;



    public String getMtrunit() {
        return mtrunit;
    }

    public void setMtrunit(String mtrunit) {
        this.mtrunit = mtrunit;
    }

    public Mtrl(String imgURL, String code, String name, String sales, String manufacturer, String mtrunit,
                String correspondingBase, ArrayList<String> unitList, String mu21, String mu31) {
        this.imgURL = imgURL;
        this.code = code;
        this.name = name;
        this.sales = sales;
        this.manufacturer = manufacturer;
        this.correspondingBase = correspondingBase;
        this.mtrunit = mtrunit;
        this.unitList = unitList;
        this.mu21 = mu21;
        this.mu31 = mu31;


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

    public void setMu21(String mu21) {
        this.mu21 = mu21;
    }

    public String getMu31() {
        return mu31;
    }

    public void setMu31(String mu31) {
        this.mu31 = mu31;
    }

    public String getQuantityToFirstMtrUnit(int index, String qty){
        String qtyToFirstMtrUnit;

        if (index==0){
            return  qty;
        }
        else if(index==1){
            if (mu21!=null) {
                qtyToFirstMtrUnit = String.valueOf(Double.parseDouble(qty) * Double.parseDouble(mu21));
                return qtyToFirstMtrUnit;
            }
        }
        else {
            if(mu31!= null) {
                qtyToFirstMtrUnit = String.valueOf(Double.parseDouble(qty) * Double.parseDouble(mu31));
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

    public void setName(String name) {
        this.name = name;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



    protected Mtrl(Parcel in) {
        imgURL = in.readString();
        code = in.readString();
        name = in.readString();
        sales = in.readString();
        manufacturer = in.readString();
        price = in.readString();
        mtrunit = in.readString();
        correspondingBase = in.readString();
        if (in.readByte() == 0x01) {
            unitList = new ArrayList<String>();
            in.readList(unitList, String.class.getClassLoader());
        } else {
            unitList = null;
        }
        mu21 = in.readString();
        mu31 = in.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getUnitList() {
        return unitList;
    }

    public void setUnitList(ArrayList<String> unitList) {
        this.unitList = unitList;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgURL);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(sales);
        dest.writeString(manufacturer);
        dest.writeString(price);
        dest.writeString(mtrunit);
        dest.writeString(correspondingBase);
        if (unitList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(unitList);
        }
        dest.writeString(mu21);
        dest.writeString(mu31);

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