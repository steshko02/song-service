package com.epam.songmanager.model.resource;


import java.io.IOException;
import java.io.InputStream;


public class CloudStorageEntity extends ResourceDecorator {

        @Override
    public InputStream read() throws IOException {
        try {
            return super.read();
        } catch (IOException e) {
            throw new IOException("Exception occurred while decompressing input stream. ", e);
        }
    }

    public CloudStorageEntity(String checkSum, String path, long size) {
        super( checkSum,path,size);
    }


}