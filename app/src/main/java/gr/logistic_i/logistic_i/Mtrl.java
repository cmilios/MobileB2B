package gr.logistic_i.logistic_i;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

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


    public String getMtrunit() {
        return mtrunit;
    }

    public void setMtrunit(String mtrunit) {
        this.mtrunit = mtrunit;
    }

    public Mtrl(String imgURL, String code, String name, String sales, String manufacturer, String mtrunit, String correspondingBase) {
        this.imgURL = imgURL;
        this.code = code;
        this.name = name;
        this.sales = sales;
        this.manufacturer = manufacturer;
        this.correspondingBase = correspondingBase;
        this.mtrunit = mtrunit;

    }

    public void loadImage(){
        GsonWorker gsonWorker = new GsonWorker(correspondingBase);
        if (imgURL!=null){
            image = gsonWorker.getImage(correspondingBase, imgURL);

        }


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
    }

    @Override
    public int describeContents() {
        return 0;
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