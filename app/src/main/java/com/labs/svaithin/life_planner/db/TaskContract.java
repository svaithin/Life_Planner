package com.labs.svaithin.life_planner.db;

import android.provider.BaseColumns;

/**
 * Created by Siddharth on 22/08/17.
 */

public class TaskContract {

    public static final String DB_NAME = "com.svaithin.lifeplanner.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {

        //Table names
        public static final String PLAN = "plan";
        public static final String PMILESTONE = "pmilestone";
        public static final String GOAL = "goal";
        public static final String GMILESTONE = "gmilestone";
        public static final String HABIT = "habit";
        public static final String NOTIFICATION = "notification";


        //Colum name for PLAN
        public static final String PLANNAME = "planname";
        public static final String PCOMPLETED = "pcompleted";
        public static final String PARCHIVED = "parch";

        //Column names for PMILESTONE
        public static final String PMILESTONENAME = "pmilestinename";
        public static final String PMCOMPLETED = "pmcompleted";
        public static final String PMPLANID = "pmplanid";

        //Column names for GMILESTONE
        public static final String GMILESTONENAME = "gmilestinename";
        public static final String GMCOMPLETED = "gmcompleted";
        public static final String GMGOALID = "gmgoalid";

        //Column name for GOAL
        public static final String GOALNAME = "goalname";
        public static final String GCOMPLETED = "gcompleted";
        public static final String GARCHIVED = "garch";
        public static final String PLANID = "planid";

        // Column name for Habit
        public static final String HABITNAME = "habitname";
        public static final String HCOMPLETED = "hcompleted";
        public static final String HARCHIVED = "harch";
        public static final String GOALID = "goalid";

        // Column name for Habit
        public static final String NOTIFINAME= "notifname";
        public static final String NCOMPLETED = "ncompleted";
        public static final String NARCHIVED = "narch";
        public static final String HABITID = "habitid";
        public static final String TIME = "time";
        public static final String DAYOFWEEK = "dayofweek";
    }
}
