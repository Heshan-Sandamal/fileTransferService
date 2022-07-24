package com.project.fileTransfer.models;

public class GracefulLeaveBootstrapServerRequestModel extends AbstractRequestModel {

    String userName;

    public GracefulLeaveBootstrapServerRequestModel(String ip, int port, String userName) {
        super(ip, port);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public void handle() {

    }
}
