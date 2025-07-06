package com.swapapp.swapappmockserver.repository.trade;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.model.trades.TradeRequest;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TradeRepositoryImpl implements ITradeRepository {
    private static final String TRADE_REQUESTS_FILE_PATH = "data/tradeRequests.json";
    private List<TradeRequest> tradeRequests = new ArrayList<>();

    public TradeRepositoryImpl() throws IOException {
        loadAllTradeRequests();
    }

    private void loadAllTradeRequests() throws IOException {
        File file = new File(TRADE_REQUESTS_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
            tradeRequests = new ArrayList<>();
            persistTradeRequests();
        } else {
            tradeRequests = mapper.readValue(file, new TypeReference<List<TradeRequest>>() {});
        }
    }

    private void persistTradeRequests() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(TRADE_REQUESTS_FILE_PATH), tradeRequests);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo", e);
        }
    }

    @Override
    public List<TradeRequest> getAllTradeRequests() {
        return tradeRequests;
    }

    @Override
    public List<TradeRequest> getAllTradeRequestsByUser(String email) {
        List<TradeRequest> tradeRequests = this.getAllTradeRequests();
        return tradeRequests.stream()
                .filter(t ->
                        email.equals(t.getFromUserEmail()) || email.equals(t.getToUserEmail())
                )
                .toList();
    }

//    @Override
//    public Optional<User> findById(String id) {
//        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
//    }

//    @Override
//    public void save(User user) {
//        Optional<User> existing = findByEmail(user.getEmail());
//        if (existing.isPresent()) {
//            users.remove(existing.get());
//        }
//        users.add(user);
//        persistTradeRequests(); // Guardar en el archivo
//    }

//    @Override
//    public List<UserAlbumDto> getUserAlbums(String email) {
//        Optional<User> user = users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
//        return user.isPresent() ? user.get().getAlbums() : new ArrayList<>();
//    }

}
