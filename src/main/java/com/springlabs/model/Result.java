package com.springlabs.model;

import lombok.Data;

import java.util.List;

@Data
public class Result
{
    private List<String> emails;
    private List<String> phones;

    public Result(List<String> emails, List<String> phones)
    {
        this.emails = emails;
        this.phones = phones;
    }
}
