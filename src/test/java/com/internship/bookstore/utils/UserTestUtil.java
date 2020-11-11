package com.internship.bookstore.utils;

import com.internship.bookstore.model.Book;
import com.internship.bookstore.model.User;

import static com.internship.TestConstants.*;
import static com.internship.TestConstants.BOOK_TITLE_ONE;
import static com.internship.bookstore.utils.AuthorTestUtils.AUTHOR_ONE;

public class UserTestUtil {

    public static final User USER_ONE = User.builder()
            .id(ID_ONE)
            .email(AUTH_USER_EMAIL)
            .password(AUTH_USER_PASSWORD)
            .build();

    public static final User USER_TWO = User.builder()
            .id(ID_TWO)
            .email(AUTH_USER_EMAIL)
            .password(AUTH_USER_PASSWORD)
            .build();
}
