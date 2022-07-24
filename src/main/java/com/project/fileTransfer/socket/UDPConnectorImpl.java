package com.project.fileTransfer.socket;


import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import com.project.fileTransfer.constants.ApplicationConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class UDPConnectorImpl implements UdpConnector {

    private static DatagramSocket socket;
    private static final Handler handler;

    static {
        try {
            socket = new DatagramSocket(ApplicationConstants.PORT);
            try (DatagramSocket socket1 = new DatagramSocket()) {
                socket1.connect(InetAddress.getByName("10.10.29.237"), ApplicationConstants.PORT);
                ApplicationConstants.IP = socket1.getLocalAddress().getHostAddress();
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        executorService = Executors.newFixedThreadPool(30);
        handler = new HandlerImpl();
    }

    private static final ExecutorService executorService;


    @Override
    public void send(String message, InetAddress receiverAddress, int port) throws IOException {
        System.out.println("Calling endpoint " + "http://localhost:" + port + "/");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>(message);
        String foo = restTemplate.postForObject("http://localhost:" + port + "/", request, String.class);
        System.out.println(foo);

    }

    public void sendServerRegister(String message, InetAddress receiverAddress, int port) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(
                buffer, buffer.length, receiverAddress, port);
        socket.send(packet);
    }

    @Override
    public Future<String> receive() throws IOException {
        byte[] bufferIncoming = new byte[55000];
        DatagramPacket incomingPacket = new DatagramPacket(bufferIncoming, bufferIncoming.length);
        socket.receive(incomingPacket);
        String incomingMessage = new String(bufferIncoming);
        return (Future<String>) executorService.submit(() -> handler.handleResponse(incomingMessage));
    }

    public void killExecutorService() {
        executorService.shutdown();
    }
}
