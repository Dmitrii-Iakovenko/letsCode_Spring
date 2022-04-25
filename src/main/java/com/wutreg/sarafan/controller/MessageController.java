package com.wutreg.sarafan.controller;

import com.wutreg.sarafan.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("messages")
public class MessageController {

    private int counter = 4;

    private final List<Map<String, String>> messages = new ArrayList<>(){{
        add(new HashMap<>() {{ put("id", "1"); put("text", "First message"); }} );
        add(new HashMap<>() {{ put("id", "2"); put("text", "Second message"); }} );
        add(new HashMap<>() {{ put("id", "3"); put("text", "Third message"); }} );
    }};


    @GetMapping
    public List<Map<String, String>> getAll() {
        return messages;
    }

    @GetMapping("{id}")
    public Map<String, String> getById(@PathVariable String id) {
        return messages.stream()
            .filter(message -> message.get("id").equals(id))
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> message) {
        message.put("id", String.valueOf(counter++));
        messages.add(message);
        return message;
    }

    @PutMapping("{id}")
    public Map<String, String> update(@PathVariable String id, @RequestBody Map<String, String> message) {
        Map<String, String> messageFromDb = getById(id);
        messageFromDb.putAll(message);
        messageFromDb.put("id", id);
        return messageFromDb;
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Map<String, String> message = getById(id);
        messages.remove(message);
    }
}
