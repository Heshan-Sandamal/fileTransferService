package com.project.fileTransfer.models;

public abstract class AbstractRequestModel extends AbstractRequestResponseModel {
    String ip;
    int port;

    String type;

    public AbstractRequestModel(String ip, int port) {
        this.ip = ip;
        this.port = port;
//        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
}
