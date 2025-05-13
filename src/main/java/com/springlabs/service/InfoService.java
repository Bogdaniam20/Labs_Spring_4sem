package com.springlabs.service;

import com.springlabs.repository.dao.InfoDao;
import com.springlabs.model.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class InfoService {


    @Autowired
    private InfoDao infoDao;

    private static final String EMAIL_REGEX = "([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})";
    private static final String PHONE_REGEX =
            "(\\+[0-9]{1,4}[-.\\s]?\\(?[0-9]{1,4}?\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9})";

    public List<Info> findAll() {
        return infoDao.findAll();
    }

    public Optional<Info> findById(Integer id) {
        return infoDao.findById(id);
    }

    public Info save(Info info) {
        return infoDao.save(info);
    }

    public void delete(Integer id) {
        infoDao.delete(id);
    }

    public String extractEmails(String text) {

        return this.extractWithRegex(text, EMAIL_REGEX);
    }

    public String extractPhones(String text) {
        return this.extractWithRegex(text, PHONE_REGEX);
    }

    private String extractWithRegex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.results()
                .map(MatchResult::group)
                .collect(Collectors.joining(regex.equals(EMAIL_REGEX) ? " " : ", "));
    }

    public Info update(Info infoDetails) {
        return infoDao.findById(infoDetails.getId())
                .map(existingInfo -> {
                    existingInfo.setEmails(infoDetails.getEmails());
                    existingInfo.setPhones(infoDetails.getPhones());
                    return infoDao.save(existingInfo);
                })
                .orElseThrow(() -> new RuntimeException("Информация по айди:" +
                        " " + infoDetails.getId() + " не найдена"));
    }
}