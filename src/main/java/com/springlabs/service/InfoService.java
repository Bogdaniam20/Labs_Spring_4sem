package com.springlabs.service;

import com.springlabs.model.User;
import com.springlabs.repository.InfoRepository;
import com.springlabs.model.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Data;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@Service
public class InfoService {

    @Autowired
    private InfoRepository infoRepository;

    private static final String EMAIL_REGEX = "([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})";
    private static final String PHONE_REGEX = "(\\+[0-9]{1,4}[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9})";

    public List<Info> findAll() {
        return infoRepository.findAll();
    }

    public Optional<Info> findById(Integer id) {
        return infoRepository.findById(id);
    }

    public Info save(Info info) {
        //info.setId(null);
        return infoRepository.save(info);
    }

    public void delete(Integer id) {
        infoRepository.deleteById(id);
    }

    public Info GetText(String text, User user) {
        String emails = extractEmails(text);
        String phones = extractPhones(text);
        Info info = new Info();
        info.setEmails(emails);
        info.setPhones(phones);
        info.setUser(user);
        return save(info);
    }

    private String extractEmails(String text) {
        StringBuilder emails = new StringBuilder();
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = emailPattern.matcher(text);

        while (emailMatcher.find()) {
            emails.append(emailMatcher.group()).append(", ");
        }

        if (emails.length() > 0) {
            emails.setLength(emails.length() - 2);
        }

        return emails.toString();
    }

    private String extractPhones(String text) {
        StringBuilder phones = new StringBuilder();
        Pattern phonePattern = Pattern.compile(PHONE_REGEX);
        Matcher phoneMatcher = phonePattern.matcher(text);

        while (phoneMatcher.find()) {
            phones.append(phoneMatcher.group()).append(", ");
        }

        if (phones.length() > 0) {
            phones.setLength(phones.length() - 2);
        }

        return phones.toString();
    }

    public Info update(Info infoDetails) {
        Info info = infoRepository.findById(infoDetails.getId())
                .orElseThrow(() -> new RuntimeException("Info not found"));
        info.setEmails(infoDetails.getEmails());
        info.setPhones(infoDetails.getPhones());
        return infoRepository.save(info);
    }
}