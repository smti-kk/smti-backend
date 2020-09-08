package ru.cifrak.telecomit.backend.utils;

import java.util.Arrays;
import java.util.Collections;

public class IpReversed {
    private final String ip;

    public IpReversed(String ipConfig) {
        this.ip = ipConfig;
    }

    public String ip() {
        String[] chunks;
        if (ip.matches(".*\\/.*")) {
            chunks = ip.substring(0, ip.indexOf("/")).split("\\.");
        } else {
            chunks = ip.split("\\.");
        }
        Collections.reverse(Arrays.asList(chunks));
        return String.join(".", chunks);
    }

    public String ipStrait() {
        String[] chunks;
        if (ip.matches(".*\\/.*")) {
            chunks = ip.substring(0, ip.indexOf("/")).split("\\.");
        } else {
            chunks = ip.split("\\.");
        }
//        Collections.reverse(Arrays.asList(chunks));
        return String.join(".", chunks);
    }

    ;

    public String mask() {
        if (!ip.matches(".*\\/.*")) {
            return "32";
        } else {
            return ip.substring(ip.indexOf("/") + 1);
        }
    }

}
