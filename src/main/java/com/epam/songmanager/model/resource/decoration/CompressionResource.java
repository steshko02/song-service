package com.epam.songmanager.model.resource.decoration;

import com.epam.songmanager.model.resource.ResourceObj;
import com.epam.songmanager.service.impl.GZIPCompressionOperations;
import com.epam.songmanager.service.interfaces.CompressionOperation;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.IOException;
import java.io.InputStream;

@Document
public class CompressionResource implements ResourceObj {

    private String id;
    @Transient
    private  CompressionOperation compressionOperation;

    private  ResourceObj delegate;

    public CompressionResource( ResourceObj delegate) {
       this.delegate = delegate;
        compressionOperation = new GZIPCompressionOperations();
    }

    @SneakyThrows
    @Override
    public void save(InputStream is) throws IOException {
        try (   InputStream isToUse = is;
                InputStream compressedIs = compressionOperation.compressInputStream(isToUse)) {
            delegate.save(compressedIs);
        }
    }

    @Override
    public InputStream read() throws IOException {
        try {
            return compressionOperation.decompressInputStream(delegate.read());
        } catch (IOException e) {
           throw new IOException("failed read InputStream");
        }
    }

    @Override
    public void setStorageId(String storageId) {
        delegate.setStorageId(storageId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getStorageId() {
        return delegate.getStorageId();
    }

    @Override
    public String getPath() {
        return delegate.getPath();
    }

    @Override
    public void delete() {
        delegate.delete();
    }

    @Override
    public void setPath(String path) {
        delegate.setPath(path);
    }

    @Override
    public Class<? extends ResourceObj> supports() {
        return delegate.supports();
    }

    @Override
    public String getFileName() {
        return delegate.getFileName();
    }
}
