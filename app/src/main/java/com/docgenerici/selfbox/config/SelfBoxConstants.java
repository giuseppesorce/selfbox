package com.docgenerici.selfbox.config;

/**
 * @author Giuseppe Sorce #@copyright xx 16/10/16.
 */

public class SelfBoxConstants {

    public static String pathProduct="http://www.docgenerici.it";

    public class TypeContent{
        public static final int PDF= 1;
        public static final int FOLDER= 2;
        public static final int VIDEO= 3;
        public static final int VISUAL= 4;
    }

    public class TypeViewContent{
        public static final int NEW= 1;
        public static final int READ= 2;
        public static final int NOREAD= 3;
        public static final int MPORTANT= 4;
    }

    public class TypeProductRow{

        public static final int HEADER= 0;
        public static final int PRODUCT = 1;
    }

    public class ContentSyncType{
        public static final int CONTENTS= 1;
        public static final int ANAGRAFICHE= 2;
        public static final int PERSONAL= 3;
        public static final int BRIEFCASE= 4;
        public static final int LOGS= 5;
        public static final int PRODUCTS= 6;
    }
}
