package com.internship.bookstore.service;

import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.api.dto.WishListResponseDto;
import com.internship.bookstore.model.WishList;
import com.internship.bookstore.repository.BookRepository;
import com.internship.bookstore.repository.WishListRepository;
import com.internship.bookstore.utils.exceptions.RecordNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Optional;
import static com.internship.TestConstants.ID_ONE;
import static com.internship.TestConstants.ID_TWO;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_ONE;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_TWO;
import static com.internship.bookstore.utils.UserTestUtil.USER_ONE;
import static com.internship.bookstore.utils.WishListTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WishListServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserService userService;
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private ValidateWishListService validateWishListService;

    @InjectMocks
    private WishListService wishListService;

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(wishListService,"messageBookNotFound",
                "Book with id %s was not found");
        ReflectionTestUtils.setField(wishListService,"wishListAlreadyExists",
                "User with id %s already has a book in wishlist");
    }


    @AfterEach
    void tearDown(){
        Mockito.verifyNoMoreInteractions(bookRepository,wishListRepository);
    }


    @Test
    void shouldAddBookToWishList(){
        final WishListResponseDto expectedResponseDto = WISH_LIST_RESPONSE_DTO;

        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        when(validateWishListService.validate(any(WishListRequestDto.class))).thenReturn(true);
        when(userService.getUser()).thenReturn(USER_ONE);
        when(wishListRepository.save(any(WishList.class))).thenReturn(WISH_LIST_ONE);

        final WishListResponseDto actualResponseDto = wishListService.save(WISH_LIST_REQUEST_DTO);

        assertAll(
                () -> assertEquals(expectedResponseDto.getBookTitle(),
                        actualResponseDto.getBookTitle()),
                () -> assertEquals(expectedResponseDto.getUserEmail(), actualResponseDto.getUserEmail())
        );

        verify(wishListRepository, times(1)).save(any(WishList.class));
        verify(validateWishListService,times(1)).validate(any(WishListRequestDto.class));
    }

    @Test
    void shouldNotAddToWishList(){
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> wishListService.save(WISH_LIST_REQUEST_DTO));
    }

    @Test
    void shouldThrowUserHadAnotherBook(){
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        when(validateWishListService.validate(WISH_LIST_REQUEST_DTO)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> wishListService.save(WISH_LIST_REQUEST_DTO));
    }

    @Test
    void shouldUpdateBook(){
        final WishListResponseDto expectedResponseDto = WISH_LIST_RESPONSE_DTO;

        when(bookRepository.findBookById(ID_TWO)).thenReturn(Optional.of(BOOK_TWO));
        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.of(WISH_LIST_ONE));
        when(userService.getUser()).thenReturn(USER_ONE);
        when(wishListRepository.save(any(WishList.class))).thenReturn(WISH_LIST_ONE);

        final WishListResponseDto actualResponseDto = wishListService.update(WISH_LIST_REQUEST_DTO_TWO);

        assertAll(
                () -> assertEquals(expectedResponseDto.getBookTitle(),actualResponseDto.getBookTitle()),
                () -> assertEquals(expectedResponseDto.getUserEmail(), actualResponseDto.getUserEmail())
        );

        verify(wishListRepository, times(1)).save(any(WishList.class));
        verify(wishListRepository, times(1)).findById(any(Long.class));

    }


    @Test
    void shouldThrowBookNotFoundOnUpdate(){
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> wishListService.update(WISH_LIST_REQUEST_DTO));
    }

    @Test
    void shouldThrowNotExistentWishListOnUpdate(){
        when(bookRepository.findBookById(ID_ONE)).thenReturn(Optional.of(BOOK_ONE));
        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> wishListService.update(WISH_LIST_REQUEST_DTO));
    }


    @Test
    void shouldDeleteWishList(){
        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.of(WISH_LIST_ONE));

        wishListService.delete(WISH_LIST_REQUEST_DTO);

        verify(wishListRepository, times(1)).findById(any(Long.class));
        verify(wishListRepository, times(1)).delete(WISH_LIST_ONE);
    }


    @Test
    void shouldThrowWishListNotFoundOnDelete(){
        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> wishListService.delete(WISH_LIST_REQUEST_DTO));
    }

    @Test
    void shouldGetUsersWishList(){
        final WishListResponseDto expectedResponseDto = WISH_LIST_RESPONSE_DTO;

        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.of(WISH_LIST_ONE));

        final WishListResponseDto actualResponse = wishListService.getWishList(ID_ONE);
        assertAll(
                () -> assertEquals(expectedResponseDto.getBookTitle(),actualResponse.getBookTitle()),
                () -> assertEquals(expectedResponseDto.getUserEmail(), actualResponse.getUserEmail())
        );
        verify(wishListRepository,times(1)).findById(any(Long.class));
    }

    @Test
    void shouldThrowWishListNotFoundOnGet(){
        when(wishListRepository.findById(ID_ONE)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> wishListService.getWishList(ID_ONE));
    }
}
