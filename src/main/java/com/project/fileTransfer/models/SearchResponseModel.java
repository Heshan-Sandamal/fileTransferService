package com.project.fileTransfer.models;



import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.route.StatTableImpl;
import com.project.fileTransfer.ui.GUIController;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class SearchResponseModel extends AbstractRequestResponseModel {

    private String ip;
    private int port;
    private int hops;
    private int noOfFiles;
    private HashSet<String> fileList;
    private static Handler handler = new HandlerImpl();

    public SearchResponseModel(String ip, int port, int hops, int noOfFiles, HashSet<String> fileList) {
        this.ip = ip;
        this.port = port;
        this.hops = hops;
        this.noOfFiles = noOfFiles;
        this.fileList = fileList;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getHops() {
        return hops;
    }

    public int getNoOfFiles() {
        return noOfFiles;
    }

    public HashSet<String> getFileList() {
        return fileList;
    }

    @Override
    public void handle() {
        final StatTableImpl statTable = StatTableImpl.getInstance();
        final PeerTableImpl peerTable = PeerTableImpl.getInstance();

        GUIController guiController = GUIController.getInstance();
        // Go inside iff ip and the port are equal.
        if (ApplicationConstants.IP.equals(this.ip) && ApplicationConstants.PORT == this.port) {
            System.out.println("Matching file are found at query source node, they are : ");
            fileList.stream().map(s -> s.replace("@", " ")).forEach(System.out::println);
            guiController.displaySearchResults(this);
        } else {
            System.out.println("Files found @ >>>>> " + this.getIp() + " : " + this.port + " and they are : ");
            fileList.forEach(s -> System.out.println("\t" + "* " + s));

            guiController.displaySearchResults(this);

            Node node = new Node(this.ip, this.port);
            final boolean[] isStatTableUpdated = {false};
            fileList.forEach((fileName) -> {
                ConcurrentLinkedQueue<Node> concurrentLinkedQueue = statTable.get(fileName);
                if (concurrentLinkedQueue == null) {
                    concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
                    statTable.insert(fileName, concurrentLinkedQueue);
                }
                if (!concurrentLinkedQueue.contains(node)) {
                    concurrentLinkedQueue.add(node);
                    isStatTableUpdated[0] =true;
                }
            });

            if(isStatTableUpdated[0]){
                guiController.populateStatTable(statTable.get());
            }

            System.out.println("Stat Table ");
            System.out.println(statTable.getStatTable());

            if (!peerTable.getPeerNodeList().contains(node)) {
                peerTable.insert(node);
                try {
                    handler.notifyNeighbours(node.getNodeIp(), node.getPort());
                } catch (IOException io) {
                    io.printStackTrace();
                }
                guiController.populatePeerTable(peerTable.getPeerNodeList());
            }
        }
    }
}
