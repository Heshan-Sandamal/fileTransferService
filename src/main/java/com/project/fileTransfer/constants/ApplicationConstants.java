package com.project.fileTransfer.constants;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class ApplicationConstants {

    public static final int PORT = randomWithRange(45000, 55000);
    public static final String USER_NAME = "vin";
    public static final int HOPS = 10;
    public static final int BS_SERVER_PORT = 55555;
    public static final int HEART_BEAT_SEND_THRESHOLD = 5; //in seconds
    public static final int HEART_BEAT_RECEIVE_THRESHOLD = 30; //in seconds
    public static final int HEART_BEAT_CLEAR_THRESHOLD = 100; //in seconds
    public static final String IP_ADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    public static String IP = "127.0.0.1";
    public static String BootstrapServerIp = "";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
    public static volatile Boolean isRegisterd = false;
}
