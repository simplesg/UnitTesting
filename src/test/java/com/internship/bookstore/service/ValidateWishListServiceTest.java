package com.internship.bookstore.service;

import com.internship.bookstore.api.dto.WishListRequestDto;

import com.internship.bookstore.model.WishList;
import com.internship.bookstore.repository.WishListRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static com.internship.bookstore.utils.WishListTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateWishListServiceTest {

    @Mock
    private WishListRepository wishListRepository;

    @InjectMocks
    private ValidateWishListService validateWishListService;


    @Test
    void shouldReturnTrueUserHasNoBooksInWishList(){
        when(wishListRepository.getWishListByUser_Id(any(Long.class))).thenReturn(Collections.emptyList());
        assertTrue(validateWishListService.validate(WISH_LIST_REQUEST_DTO_TWO));

        verify(wishListRepository).getWishListByUser_Id(any(Long.class));
    }

    @Test
    void shouldReturnFalseUserAlreadyHasBookInWishList(){
        when(wishListRepository.getWishListByUser_Id(any(Long.class))).thenReturn(Collections.singletonList(WISH_LIST_ONE));
        assertFalse(validateWishListService.validate(WISH_LIST_REQUEST_DTO));

        verify(wishListRepository).getWishListByUser_Id(any(Long.class));
    }
}
