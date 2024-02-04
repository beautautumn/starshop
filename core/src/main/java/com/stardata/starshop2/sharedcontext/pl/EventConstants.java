package com.stardata.starshop2.sharedcontext.pl;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2024/1/29 14:36
 */
public class EventConstants {
    private EventConstants(){}

    public static class EventTopic {
        private EventTopic(){}
        public static final String ORDER_EVENT = "Starshop-Order-Notification";
    }

    public static class EventOperator {
        private EventOperator(){}

        public static final String PLACE = "PlaceOrder";
        public static final String CLOSE = "CloseOrder";
        public static final String PAY = "PayOrder";
        public static final String CANCEL = "CancelOrder";
    }

}
