package com.example.demo.controller;

import com.example.demo.dto.ContactInfo;
import com.example.demo.service.EmailParserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parser")
public class EmailParserController {

    private final EmailParserService emailParserService;

    public EmailParserController(EmailParserService emailParserService) {
        this.emailParserService = emailParserService;
    }

    @PostMapping("/email")
    public ResponseEntity<ContactInfo> parseEmail(@RequestBody String emailBody) {
        try {
            ContactInfo result = emailParserService.parseEmail(emailBody);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}