package com.swapapp.swapappmockserver.repository.user;

import com.swapapp.swapappmockserver.dto.User.UserAlbumDto;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    List<User> getAll();
    Optional<User> findByEmail(String email);
    void save(User user);
    Optional<User> findById(String id);
    List<UserAlbumDto> getUserAlbums(String email);
    void saveCards(Album album, String email);
}
