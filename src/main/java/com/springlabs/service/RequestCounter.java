package com.springlabs.service;

import org.springframework.stereotype.Component;

@Component
public class RequestCounter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}

//E:\JMETER\apache-jmeter-5.6.3\bin\jmeter.bat -n -t .\TEST-100.jmx -l log-100.jtl -e -o .\RESULT-100\