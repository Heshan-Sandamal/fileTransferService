package com.project.fileTransfer.files;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class FileHandlerImpl implements FileHandler {
    private volatile static FileHandlerImpl fileHandler;
    private List<String> localFileList;

    private FileHandlerImpl() {
        localFileList = new ArrayList<>();

    }

    public static FileHandlerImpl getInstance() {
        if (fileHandler == null) {
            synchronized (FileHandlerImpl.class) {
                if (fileHandler == null) {
                    fileHandler = new FileHandlerImpl();
                }
            }
        }
        return fileHandler;
    }

    public void initializeFileStorage(String documentName) {
        localFileList.add(documentName);
//        System.out.println("Added " + documentName + " "+localFileList.size());
    }

    public List<String> searchLocalFileList(String searchText) {
        String regex = ".*?\\b" + searchText.toLowerCase() + "\\b.*?";
        return localFileList.stream().filter(s -> {
            String sl = s.toLowerCase().replace("@"," " );
            return sl.matches(regex);

        }).collect(Collectors.toList());
    }

}
