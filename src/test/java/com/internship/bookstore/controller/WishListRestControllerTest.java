package com.internship.bookstore.controller;


import com.internship.bookstore.api.controller.WishListRestController;
import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.service.UserService;
import com.internship.bookstore.service.WishListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.internship.TestConstants.ID_ONE;
import static com.internship.bookstore.utils.BookTestUtils.BOOK_RESPONSE_DTO_ONE;
import static com.internship.bookstore.utils.WishListTestUtils.WISH_LIST_REQUEST_DTO;
import static com.internship.it.controller.BaseController.createExpectedBody;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.internship.bookstore.utils.WishListTestUtils.WISH_LIST_RESPONSE_DTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static sun.plugin2.util.PojoUtil.toJson;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WishListRestController.class)
class WishListRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishListService wishListService;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser
    void shouldReturnUsersWishList() throws Exception{
            when(wishListService.getWishList(any(Long.class))).thenReturn(WISH_LIST_RESPONSE_DTO);


            mockMvc.perform(get("/wishlist/{id}",ID_ONE))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(createExpectedBody(WISH_LIST_RESPONSE_DTO)));

            verify(wishListService).getWishList(any(Long.class));
    }

    @Test
    @WithMockUser
    void shouldReturnErrorInvalidNumber() throws Exception{
        when(wishListService.getWishList(any(Long.class))).thenReturn(WISH_LIST_RESPONSE_DTO);

        mockMvc.perform(get("/wishlist/{id}",-3))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }


    @Test
    @WithMockUser
    void shouldCreateWishList() throws Exception{
        when(wishListService.save(any(WishListRequestDto.class))).thenReturn(WISH_LIST_RESPONSE_DTO);

        mockMvc.perform(post("/wishlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(WISH_LIST_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createExpectedBody(WISH_LIST_RESPONSE_DTO)));

        verify(wishListService).save(any(WishListRequestDto.class));
    }


    @Test
    @WithMockUser
    void shouldDeleteWishList() throws Exception{

        mockMvc.perform(delete("/wishlist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(WISH_LIST_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    @WithMockUser
    void shouldUpdateUser() throws Exception{
        when(wishListService.update(any(WishListRequestDto.class))).thenReturn(WISH_LIST_RESPONSE_DTO);

        mockMvc.perform(post("/wishlist/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(WISH_LIST_REQUEST_DTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(createExpectedBody(WISH_LIST_RESPONSE_DTO)));


    }

}
