package com.sionicmobile.ion.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TChwang on 4/6/2015.
 */
public class ExploreListItem implements Parcelable {

    private String _title;

    private String _address;

    private String _logoUrl;

    public ExploreListItem() {

    }

    @Override
    protected Object clone() {

        return new ExploreListItem();
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

    }

}
