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

    private List<User> users = new ArrayList<>();

    public UserRepositoryImpl() throws IOException {
        loadUsers();
    }

    private void loadUsers() throws IOException {
        File file = ResourceUtils.getFile("classpath:users.json");
        ObjectMapper mapper = new ObjectMapper();
        users = mapper.readValue(file, new TypeReference<List<User>>() {});
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
        users.add(user);
    }
}
