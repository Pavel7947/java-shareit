package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.common.validation.ValidationAtCreate;
import ru.practicum.shareit.common.validation.ValidationAtUpdate;

@Data
public class UserDto {
    @NotBlank(groups = {ValidationAtCreate.class}, message = "Имя не может быть пустым")
    private String name;
    @Email(groups = {ValidationAtCreate.class, ValidationAtUpdate.class}, message = "Некорретный формат email")
    @NotBlank(groups = {ValidationAtCreate.class}, message = "Email не может быть пустым")
    private String email;
}
