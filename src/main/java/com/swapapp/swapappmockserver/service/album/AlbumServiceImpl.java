package com.swapapp.swapappmockserver.service.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swapapp.swapappmockserver.dto.Album.AlbumCategoryCountDto;
import com.swapapp.swapappmockserver.dto.User.UserAlbumDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.trades.StickerTrade;
import com.swapapp.swapappmockserver.model.trades.TradingCard;
import com.swapapp.swapappmockserver.repository.album.IAlbumRepository;

import com.swapapp.swapappmockserver.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AlbumServiceImpl implements IAlbumService {

    @Autowired
    private IAlbumRepository albumRepository;

    @Autowired
    private IUserRepository usuarioRepository;


    ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<AlbumCategoryCountDto> getAlbumCountByCategory() {
        List<Album> albums = albumRepository.getAlbums();
        if (albums.isEmpty()){
            //
        }

        return albums.stream().collect(Collectors.groupingBy(Album::getCategory, Collectors.counting()))
                .entrySet().stream()
                .map(entry ->
                        new AlbumCategoryCountDto(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Album> getAlbumsCategory(String category) {
        List<Album> albums = albumRepository.getAlbums();
        if (albums.isEmpty()){
            return new ArrayList<>();
        }

        return albums.stream().filter(album -> category.equals(album.getCategory().toString())).collect(Collectors.toList());
    }

    @Override
    public Album getAlbum(String albumId) {
        List<Album> albums = albumRepository.getAlbums();
        if (albums.isEmpty()){
            //
        }

        return albums.stream().filter(album -> albumId.equals(album.getId().toString())).toList().getFirst();
    }

    @Override
    public List<Album> getAlbums() {
        return albumRepository.getAlbums();
    }

    @Override
    public List<Album> getUserAlbums(String email) {
        List<UserAlbumDto> userAlbumDtos = usuarioRepository.getUserAlbums(email);
        return userAlbumDtos.stream().map(this::mapToAlbum).toList();
    }

    private Album mapToAlbum(UserAlbumDto userAlbumDto) {
        Album fullAlbum = getAlbum(userAlbumDto.getId().toString());

        List<TradingCard> tradingCards = userAlbumDto.getStickers().stream().map(stickerTrade -> mapToTradingCard(stickerTrade, fullAlbum.getAlbumId())).toList();

        Album album = new Album();
        album.setCategory(fullAlbum.getCategory());
        album.setAlbumId(fullAlbum.getAlbumId());
        album.setId(fullAlbum.getId());
        album.setName(fullAlbum.getName());
        album.setFinished(fullAlbum.getFinished());
        album.setCover(fullAlbum.getCover());
        album.setReleaseYear(fullAlbum.getReleaseYear());
        album.setTotalCards(fullAlbum.getTotalCards());
        album.setTradingCards(tradingCards);

        return album;
    }

    private TradingCard mapToTradingCard(StickerTrade sticker, String albumId){
        TradingCard tradingCard = new TradingCard();
        tradingCard.setAlbumId(albumId);
        tradingCard.setObtained(sticker.getObtained());
        tradingCard.setNumber(sticker.getNumber());
        tradingCard.setRepeatedQuantity(sticker.getRepeatCount());

        return tradingCard;
    }

    @Override
    public Album getUserAlbum(String albumId, String email) {
        List<UserAlbumDto> albums = usuarioRepository.getUserAlbums(email);
        if (albums.isEmpty()){
            //
        }

        return albums.stream().filter(album -> albumId.equals(album.getId().toString())).map(this::mapToAlbum).toList().getFirst();
    }

    @Override
    public List<Album> getUserAlbumsCategory(String category, String email) {
        List<Album> albums = getUserAlbums(email);
        if (albums.isEmpty()){
            return new ArrayList<>();
        }

        return albums.stream().filter(album -> category.equals(album.getCategory().toString())).toList();
    }
}