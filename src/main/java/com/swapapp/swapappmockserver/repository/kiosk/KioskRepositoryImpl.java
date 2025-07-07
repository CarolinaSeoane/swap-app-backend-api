package com.swapapp.swapappmockserver.repository.kiosk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.model.Kiosk;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class KioskRepositoryImpl implements IKioskRepository {

    private static final String KIOSKS_FILE_PATH = "data/kiosks.json";
    private List<Kiosk> listOfKiosks = new ArrayList<>();

    public KioskRepositoryImpl() throws IOException {
        loadDataBase();
    }

    private void loadDataBase() throws IOException {
        File file = new File(KIOSKS_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        
        if (!file.exists()) {
            System.err.println("Archivo kiosks.json no encontrado en: " + file.getAbsolutePath());
            file.getParentFile().mkdirs(); // Crear directorio data si no existe
            listOfKiosks = new ArrayList<>(); // Lista vacía por defecto
        } else {
            listOfKiosks = objectMapper.readValue(file, new TypeReference<List<Kiosk>>() {});
            System.out.println("Kiosks cargados: " + listOfKiosks.size() + " desde " + KIOSKS_FILE_PATH);
        }
    }

    @Override
    public List<Kiosk> getKiosks() {
        return listOfKiosks;
    }
}
