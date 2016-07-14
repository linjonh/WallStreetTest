package com.jaysen.wallstreet.util;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Message implements Parcelable {
    public String id;
    public String authorId;
    public String title;
    public String summary;
    public String content;
    public String image;
    public String imageType;
    public String url;
    public String source;
    public boolean liked;
    public int likeCount;
    public int style;// MessageStyleShort 1 MessageStyleLong 2  MessageStyleUrl 3
    public int type;//1 new //2 hot
    public long createdAt;
    //public String[] subjectIds;
    public ArrayList<Stock> stocks;
    public String shareUrl;

    public Message() {
    }

    protected Message(Parcel in) {
        id = in.readString();
        authorId = in.readString();
        title = in.readString();
        summary = in.readString();
        content = in.readString();
        image = in.readString();
        imageType = in.readString();
        url = in.readString();
        source = in.readString();
        liked = in.readByte() != 0;
        likeCount = in.readInt();
        style = in.readInt();
        type = in.readInt();
        createdAt = in.readLong();
        shareUrl = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(authorId);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(content);
        dest.writeString(image);
        dest.writeString(imageType);
        dest.writeString(url);
        dest.writeString(source);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeInt(likeCount);
        dest.writeInt(style);
        dest.writeInt(type);
        dest.writeLong(createdAt);
        dest.writeString(shareUrl);
    }
}
