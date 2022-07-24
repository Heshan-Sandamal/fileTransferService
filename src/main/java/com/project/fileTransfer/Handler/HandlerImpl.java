package com.project.fileTransfer.Handler;


import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.message.build.MessageBuilder;
import com.project.fileTransfer.message.build.MessageBuilderImpl;
import com.project.fileTransfer.message.tokenize.MessageTokenizer;
import com.project.fileTransfer.message.tokenize.MessageTokenizerImpl;
import com.project.fileTransfer.models.AbstractRequestResponseModel;
import com.project.fileTransfer.models.GracefulLeaveBootstrapServerRequestModel;
import com.project.fileTransfer.models.GracefulLeaveRequestModel;
import com.project.fileTransfer.models.GracefulLeaveResponseModel;
import com.project.fileTransfer.models.HeartBeatSignalModel;
import com.project.fileTransfer.models.Node;
import com.project.fileTransfer.models.NotifyNeighbourRequestModel;
import com.project.fileTransfer.models.RegistrationRequestModel;
import com.project.fileTransfer.models.SearchRequestModel;
import com.project.fileTransfer.models.SearchResponseModel;
import com.project.fileTransfer.route.NeighbourTableImpl;
import com.project.fileTransfer.route.PeerTableImpl;
import com.project.fileTransfer.socket.UDPConnectorImpl;
import com.project.fileTransfer.socket.UdpConnector;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class HandlerImpl implements Handler {

    private MessageTokenizer messageTokenizer;
    private MessageBuilder messageBuilder;
    private UdpConnector udpConnector;

    public HandlerImpl() {
        this.udpConnector = new UDPConnectorImpl();
        this.messageTokenizer = new MessageTokenizerImpl();
        this.messageBuilder = new MessageBuilderImpl();
    }

    @Override
    public void handleResponse(String message) {
        AbstractRequestResponseModel abstractRequestResponseModel = messageTokenizer.tokenizeMessage(message);
        if (abstractRequestResponseModel != null) {
            abstractRequestResponseModel.handle();
        }

    }

    @Override
    public void registerInBS(String bsServerIp) throws IOException {
        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, ApplicationConstants.USER_NAME);
        String message = messageBuilder.buildRegisterRequestMessage(registrationRequestModel);
        udpConnector.sendServerRegister(message, InetAddress.getByName(bsServerIp), 55555);
        ApplicationConstants.BootstrapServerIp = bsServerIp;
    }

    @Override
    public void sendHeartBeatSignal() {
        Set<Node> neighbourNodes = NeighbourTableImpl.getInstance().getNeighbourNodeList();
        if (!neighbourNodes.isEmpty()) {
            HeartBeatSignalModel heartBeatSignalModel = new HeartBeatSignalModel(ApplicationConstants.IP, ApplicationConstants.PORT, ApplicationConstants.USER_NAME);
            for (Node neighbour : neighbourNodes) {
                String heartBeatMessage = messageBuilder.buildHeartBeatSignalMessage(heartBeatSignalModel);
                try {
                    System.out.println("Sending HBEAT to "+neighbour.getNodeIp()+" "+ neighbour.getPort() +"by" + ApplicationConstants.IP + " " + String.valueOf(ApplicationConstants.PORT) + " " + ApplicationConstants.USER_NAME);
                    udpConnector.send(heartBeatMessage, InetAddress.getByName(neighbour.getNodeIp()), neighbour.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void gracefulLeaveRequest() {
        NeighbourTableImpl neighbourTable = NeighbourTableImpl.getInstance();

        //First Unreg from the Bootstrap server
        GracefulLeaveBootstrapServerRequestModel gracefulLeaveBootstrapServerRequestModel = new GracefulLeaveBootstrapServerRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, ApplicationConstants.USER_NAME);
        String message = messageBuilder.buildUnregisterRequestMessage(gracefulLeaveBootstrapServerRequestModel);
        try {
            udpConnector.send(message, InetAddress.getByName(ApplicationConstants.BootstrapServerIp), ApplicationConstants.BS_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendLeaveOkToSource(GracefulLeaveResponseModel gracefulLeaveResponseModel) throws IOException {
        String buildLeaveOkToSourceMessage = messageBuilder.buildLeaveOkToSourceMessage(gracefulLeaveResponseModel);
        udpConnector.send(buildLeaveOkToSourceMessage,InetAddress.getByName(gracefulLeaveResponseModel.getIp()),gracefulLeaveResponseModel.getPort());
    }

    @Override
    public void searchFile(String file) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(ApplicationConstants.IP, ApplicationConstants.PORT));
        SearchRequestModel searchRequestModel = new SearchRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, file, ApplicationConstants.HOPS, nodes);
        searchRequestModel.handle();
    }

    @Override
    public void sendSearchRequest(SearchRequestModel model, ConcurrentLinkedQueue<Node> statTablePeers) throws IOException {
        String searchRequestMessage = messageBuilder.buildSearchRequestMessage(model);

        System.out.println("Found stat table entries");
        System.out.println(statTablePeers);

        Iterator<Node> nodeIterator = statTablePeers.iterator();
        while (nodeIterator.hasNext()) {
            Node node = nodeIterator.next();
            if (!model.getLastHops().contains(node)) {
                udpConnector.send(searchRequestMessage, InetAddress.getByName(node.getNodeIp()), node.getPort());
            }
            System.out.println("send to stat table entries " + node.getPort());
        }

        final Set<Node> peerNodeList = PeerTableImpl.getInstance().getPeerNodeList();

        final ArrayList<Node> peerNodeListToSend = new ArrayList<>();

        peerNodeList.forEach((node) -> {
            if (!model.getLastHops().contains(node) && !statTablePeers.contains(node)) {
                peerNodeListToSend.add(node);
            }
        });

        System.out.println("peer nodes to send list " + peerNodeListToSend);
        Random random = new Random();
        int size = peerNodeListToSend.size();
        if (size > 0) {
            final int item1 = random.nextInt(size);
            Node node = peerNodeListToSend.get(item1);
            udpConnector.send(searchRequestMessage, InetAddress.getByName(node.getNodeIp()), node.getPort());
            System.out.println("Sending to peer node " + peerNodeListToSend.get(item1).getPort());
            peerNodeListToSend.remove(item1);
        }
        size = peerNodeListToSend.size();
        if (size > 0) {
            final int item2 = random.nextInt(size);
            Node node = peerNodeListToSend.get(item2);
            udpConnector.send(searchRequestMessage, InetAddress.getByName(node.getNodeIp()), node.getPort());
            System.out.println("Sending to peer node " + peerNodeListToSend.get(item2).getPort());
        }
    }

    @Override
    public void sendLocalSearchToSource(SearchResponseModel searchResponseModel, List<String> list) throws IOException {
        String searchResponseToSourceMessage = messageBuilder.buildSearchResponseToSourceMessage(searchResponseModel);
        udpConnector.send(searchResponseToSourceMessage, InetAddress.getByName(searchResponseModel.getIp()), searchResponseModel.getPort());
    }

    //check whether the stat table entry equals to the node which request the file
    private boolean isRequestingNode(SearchRequestModel searchRequestModel, Node node) {
        return searchRequestModel.getFileName().equals(node.getNodeIp()) && searchRequestModel.getPort() == node.getPort();
    }

    public void notifyNeighbours(String ip, int port) throws IOException {
        NotifyNeighbourRequestModel notifyNeighbourRequestModel = new NotifyNeighbourRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT);
        String message = messageBuilder.buildNeighbourJoinMessage(notifyNeighbourRequestModel);
        udpConnector.send(message, InetAddress.getByName(ip), port);

    }

    public void notifyNeighbourLeave(Set<Node> nodes) throws IOException {
        nodes.forEach(node -> {
            GracefulLeaveRequestModel gracefulLeaveRequestModel = new GracefulLeaveRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT);
            String neighbourLeaveMessage = new MessageBuilderImpl().buildLeaveMessage(gracefulLeaveRequestModel);
            try {
                udpConnector.send(neighbourLeaveMessage, InetAddress.getByName(node.getNodeIp()), node.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


}
