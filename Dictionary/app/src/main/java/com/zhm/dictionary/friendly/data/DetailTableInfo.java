package com.zhm.dictionary.friendly.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DetailTableInfo implements Parcelable {

    public static final Creator<DetailTableInfo> CREATOR = new Creator<DetailTableInfo>() {
        @Override
        public DetailTableInfo createFromParcel(Parcel in) {
            return new DetailTableInfo(in);
        }

        @Override
        public DetailTableInfo[] newArray(int size) {
            return new DetailTableInfo[size];
        }
    };
    public English2Chinese theWord;
    public String addTime;
    public int count;

    public DetailTableInfo() {
    }

    protected DetailTableInfo(Parcel in) {
        theWord = in.readParcelable(English2Chinese.class.getClassLoader());
        addTime = in.readString();
        count = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(theWord, flags);
        dest.writeString(addTime);
        dest.writeInt(count);
    }
}
