package com.springlabs.service;

import com.springlabs.model.Result;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TextService {
    private static final String EMAIL_REGEX = "([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})";

    private static final String PHONE_REGEX = "(\\+?[0-9]{1,4}[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9})";

    public Result parseText(String text) {
        List<String> emails = extractEmails(text);
        List<String> phones = extractPhones(text);

        return new Result(emails, phones);
    }

    private List<String> extractEmails(String text) {
        List<String> emails = new ArrayList<>();
        Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
        Matcher emailMatcher = emailPattern.matcher(text);

        while (emailMatcher.find()) {
            emails.add(emailMatcher.group());
        }

        return emails;
    }

    private List<String> extractPhones(String text) {
        List<String> phones = new ArrayList<>();
        Pattern phonePattern = Pattern.compile(PHONE_REGEX);
        Matcher phoneMatcher = phonePattern.matcher(text);

        while (phoneMatcher.find()) {
            phones.add(phoneMatcher.group());
        }

        return phones;
    }
}