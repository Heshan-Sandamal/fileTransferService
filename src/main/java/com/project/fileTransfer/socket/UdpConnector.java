package com.project.fileTransfer.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Future;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface UdpConnector {

//    void send(AbstractRequestModel message) throws IOException;

    void send(String message, InetAddress receiverAddress, int port) throws IOException;
    void sendServerRegister(String message, InetAddress receiverAddress, int port) throws IOException;


    Future<String> receive() throws IOException;

}
