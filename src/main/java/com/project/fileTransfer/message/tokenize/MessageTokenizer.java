package com.project.fileTransfer.message.tokenize;


import com.project.fileTransfer.models.AbstractRequestResponseModel;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface MessageTokenizer {

    AbstractRequestResponseModel tokenizeMessage(String message);

}
