package com.internship.bookstore.service;

import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.model.WishList;
import com.internship.bookstore.repository.BookRepository;
import com.internship.bookstore.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateWishListService {

    private final WishListRepository wishListRepository;


    public boolean validate(WishListRequestDto wishListRequestDto){
        List<WishList> usersWish = wishListRepository.getWishListByUser_Id(wishListRequestDto.getUserId());

        if(usersWish.isEmpty()){
            log.info("Validated!   User with id [{}] has no books in wishList",wishListRequestDto.getUserId());
            return true;
        }
        log.warn("Attention!   User with id [{}] already has a book with id [{}] in his wishList",wishListRequestDto.getUserId(),usersWish.stream().findFirst().get().getBook().getId());
        return false;
    }
}
