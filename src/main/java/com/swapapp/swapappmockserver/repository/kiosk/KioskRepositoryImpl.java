package com.swapapp.swapappmockserver.repository.kiosk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.model.Kiosk;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class KioskRepositoryImpl implements IKioskRepository {

    private List<Kiosk> listOfKiosks = new ArrayList<>();

    public KioskRepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file = ResourceUtils.getFile("classpath:kiosks.json");
        ObjectMapper objectMapper = new ObjectMapper();
        listOfKiosks = objectMapper.readValue(file, new TypeReference<List<Kiosk>>() {});
    }

    @Override
    public List<Kiosk> getKiosks() {
        return listOfKiosks;
    }
}
