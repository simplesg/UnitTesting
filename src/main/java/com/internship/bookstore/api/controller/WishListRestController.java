package com.internship.bookstore.api.controller;

import com.internship.bookstore.api.dto.WishListRequestDto;
import com.internship.bookstore.api.dto.WishListResponseDto;
import com.internship.bookstore.api.exchange.Response;
import com.internship.bookstore.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Objects;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishListRestController {

    private final WishListService wishListService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUsersWishList(@PathVariable("id") Long id) {
        if (id < 0) {
            return ResponseEntity.badRequest().body(Response.build("Invalid number entered"));
        }
        return ResponseEntity.ok(Response.build(wishListService.getWishList(id)));
    }

    @PostMapping
    public ResponseEntity<Response<WishListResponseDto>> createWishList(
            @RequestBody @Valid WishListRequestDto wishListRequestDto,
            Errors validationErrors) {

        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }
        return ok(Response.build(wishListService.save(wishListRequestDto)));
    }

    @DeleteMapping()
    public ResponseEntity<Object> deleteFromWishList(
            @RequestBody @Valid WishListRequestDto wishListRequestDto,
            Errors validationErrors) {
        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }

        wishListService.delete(wishListRequestDto);
        return ResponseEntity.ok().body(Response.build("WishList restored"));
    }


    @PostMapping("/update")
    public ResponseEntity<Response<WishListResponseDto>> updateWishList(
            @RequestBody @Valid WishListRequestDto wishListRequestDto,
            Errors validationErrors) {
        if (validationErrors.hasErrors()) {
            throw new ValidationException(Objects.requireNonNull(validationErrors.getFieldError()).getDefaultMessage());
        }
        return ok(Response.build(wishListService.update(wishListRequestDto)));
    }
}
