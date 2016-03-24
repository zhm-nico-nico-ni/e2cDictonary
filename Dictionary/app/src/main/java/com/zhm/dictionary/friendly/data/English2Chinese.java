package com.zhm.dictionary.friendly.data;

import android.os.Parcel;
import android.os.Parcelable;

public class English2Chinese implements Parcelable {
    public final static String WORD = "word";
    public final static String DESCRIPTION = "description";
    public static final Creator<English2Chinese> CREATOR = new Creator<English2Chinese>() {
        @Override
        public English2Chinese createFromParcel(Parcel in) {
            return new English2Chinese(in);
        }

        @Override
        public English2Chinese[] newArray(int size) {
            return new English2Chinese[size];
        }
    };
    public String word;
    public String description;

    public English2Chinese() {
    }

    protected English2Chinese(Parcel in) {
        word = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(description);
    }
}
