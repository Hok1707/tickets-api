package com.kimhok.tickets.common.utils;

import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class BillUtil {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private static String lastDate = "";

    public static synchronized String generateBillNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        if (!today.equals(lastDate)) {
            counter.set(0);
            lastDate = today;
        }
        int sequence = counter.incrementAndGet();
        return String.format("ET-%s-%04d", today, sequence);
    }

    public static synchronized String generateTxnNumber() {
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        if (!today.equals(lastDate)) {
            counter.set(0);
            lastDate = today;
        }
        int sequence = counter.incrementAndGet();
        return String.format("TXN-%s-%04d", today, sequence);
    }
}
