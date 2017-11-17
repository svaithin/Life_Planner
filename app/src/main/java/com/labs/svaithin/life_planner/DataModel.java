package com.labs.svaithin.life_planner;

import static android.R.attr.name;
import static android.R.attr.type;

/**
 * Created by Siddharth on 16/11/17.
 */

public class DataModel {

    String time;
    String days;


    public DataModel(String time, String days ) {
        this.time=time;
        this.days=days;


    }

    public String getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }


}
