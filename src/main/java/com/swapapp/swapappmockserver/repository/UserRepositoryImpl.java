package com.swapapp.swapappmockserver.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private static final String USER_FILE_PATH = "data/users.json";
    private List<User> users = new ArrayList<>();

    public UserRepositoryImpl() throws IOException {
        loadUsers();
    }

    private void loadUsers() throws IOException {
        File file = new File(USER_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        if (!file.exists()) {
            // Crear archivo vacío
            file.getParentFile().mkdirs(); // Crea carpeta 'data' si no existe
            file.createNewFile();
            users = new ArrayList<>();
            persistUsers(); // Guarda lista vacía en el archivo
        } else {
            users = mapper.readValue(file, new TypeReference<List<User>>() {});
        }
    }

    private void persistUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(USER_FILE_PATH), users);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo users.json", e);
        }
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public void save(User user) {
        Optional<User> existing = findByEmail(user.getEmail());
        if (existing.isPresent()) {
            users.remove(existing.get());
        }
        users.add(user);
        persistUsers(); // Guardar en el archivo
    }

}
