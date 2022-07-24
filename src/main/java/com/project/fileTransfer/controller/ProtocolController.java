package com.project.fileTransfer.controller;

import com.project.fileTransfer.Handler.Handler;
import com.project.fileTransfer.Handler.HandlerImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class ProtocolController {

    private static final Handler handler = new HandlerImpl();
    private static final ExecutorService executorService;

    static {
        executorService = Executors.newFixedThreadPool(30);
    }


    @PostMapping("/")
    public String receive(@RequestBody String data) {
        final Future<String> submit = (Future<String>) executorService.submit(() -> handler.handleResponse(data));
        return "Success";
    }

}
