package com.project.fileTransfer.models;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class JoinResponseModel extends AbstractRequestResponseModel {

    int status;

    public JoinResponseModel(int status) {
        this.status = status;
    }

    @Override
    public void handle() {
        System.out.println("Join Response Received");

    }
}
