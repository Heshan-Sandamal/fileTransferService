package com.project.fileTransfer.route;


import com.project.fileTransfer.models.Node;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */

public interface Table {

    void insert(Node node);

    boolean remove(Node node);

    void search(String query);

}
