package com.example.eyes38.beans;

import android.provider.BaseColumns;

/**
 * Created by jqchen on 2016/6/15.
 */
public final class ConsultTable {
    public static class Field implements BaseColumns{
        public static final String TABLE_NAME = "consult";
        public static final String CUSOTMER_ID = "customerid";
        public static final String CONSULT_ID = "consultid";
        public static final String CONSULT_CONTENT = "consultcontent";
        public static final String CONSULT_REPLY = "consultreply";
        public static final String IMAGE = "image";
    }
}
