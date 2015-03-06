package com.yobook.model;

/**
 *
 */
public class BookInfo {
    //书籍唯一ID
    String mId;
    //SN 书籍序列号，可以填写CN \ ISSN \ ISBN等。
    String mSn;
    //书籍名称
    String mName;
    //书籍描述
    String mDescription;

    public BookInfo(String id, String sn, String name, String desc) {
        mId = id;
        mSn = sn;
        mName = name;
        mDescription = desc;
    }
}
