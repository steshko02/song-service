package com.epam.songmanager.utils.impl;

import com.epam.songmanager.utils.CheckSum;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

@Component
public class CheckSumImpl implements CheckSum {

    @Override
    public String calculate(InputStream stream, MessageDigest md) throws IOException {
        try (
                var fis = stream;
                var bis = new BufferedInputStream(fis);
                var dis = new DigestInputStream(bis, md)
        ) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }
        var result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    @Override
    public boolean check(String storageFile, String resourceFile) throws IOException {
        return storageFile.equals(resourceFile);
    }
}
