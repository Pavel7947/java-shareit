package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.ValidationAtCreate;
import ru.practicum.shareit.validation.ValidationAtUpdate;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private long id;
    @NotBlank(groups = {ValidationAtCreate.class}, message = "Имя не может быть пустым")
    private String name;
    @Email(groups = {ValidationAtCreate.class, ValidationAtUpdate.class}, message = "Некорретный формат email")
    @NotBlank(groups = {ValidationAtCreate.class}, message = "Email не может быть пустым")
    private String email;
}
