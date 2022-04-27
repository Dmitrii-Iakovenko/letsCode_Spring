package com.wutreg.sarafan.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.wutreg.sarafan.entity.Message;
import com.wutreg.sarafan.entity.Views;
import com.wutreg.sarafan.repository.MessageRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageController {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    @GetMapping
    @JsonView(Views.IdName.class)
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @GetMapping("{id}")
    @JsonView(Views.FullMessage.class)
    public Message getById(@PathVariable("id") Message message) {
        return message;
    }

    @PostMapping
    public Message create(@RequestBody Message message) {
        message.setCreationDate(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @PutMapping("{id}")
    public Message update(
            @PathVariable("id") Message messageFromDb,
            @RequestBody Message message)
    {
        BeanUtils.copyProperties(message, messageFromDb, "id");
        return messageRepository.save(messageFromDb);
     }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Message message) {
        messageRepository.delete(message);
    }
}
