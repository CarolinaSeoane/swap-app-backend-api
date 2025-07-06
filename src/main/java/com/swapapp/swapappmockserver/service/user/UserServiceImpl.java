package com.swapapp.swapappmockserver.service.user;

import com.swapapp.swapappmockserver.dto.User.*;
import com.swapapp.swapappmockserver.model.Album;
import com.swapapp.swapappmockserver.model.User;
import com.swapapp.swapappmockserver.model.trades.PossibleTrade;
import com.swapapp.swapappmockserver.model.trades.StickerTrade;
import com.swapapp.swapappmockserver.repository.user.IUserRepository;
import com.swapapp.swapappmockserver.security.JwtUtil;
import com.swapapp.swapappmockserver.service.album.AlbumServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.swapapp.swapappmockserver.exceptions.EmailNotFoundException;
import com.swapapp.swapappmockserver.exceptions.IncorrectPasswordException;

import java.util.*;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private AlbumServiceImpl albumService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto register(UserRegisterDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado.");
        }

        User newUser = new User(
            UUID.randomUUID().toString(),
            dto.getEmail(),
            dto.getFullName(),
            dto.getUsername(),
            passwordEncoder.encode(dto.getPassword()),
            dto.getProfileImageUrl(),
            dto.getLocation(),
            dto.getShipping(),
            dto.getReputation(),
            dto.getAlbums(),
            dto.getFriends()
        );

        userRepository.save(newUser);

        String token = jwtUtil.generateToken(newUser.getEmail());

        return new LoginResponseDto(
            token,
            newUser.getEmail(),
            newUser.getFullName(),
            newUser.getUsername(),
            newUser.getAlbums(),
            newUser.getFriends()
        );
    }

    @Override
    public LoginResponseDto login(UserLoginDto dto) {
        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new EmailNotFoundException("El email no está registrado");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Contraseña incorrecta");
        }

        return new LoginResponseDto(
            jwtUtil.generateToken(user.getEmail()),
            user.getEmail(),
            user.getFullName(),
            user.getUsername(),
            user.getAlbums(),
            user.getFriends()
        );
    }

    @Override
    public UserDto getCurrentUser(String token) {
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .map(user -> new UserDto(
                        user.getEmail(),
                        user.getFullName(),
                        user.getUsername(),
                        user.getProfileImageUrl(),
                        user.getLocation(),
                        user.getShipping(),
                        user.getReputation(),
                        user.getAlbums(),
                        user.getFriends()
                ))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public UserDto getUserById(String id) {
        return userRepository.findById(id)
                .map(user -> new UserDto(
                        user.getEmail(),
                        user.getFullName(),
                        user.getUsername(),
                        user.getProfileImageUrl(),
                        user.getLocation(),
                        user.getShipping(),
                        user.getReputation(),
                        user.getAlbums(),
                        user.getFriends()
                ))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public List<PossibleTrade> findPossibleTrades(UserDto user, UserDto friend) {
        List<UserAlbumDto> myAlbums = user.getAlbums();
        List<UserAlbumDto> friendAlbums = friend.getAlbums();

        List<PossibleTrade> trades = new ArrayList<>();

        List<UserAlbumDto> matchingAlbums = friendAlbums.stream().filter(
                album -> myAlbums.stream().map(UserAlbumDto::getId).toList().contains(album.getId())
        ).toList();

        for (UserAlbumDto matchingAlbum : matchingAlbums) {
            Integer albumId = matchingAlbum.getId();

            UserAlbumDto myAlbum = myAlbums.stream()
                    .filter(album -> album.getId().equals(albumId))
                    .findFirst().orElseThrow();
            List<StickerTrade> myStickers = myAlbum.getStickers();
            List<StickerTrade> friendStickers = matchingAlbum.getStickers();

            List<Integer> toReceive = new ArrayList<>();
            List<Integer> toGive = new ArrayList<>();

            // friend can give me
            for (StickerTrade sticker : friendStickers) {
                Integer stickerNum = sticker.getNumber();
                Integer repeatCount = sticker.getRepeatCount();

                boolean haveSticker = myStickers.stream()
                        .anyMatch(mySticker -> mySticker.getNumber().equals(stickerNum));

                if (!haveSticker && repeatCount > 0) {
                    toReceive.add(stickerNum);
                }
            }

            // I can give friend
            for (StickerTrade sticker : myStickers) {
                Integer stickerNum = sticker.getNumber();
                Integer repeatCount = sticker.getRepeatCount();

                boolean friendHas = friendStickers.stream()
                        .anyMatch(friendSticker -> friendSticker.getNumber().equals(stickerNum));

                if (!friendHas && repeatCount > 0) {
                    toGive.add(stickerNum);
                }
            }

            // the trade is valid if they can give me something and I can give them something
            if (!toReceive.isEmpty() && !toGive.isEmpty()) {
                PossibleTrade trade = new PossibleTrade();
                Album album = albumService.getAlbum(String.valueOf(albumId));
                trade.setAlbum(albumId);
                trade.setAlbumName(album.getName());
                trade.setStickers(toReceive);
                trade.setFrom(friend);
                trade.setToGive(toGive);

                trades.add(trade);

            }
        }
        return trades;
    }

    @Override
    public List<PossibleTrade> getPossibleTrades(String token) {
        UserDto user = this.getCurrentUser(token);
        List<String> friends = user.getFriends();
        List<PossibleTrade> possibleTrades = new ArrayList<>();
        for (String id : friends) {
            UserDto friend = this.getUserById(id);
            List<PossibleTrade> trades = this.findPossibleTrades(user, friend);
            possibleTrades.addAll(trades);
        }

        return possibleTrades;
    }

    @Override
    public String saveProfileImage(String token, MultipartFile image) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String filename = UUID.randomUUID() + "-" + image.getOriginalFilename();
        String uploadDir = "uploads/profile-images";

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File dest = new File(uploadPath, filename);
    try {
            image.transferTo(dest);
        } catch (IOException | IllegalStateException e) {
            throw new RuntimeException("Error al guardar la imagen de perfil", e);
        }

        String imageUrl = "/images/" + filename;
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return imageUrl;
    }

    @Override
    public UserDto updateProfile(String token, UserDto dto) {
        String email = jwtUtil.extractEmail(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        userRepository.save(user);

        return new UserDto(
                user.getEmail(),
                user.getFullName(),
                user.getUsername(),
                user.getProfileImageUrl(),
                user.getLocation(),
                user.getShipping(),
                user.getReputation(),
                user.getAlbums(),
                user.getFriends()
        );
    }

    public void changePassword(String token, ChangePasswordDto dto) {
        String email = jwtUtil.extractUsername(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public UserAlbumDto getUserAlbumById(UserDto user, Integer albumId) {
        List<UserAlbumDto> myAlbums = user.getAlbums();

        return myAlbums.stream().filter(
                album -> album.getId().equals(albumId)
        ).toList().getFirst();
    }

    public Optional<StickerTrade> getStickerFromUserAlbum(UserAlbumDto album, Integer stickerNumber) {
        List<StickerTrade> stickers = album.getStickers();
        return Optional.ofNullable(stickers.stream().filter(
                sticker -> sticker.getNumber().equals(stickerNumber)
        ).toList().getFirst());
    }

    @Override
    public void addStickerToAlbum(UserDto user, Integer stickerNumber, Integer albumId) {
        UserAlbumDto album = this.getUserAlbumById(user, albumId);
        Optional<StickerTrade> sticker = this.getStickerFromUserAlbum(album, stickerNumber);

        if (sticker.isPresent()) {
            sticker.get().incrementRepeatCount();
        } else {
            StickerTrade newSticker = new StickerTrade(stickerNumber, 0);
            album.addSticker(newSticker);
        }
    }

    @Override
    public void removeStickerFromAlbum(UserDto user, Integer stickerNumber, Integer albumId) {
        UserAlbumDto album = this.getUserAlbumById(user, albumId);
        Optional<StickerTrade> sticker = this.getStickerFromUserAlbum(album, stickerNumber);

        sticker.ifPresent(StickerTrade::decrementRepeatCount);
    }

}
