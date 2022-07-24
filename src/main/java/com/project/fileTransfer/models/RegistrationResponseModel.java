package com.project.fileTransfer.models;

import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.ui.GUIController;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class RegistrationResponseModel extends AbstractRequestResponseModel {
    HashSet<Node> nodeset;
    int nodeCount;
    Handler handler;

    public RegistrationResponseModel(int nodeCount, HashSet<Node> nodeset) {
        this.nodeCount = nodeCount;
        this.nodeset = nodeset;
        handler = new HandlerImpl();
    }

    @Override
    public void handle() {
        PeerTableImpl peerTable = PeerTableImpl.getInstance();
        nodeset.forEach((node) -> {
            peerTable.insert(node);
            try {
                handler.notifyNeighbours(node.getNodeIp(), node.getPort());
            } catch (IOException io) {
                io.printStackTrace();
            }
        });
        GUIController guiController = GUIController.getInstance();
        guiController.populatePeerTable(peerTable.getPeerNodeList());
        guiController.displayMessage("Successfully registered");
        ApplicationConstants.isRegisterd = true;
        guiController.handleRegisteration();
    }
}
