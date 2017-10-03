package com.rocket.sivico.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Category implements Parcelable {
    private String parent;
    private int id;
    private String title;
    private String color;
    private List<Category> children;

    /**
     * constructor for parent nodes
     *
     * @param id
     * @param title
     */
    public Category(int id, String title, String color) {
        this.children = new ArrayList<>();
        this.id = id;
        this.title = title;
        this.color = color;
    }

    /**
     * constructor for leaf nodes
     *
     * @param parent
     * @param id
     * @param title
     */
    public Category(String parent, int id, String title, String color) {
        this(parent, id, title, new ArrayList<Category>(), color);
    }

    /***
     * constructor for intermediate nodes
     * @param parent
     * @param id
     * @param title
     * @param children
     */
    public Category(String parent, int id, String title, List<Category> children, String color) {
        this.parent = parent;
        this.id = id;
        this.title = title;
        this.children = children;
        this.color = color;
    }

    protected Category(Parcel in) {
        parent = in.readString();
        id = in.readInt();
        title = in.readString();
        color = in.readString();
        children = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public void addChild(Category category) {
        this.children.add(category);
    }

    public int getGlobalId() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.insert(0, this.parent);
        return Integer.valueOf(sb.toString());
    }


    public String getTitle() {
        return title;
    }

    public List<Category> getChildren() {
        return children;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(parent);
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(color);
        parcel.writeTypedList(children);
    }

    public String getParent() {
        return parent;
    }
}
