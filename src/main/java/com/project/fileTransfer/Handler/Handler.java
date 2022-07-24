package com.project.fileTransfer.Handler;


import com.project.fileTransfer.models.GracefulLeaveResponseModel;
import com.project.fileTransfer.models.Node;
import com.project.fileTransfer.models.SearchRequestModel;
import com.project.fileTransfer.models.SearchResponseModel;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Handler {
    void handleResponse(String message);

    void registerInBS(String bsServerIp) throws IOException;

    void searchFile(String file);

    void notifyNeighbours(String ip, int port) throws IOException;

    void sendSearchRequest(SearchRequestModel model, ConcurrentLinkedQueue<Node> concurrentLinkedQueue) throws IOException;

    void sendLocalSearchToSource(SearchResponseModel searchResponseModel, List<String> list) throws IOException;

    void sendHeartBeatSignal();

    void gracefulLeaveRequest();

    void sendLeaveOkToSource(GracefulLeaveResponseModel gracefulLeaveResponseModel) throws IOException;

    void notifyNeighbourLeave(Set<Node> nodes) throws IOException;
}
