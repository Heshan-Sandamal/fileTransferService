package com.project.fileTransfer.models;

import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.heartbeater.HeartBeaterImpl;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.ui.GUIController;

/**
 * Created by Tharindu Diluksha on 10/25/2017.
 */
public class HeartBeatSignalModel extends AbstractRequestResponseModel {
    String ip;
    int port;
    String userName;

    public HeartBeatSignalModel(String ip, int port, String userName) {
        this.userName = userName;
        this.port = port;
        this.ip = ip;
    }


    @Override
    public void handle() {
        Node beatedNode = new Node(this.ip, this.port);
        HeartBeaterImpl.getInstance().saveBeatedNodes(beatedNode);
        PeerTableImpl peerTable = PeerTableImpl.getInstance();
        if(!peerTable.getPeerNodeList().contains(beatedNode) && ApplicationConstants.isRegisterd){
            peerTable.insert(beatedNode);
        }
        GUIController.getInstance().populatePeerTable(peerTable.getPeerNodeList());
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }
}
