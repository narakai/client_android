package com.yobook.model;

import org.json.JSONException;
import org.json.JSONObject;

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

    public BookInfo(String sn, String name, String desc) {
        mSn = sn;
        mName = name;
        mDescription = desc;
    }

    public String toJSONString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("name", mName);
            obj.put("sn", mSn);
            obj.put("summary", mDescription);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return obj.toString();
    }
}
