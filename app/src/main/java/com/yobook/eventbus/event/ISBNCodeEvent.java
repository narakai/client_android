package com.yobook.eventbus.event;

/**
 * Created by michael on 3/1/15.
 */
public class ISBNCodeEvent {
    String mCode = null;

    public ISBNCodeEvent(String code) {
        mCode = code;
    }

    public String getISBNCode() {
        return mCode;
    }

}
