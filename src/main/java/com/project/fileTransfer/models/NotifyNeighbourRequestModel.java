package com.project.fileTransfer.models;

import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.route.NeighbourTableImpl;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.ui.GUIController;

import java.io.IOException;

/**
 * Created by dimuth on 10/24/17.
 */
public class NotifyNeighbourRequestModel extends AbstractRequestModel {

    private static Handler handler = new HandlerImpl();

    public NotifyNeighbourRequestModel(String ip, int port) {
        super(ip, port);

    }


    @Override
    public void handle() {
        NeighbourTableImpl neighbourTable = NeighbourTableImpl.getInstance();
        Node node = new Node(this.ip, this.port);
        neighbourTable.insert(node);
        PeerTableImpl peerTable = PeerTableImpl.getInstance();
        if (peerTable.getPeerNodeList().size() < 10) {
            if (!peerTable.getPeerNodeList().contains(node)) {
                peerTable.insert(node);
                try {
                    handler.notifyNeighbours(node.getNodeIp(), node.getPort());
                } catch (IOException io) {
                    io.printStackTrace();
                }
                GUIController guiController = GUIController.getInstance();
                guiController.populatePeerTable(peerTable.getPeerNodeList());
            }
        }
        System.out.println("Peer table");
        System.out.println(peerTable.getPeerNodeList());


    }
}
