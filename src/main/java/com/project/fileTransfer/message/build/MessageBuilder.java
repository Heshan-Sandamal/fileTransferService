package com.project.fileTransfer.message.build;


import com.project.fileTransfer.models.GracefulLeaveBootstrapServerRequestModel;
import com.project.fileTransfer.models.GracefulLeaveRequestModel;
import com.project.fileTransfer.models.GracefulLeaveResponseModel;
import com.project.fileTransfer.models.HeartBeatSignalModel;
import com.project.fileTransfer.models.NotifyNeighbourRequestModel;
import com.project.fileTransfer.models.RegistrationRequestModel;
import com.project.fileTransfer.models.SearchRequestModel;
import com.project.fileTransfer.models.SearchResponseModel;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface MessageBuilder {

    String buildRegisterRequestMessage(RegistrationRequestModel model);

    String buildUnregisterRequestMessage(GracefulLeaveBootstrapServerRequestModel gracefulLeaveBootstrapServerRequestModel);

    String buildJoinMessage();

    String buildLeaveMessage(GracefulLeaveRequestModel gracefulLeaveRequestModel);

    String buildSearchRequestMessage(SearchRequestModel model);

    String buildNeighbourJoinMessage(NotifyNeighbourRequestModel model);

    String buildSearchResponseToSourceMessage(SearchResponseModel model);

    String buildHeartBeatSignalMessage(HeartBeatSignalModel model);

    String buildLeaveOkToSourceMessage(GracefulLeaveResponseModel gracefulLeaveResponseModel);
}
