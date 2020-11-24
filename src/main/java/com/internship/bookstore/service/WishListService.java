package com.internship.bookstore.service;

import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.api.dto.WishListResponseDto;
import com.internship.bookstore.model.Book;
import com.internship.bookstore.model.WishList;
import com.internship.bookstore.repository.BookRepository;
import com.internship.bookstore.repository.WishListRepository;
import com.internship.bookstore.utils.exceptions.RecordAlreadyAssigned;
import com.internship.bookstore.utils.exceptions.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.bookstore.utils.mappers.WishListMapper.mapWishListToWishListResponseDto;
import static java.lang.String.format;


@Service
@RequiredArgsConstructor
@Slf4j
public class WishListService {
    private final BookRepository bookRepository;
    private final UserService userService;
    private final WishListRepository wishListRepository;
    private final ValidateWishListService validateWishListService;

    @Value("Book with id %s was not found")
    private String messageBookNotFound;

    @Value("${message.wishlist.not-found}")
    private String wishListAlreadyExists;

    @Value("${message.wishlist.never-created}")
    private String wishListNeverCreated;

    @Transactional
    public WishListResponseDto save(WishListRequestDto wishListRequestDto) {
        log.warn("Saving the book with id [{}] in the wishlist", wishListRequestDto.getBookId());

        Book book = bookRepository.findBookById(wishListRequestDto.getBookId()).orElseThrow(() -> {
            log.warn(wishListAlreadyExists, wishListRequestDto.getBookId());
            return new RecordNotFoundException(format(messageBookNotFound, wishListRequestDto.getBookId()));
        });
        if (validateWishListService.validate(wishListRequestDto)) {
            WishList wishList = new WishList();
            wishList.setBook(book);
            wishList.setUser(userService.getUser());

            wishList = wishListRepository.save(wishList);

            return mapWishListToWishListResponseDto.apply(wishList);
        }
        log.warn("User with id [{}] already has a book in wishlist", wishListRequestDto.getUserId());
        throw new RecordAlreadyAssigned(format(wishListAlreadyExists, wishListRequestDto.getUserId()));
    }

    @Transactional
    public WishListResponseDto update(WishListRequestDto wishListRequestDto) {
        log.warn("Updating the book with id [{}] for user [{}] in the wishlist", wishListRequestDto.getBookId(), wishListRequestDto.getUserId());

        Book book = bookRepository.findBookById(wishListRequestDto.getBookId()).orElseThrow(() -> {
            log.warn("Book with id [{}] was not found in the database", wishListRequestDto.getBookId());
            return new RecordNotFoundException(format(messageBookNotFound, wishListRequestDto.getBookId()));
        });

        WishList wishList = wishListRepository.findById(wishListRequestDto.getId()).orElseThrow(() -> {
            log.warn(wishListNeverCreated, wishListRequestDto.getBookId());
            return new RecordAlreadyAssigned(format(wishListNeverCreated, wishListRequestDto.getBookId()));
        });
        wishList.setBook(book);
        wishList.setUser(userService.getUser());

        wishList = wishListRepository.save(wishList);

        return mapWishListToWishListResponseDto.apply(wishList);
    }

    @Transactional
    public void delete(WishListRequestDto wishListRequestDto){
        log.warn("Resetting the wish list of user with id [{}]",wishListRequestDto.getUserId());

        WishList wishList = wishListRepository.findById(wishListRequestDto.getId()).orElseThrow(() -> {
            log.warn(wishListNeverCreated, wishListRequestDto.getBookId());
            return new RecordNotFoundException(format(wishListNeverCreated, wishListRequestDto.getBookId()));
        });

        wishListRepository.delete(wishList);
        log.warn("User's wishlist was reset successfully");
    }


    @Transactional(readOnly = true)
    public WishListResponseDto getWishList(Long id) {
        log.warn("Getting your wishList from database");

        WishList wishList = wishListRepository.findById(id).orElseThrow(() -> {
            log.warn(wishListNeverCreated, id);
            return new RecordNotFoundException(format(wishListNeverCreated, id));
        });

        return mapWishListToWishListResponseDto.apply(wishList);
    }
}
