package com.project.fileTransfer.route;


import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.models.Node;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class NeighbourTableImpl implements Table {

    private static Set<Node> neighbourNodeList;
    private volatile static NeighbourTableImpl neighbourTable;

    static {
        neighbourNodeList = ConcurrentHashMap.newKeySet();
    }

    private NeighbourTableImpl() {
    }

    public static NeighbourTableImpl getInstance() {
        if (neighbourTable == null) {
            synchronized (NeighbourTableImpl.class) {
                if (neighbourTable == null) {
                    neighbourTable = new NeighbourTableImpl();
                }
            }
        }
        return neighbourTable;
    }

    @Override
    public void insert(Node node) {
        System.out.println("Node " + node.getNodeIp() + ":" + node.getPort() + " Added to Neighbour Table of " + ApplicationConstants.IP + ":" + ApplicationConstants.PORT);
        neighbourNodeList.add(node);
    }

    @Override
    public boolean remove(Node node) {
        return neighbourNodeList.remove(node);
    }

    @Override
    public void search(String query) {
    }

    public Set<Node> getNeighbourNodeList() {
        return neighbourNodeList;
    }
}
