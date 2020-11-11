package com.internship.bookstore.utils;

import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.api.dto.WishListResponseDto;
import com.internship.bookstore.model.WishList;
import static com.internship.TestConstants.*;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_ONE;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_TWO;
import static com.internship.bookstore.utils.UserTestUtil.USER_ONE;
import static com.internship.bookstore.utils.UserTestUtil.USER_TWO;

public class WishListTestUtils {

    public static final WishListResponseDto WISH_LIST_RESPONSE_DTO = WishListResponseDto
            .builder()
            .userEmail(AUTH_USER_EMAIL)
            .bookTitle(BOOK_TITLE_ONE)
            .build();

    public static final WishListRequestDto WISH_LIST_REQUEST_DTO= WishListRequestDto
            .builder()
            .id(ID_ONE)
            .bookId(BOOK_ONE.getId())
            .userId(USER_ONE.getId())
            .build();

    public static final WishListRequestDto WISH_LIST_REQUEST_DTO_TWO= WishListRequestDto
            .builder()
            .id(ID_ONE)
            .bookId(BOOK_TWO.getId())
            .userId(USER_ONE.getId())
            .build();


    public static final WishList WISH_LIST_ONE = WishList.builder()
            .book(BOOK_ONE)
            .user(USER_ONE)
            .id(ID_ONE)
            .build();

    public static final WishList WISH_LIST_TWO = WishList.builder()
            .book(BOOK_ONE)
            .user(USER_TWO)
            .id(ID_TWO)
            .build();


}
