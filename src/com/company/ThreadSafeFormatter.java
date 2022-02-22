package com.company;

import java.text.SimpleDateFormat;

public class ThreadSafeFormatter {
    public static ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        // Called once for each thread
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("dd.MM.yyyy");
        }

        // 1st call = initialValue(). Subsequent calls will return the same initialiased value
        @Override
        public SimpleDateFormat get(){
            return super.get();
        }
    };
}
