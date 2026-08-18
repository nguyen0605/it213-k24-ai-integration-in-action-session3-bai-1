package com.example.demo.service;

import com.example.demo.dto.ContactInfo;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class EmailParserService {

    private final ChatModel chatModel;

    public EmailParserService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public ContactInfo parseEmail(String emailText) {
        BeanOutputConverter<ContactInfo> converter = new BeanOutputConverter<>(ContactInfo.class);
        String formatInstructions = converter.getFormatInstructions();

        String promptString = "[ROLE]\n" +
                "You are an elite, zero-error data extraction engine designed specifically to parse structured data for machine APIs. Your primary directive is to output ONLY raw JSON compliance data without any fluff.\n\n" +
                "[OBJECTIVE]\n" +
                "Extract the following fields from the client email provided in [INPUT EMAIL]:\n" +
                "- Customer Name (tên khách hàng)\n" +
                "- Phone Number (số điện thoại liên hệ)\n\n" +
                "[INPUT EMAIL]\n" +
                "{email}\n\n" +
                "[STRICT SYSTEM CONSTRAINTS - MANDATORY]\n" +
                "1. Output MUST be a single raw JSON block only. DO NOT wrap the output in markdown code blocks (e.g. no ```json, no ```).\n" +
                "2. NO preambles, NO introductory text, NO polite greetings, and NO conversational postscripts. Absolute silence other than the raw JSON payload.\n" +
                "3. The output must strictly begin with '{' and end with '}'.\n" +
                "4. If a field is missing, set its value to null.\n\n" +
                "[FORMAT INSTRUCTIONS]\n" +
                "{formatInstructions}";

        PromptTemplate template = new PromptTemplate(promptString);
        Prompt prompt = template.create(Map.of(
                "email", emailText,
                "formatInstructions", formatInstructions
        ));

        String response = chatModel.call(prompt).getResult().getOutput().getContent();
        String cleanedResponse = cleanJsonIfNeeded(response);
        return converter.convert(cleanedResponse);
    }

    private String cleanJsonIfNeeded(String raw) {
        if (raw == null) return "{}";
        String clean = raw.trim();
        if (clean.startsWith("```")) {
            clean = clean.replaceAll("^```[a-zA-Z]*\\s*", "");
            clean = clean.replaceAll("\\s*```$", "");
        }
        return clean.trim();
    }
}