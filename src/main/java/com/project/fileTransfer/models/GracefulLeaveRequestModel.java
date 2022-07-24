package com.project.fileTransfer.models;


import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.route.NeighbourTableImpl;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.route.StatTableImpl;
import com.project.fileTransfer.ui.GUIController;

import java.io.IOException;

public class GracefulLeaveRequestModel extends AbstractRequestModel {

    private static Handler handler = new HandlerImpl();
    private Node node;

    public GracefulLeaveRequestModel(String ip, int port) {
        super(ip, port);
        node = new Node(this.ip, this.port);
    }

    @Override
    public void handle() {
        final PeerTableImpl peerTable = PeerTableImpl.getInstance();
        final StatTableImpl statTable = StatTableImpl.getInstance();
        NeighbourTableImpl neighbourTable = NeighbourTableImpl.getInstance();
        boolean peerTableRemoved = peerTable.remove(node);
        Boolean statTableRemoved = statTable.remove(node);
        boolean neighbourTableRemoved=neighbourTable.remove(node);

        if (statTableRemoved || peerTableRemoved || neighbourTableRemoved) {
            try {
                handler.sendLeaveOkToSource(new GracefulLeaveResponseModel(ip, port, 0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (peerTableRemoved) {
            GUIController.getInstance().populatePeerTable(peerTable.getPeerNodeList());
        }

        if (statTableRemoved) {
            GUIController.getInstance().populateStatTable(statTable.get());
        }
    }

    public Node getNode() {
        return node;
    }
}
