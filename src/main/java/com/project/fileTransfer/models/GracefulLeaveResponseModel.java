package com.project.fileTransfer.models;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class GracefulLeaveResponseModel extends AbstractRequestResponseModel {

    private int status;
    private String ip;
    private int port;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public GracefulLeaveResponseModel(String ip, int port, int status) {
        this.ip=ip;
        this.port=port;
        this.status = status;
    }

    @Override
    public void handle() {
        if (this.status == 0) {
            System.out.println( this.ip + ":"+ this.port +" Node's entries are successfully Updated");
        } else {
            System.out.println( "Error while Updating " +this.ip + this.port);
        }
    }
}
