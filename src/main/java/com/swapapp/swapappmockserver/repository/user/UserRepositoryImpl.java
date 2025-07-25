package com.swapapp.swapappmockserver.repository.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        System.out.println("Inicializando UserRepositoryImpl...");
        loadUsers();
        System.out.println("UserRepositoryImpl inicializado con " + users.size() + " usuarios");
    }

    private void loadUsers() throws IOException {
        File file = new File(USER_FILE_PATH);
        ObjectMapper mapper = new ObjectMapper();

        System.out.println("Cargando usuarios desde: " + file.getAbsolutePath());
        System.out.println("Archivo existe: " + file.exists());

        if (!file.exists()) {
            // Crear archivo vacío solo si no existe
            System.out.println("Creando archivo de usuarios vacío...");
            file.getParentFile().mkdirs(); // Crea carpeta 'data' si no existe
            file.createNewFile();
            users = new ArrayList<>();
            // NO persistir inmediatamente para evitar sobreescribir datos existentes
        } else {
            try {
                System.out.println("Leyendo usuarios del archivo...");
            users = mapper.readValue(file, new TypeReference<List<User>>() {});
                if (users == null) {
                    users = new ArrayList<>();
                }
                System.out.println("Usuarios cargados: " + users.size());
                for (User user : users) {
                    System.out.println("  - " + user.getEmail());
                }
            } catch (IOException e) {
                System.err.println("Error al cargar usuarios, creando lista vacía: " + e.getMessage());
                users = new ArrayList<>();
            }
        }
    }

    private void persistUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            System.out.println("Persistiendo " + users.size() + " usuarios en " + USER_FILE_PATH);
            mapper.writeValue(new File(USER_FILE_PATH), users);
            System.out.println("Usuarios guardados exitosamente");
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
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
        System.out.println("Guardando usuario: " + user.getEmail() + " (Total actual: " + users.size() + ")");
        Optional<User> existing = findByEmail(user.getEmail());
        if (existing.isPresent()) {
            System.out.println("Usuario existente encontrado, reemplazando...");
            users.remove(existing.get());
        }
        users.add(user);
        System.out.println("Usuario agregado. Total ahora: " + users.size());
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

        userAlbumDto.ifPresent(albumDto -> updateSticker(albumDto, album));

        if(user.isPresent()){
            users.remove(user.get());
            users.add(user.get());
        }
        persistUsers();
    }


    private Optional<TradingCard> isStickerPresent(StickerTrade sticker, List<TradingCard> tradingCards){
        return tradingCards.stream().filter(tradingCard1 -> tradingCard1.getNumber().equals(sticker.getNumber())).findFirst();
    }

    private void updateSticker(UserAlbumDto albumDto, Album album){
        List<TradingCard> tradingCardsToAdd = album.getTradingCards().stream().filter(tradingCard -> tradingCard.getRepeatedQuantity() > 0 || (!userHasIt(tradingCard, albumDto.getStickers()) && tradingCard.getObtained())).toList();
        albumDto.getStickers().forEach(stickerTrade -> {
            Optional<TradingCard> tradingCard = isStickerPresent(stickerTrade, album.getTradingCards());
            tradingCard.ifPresent(card -> stickerTrade.setRepeatCount(card.getRepeatedQuantity()));
        });

        List<StickerTrade> stickerTradesToAdd = tradingCardsToAdd.stream().map(tradingCard -> !userHasIt(tradingCard, albumDto.getStickers())? new StickerTrade(tradingCard.getNumber(), tradingCard.getRepeatedQuantity()) : null).toList();
        for(StickerTrade sticker : stickerTradesToAdd){
            if(sticker != null){
                albumDto.getStickers().add(sticker);
            }

        }

        List<TradingCard> tradingCardsToRemove = album.getTradingCards().stream().filter(tradingCard -> !tradingCard.getObtained() && userHasIt(tradingCard, albumDto.getStickers())).toList();
        List<StickerTrade> stickerTradesToRemove = tradingCardsToRemove.stream().map(tradingCard -> new StickerTrade(tradingCard.getNumber(), tradingCard.getRepeatedQuantity())).toList();

        for(StickerTrade sticker : stickerTradesToRemove){
            albumDto.getStickers().remove(sticker);
        }
    }

    private Boolean userHasIt(TradingCard tradingCard, List<StickerTrade> userTradingCards){
        return userTradingCards.stream().anyMatch(sticker -> sticker.getNumber().equals(tradingCard.getNumber()));
    }
}
