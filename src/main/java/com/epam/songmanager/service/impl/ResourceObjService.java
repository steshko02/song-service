package com.epam.songmanager.service.impl;

import com.epam.songmanager.jms.Producer;
import com.epam.songmanager.model.entity.StorageType;
import com.epam.songmanager.model.resource.ResourceObj;
import com.epam.songmanager.model.storage.Storage;
import com.epam.songmanager.repository.mango.ResourceObjRepository;
import com.epam.songmanager.repository.mango.StorageRepository;
import com.epam.songmanager.service.interfaces.ResourceObjectService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class ResourceObjService implements ResourceObjectService {

    @Autowired
    private ResourceObjRepository resourceObjRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private Producer producer;

    public void store(InputStream inputStream,StorageType storageType,String ex) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Storage storage = storageRepository.getStorage(storageType);
        ResourceObj resource = storage.requestBuilder().withCompression().build();
        resourceObjRepository.saveResource(resource);
        resource.save(inputStream);
        producer.sendMessage(resource.getId());
    }

    public  List<String> loadAll(StorageType storageType) throws IOException {
        Storage storage = storageRepository.getStorage(storageType);
        List<ResourceObj> resources = resourceObjRepository.getByStorageId(storage.getId());
        List<String> filenames = new ArrayList<>();
        resources.stream().forEach(x->filenames.add(x.getFileName()));
       return filenames;
    }

    @Override
    public ResourceObj getResource(String id) {
        return resourceObjRepository.getResourceById(id);
    }
}
