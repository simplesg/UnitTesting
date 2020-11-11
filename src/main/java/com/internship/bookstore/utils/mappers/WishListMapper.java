package com.internship.bookstore.utils.mappers;

import com.internship.bookstore.api.dto.WishListResponseDto;
import com.internship.bookstore.model.WishList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WishListMapper {

    public static final Function<WishList, WishListResponseDto> mapWishListToWishListResponseDto =
            wishList -> WishListResponseDto.builder()
                    .bookTitle(wishList.getBook().getTitle())
                    .userEmail(wishList.getUser().getEmail())
                    .build();
}

