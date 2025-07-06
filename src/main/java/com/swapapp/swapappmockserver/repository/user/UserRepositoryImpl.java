package com.swapapp.swapappmockserver.repository.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.Album.AlbumDto;
import com.swapapp.swapappmockserver.dto.User.UserAlbumDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.User;
import com.swapapp.swapappmockserver.model.trades.StickerTrade;
import com.swapapp.swapappmockserver.model.trades.TradingCard;
import com.swapapp.swapappmockserver.repository.album.IAlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private static final String USER_FILE_PATH = "data/users.json";
    private List<User> users = new ArrayList<>();

    @Autowired
    private IAlbumRepository albumRepository;

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
    public Optional<User> findById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
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

    @Override
    public List<UserAlbumDto> getUserAlbums(String email) {
        Optional<User> user = users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
        return user.isPresent() ? user.get().getAlbums() : new ArrayList<>();
    }

    @Override
    public void saveCards(Album album, String email) {
        List<UserAlbumDto> userAlbumDtos = getUserAlbums(email);
        Optional<User> user = findByEmail(email);

        Optional<UserAlbumDto> userAlbumDto = userAlbumDtos.stream().filter(userAlbumDto1 -> userAlbumDto1.getId().equals(album.getId())).findFirst();

        userAlbumDto.ifPresent(albumDto -> isStickerPresent(albumDto, album));

        if(user.isPresent()){
            users.remove(user.get());
            users.add(user.get());
        }
        persistUsers();
    }


    private void updateSticker (StickerTrade sticker, List<TradingCard> tradingCards){
        Optional<TradingCard> tradingCard = tradingCards.stream().filter(tradingCard1 -> tradingCard1.getNumber().equals(sticker.getNumber())).findFirst();
        tradingCard.ifPresent(card -> sticker.setRepeatCount(card.getRepeatedQuantity()));
    }

    private void isStickerPresent(UserAlbumDto albumDto, Album album){
        List<TradingCard> tradingCardsToAdd = album.getTradingCards().stream().filter(tradingCard -> tradingCard.getRepeatedQuantity() > 0 && !tradingCard.getObtained()).toList();
        albumDto.getStickers().forEach(stickerTrade -> updateSticker(stickerTrade, album.getTradingCards()));

        List<StickerTrade> stickerTrades = tradingCardsToAdd.stream().map(tradingCard -> new StickerTrade(tradingCard.getNumber(), tradingCard.getRepeatedQuantity())).toList();

        for(StickerTrade sticker : stickerTrades){
            albumDto.getStickers().add(sticker);
        }
    }
}
