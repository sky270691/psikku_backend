package com.psikku.backend.controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


@RestController
public class SseController
{
    
    private final Map<Integer,SseEmitter> emitterList = new HashMap<>();

    @GetMapping("/sse/test")
    public SseEmitter handleTest(@RequestParam Integer userId) {
        SseEmitter emitter = new SseEmitter();
        try {
            emitter.send(SseEmitter.event().name("INIT"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        emitter.onCompletion(() -> emitterList.remove(userId));
        emitterList.put(userId,emitter);

        return emitter;
    }

    @GetMapping("/sse/trigger")
    public void trigger(@RequestParam String message, @RequestParam Integer userId) {
        SseEmitter emitter = emitterList.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("pesan_satu_tas_tarabe").data(message));
            } catch (IOException e) {
                emitterList.remove(userId);
            }
        }
    }

}

