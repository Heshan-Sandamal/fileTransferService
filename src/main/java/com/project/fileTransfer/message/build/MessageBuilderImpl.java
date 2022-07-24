package com.project.fileTransfer.message.build;


import com.project.fileTransfer.constants.ApplicationConstants;
import com.project.fileTransfer.message.MessageConstants;
import com.project.fileTransfer.models.GracefulLeaveBootstrapServerRequestModel;
import com.project.fileTransfer.models.GracefulLeaveRequestModel;
import com.project.fileTransfer.models.GracefulLeaveResponseModel;
import com.project.fileTransfer.models.HeartBeatSignalModel;
import com.project.fileTransfer.models.Node;
import com.project.fileTransfer.models.NotifyNeighbourRequestModel;
import com.project.fileTransfer.models.RegistrationRequestModel;
import com.project.fileTransfer.models.SearchRequestModel;
import com.project.fileTransfer.models.SearchResponseModel;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class MessageBuilderImpl implements MessageBuilder {

    @Override
    public String buildRegisterRequestMessage(RegistrationRequestModel model) {
        int length = MessageConstants.REG_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length()
                + model.getUserName().length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.REG_MESSAGE + " " + model.getIp() + " " + model.getPort() + " " + model.getUserName();
    }

    @Override
    public String buildUnregisterRequestMessage(GracefulLeaveBootstrapServerRequestModel gracefulLeaveBootstrapServerRequestModel) {
        String ip = gracefulLeaveBootstrapServerRequestModel.getIp();
        int port = gracefulLeaveBootstrapServerRequestModel.getPort();
        String userName = gracefulLeaveBootstrapServerRequestModel.getUserName();
        int length = MessageConstants.UNREG_MESSAGE.length() + ip.length() + String.valueOf(port).length() + userName.length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.UNREG_MESSAGE + " " + ip + " " + port + " " + userName;
    }

    @Override
    public String buildJoinMessage() {
        return null;
    }

    @Override
    public String buildLeaveMessage(GracefulLeaveRequestModel gracefulLeaveRequestModel) {
        String ip = gracefulLeaveRequestModel.getIp();
        int port = gracefulLeaveRequestModel.getPort();
        int length = MessageConstants.LEAVE_MESSAGE.length() + ip.length() + String.valueOf(port).length() + 3 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.LEAVE_MESSAGE + " " + ip + " " + port;
    }

    @Override
    public String buildSearchRequestMessage(SearchRequestModel model) {
        int length = MessageConstants.SER_MESSAGE.length() + model.getIp().length()
                + String.valueOf(model.getPort()).length() + model.getFileName().length()
                + String.valueOf(model.getHops()).length() + 5 + 4;
        for (Node node : model.getLastHops()) {
            length += node.getNodeIp().length();
            length += String.valueOf(node.getPort()).length();
            length += 2;
        }
        String requestFinalLength = String.format("%04d", length);
        requestFinalLength = requestFinalLength + " " + MessageConstants.SER_MESSAGE + " "
                + model.getIp() + " " + model.getPort() + " " + model.getFileName() + " " + model.getHops();
        for (Node node : model.getLastHops()) {
            requestFinalLength += (" " + node.getNodeIp() + " " + node.getPort());
        }
        return requestFinalLength;


    }

    @Override
    public String buildSearchResponseToSourceMessage(SearchResponseModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        if (model.getFileList() != null) {
            for (String s : model.getFileList()) {
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1); // to remove the trailing space.
        }
        int length = MessageConstants.SEROK_MESSAGE.length() + String.valueOf(model.getNoOfFiles()).length()
                + String.valueOf(ApplicationConstants.IP).length() + String.valueOf(ApplicationConstants.PORT).length()
                + String.valueOf(model.getHops()).length() + stringBuilder.length() + 6 + 4 + model.getNoOfFiles() - 1;

        final String requestFinalLength = String.format("%04d", length);
        String s = requestFinalLength + " " + MessageConstants.SEROK_MESSAGE + " " + model.getNoOfFiles() + " "
                + ApplicationConstants.IP + " " + ApplicationConstants.PORT + " " + model.getHops();
        if (model.getFileList() != null) {
            s += " " + stringBuilder;
        }
        return s;
    }

    @Override
    public String buildHeartBeatSignalMessage(HeartBeatSignalModel model) {
        int length = MessageConstants.HEARTBEAT_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length() + model.getUserName().length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.HEARTBEAT_MESSAGE + " " + model.getIp() + " " + model.getPort() + " " + model.getUserName();
    }

    @Override
    public String buildLeaveOkToSourceMessage(GracefulLeaveResponseModel gracefulLeaveResponseModel) {
        int length = MessageConstants.LEAVEOK_MESSAGE.length()+ ApplicationConstants.IP.length() + String.valueOf(ApplicationConstants.PORT).length() + String.valueOf(gracefulLeaveResponseModel.getStatus()).length() + 4+ 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.LEAVEOK_MESSAGE + " " + ApplicationConstants.IP +" "+ ApplicationConstants.PORT + " " + gracefulLeaveResponseModel.getStatus();
    }

    @Override
    public String buildNeighbourJoinMessage(NotifyNeighbourRequestModel model) {
        int length = MessageConstants.NEIGHBOUR_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.NEIGHBOUR_MESSAGE + " " + model.getIp() + " " + model.getPort();
    }


}
