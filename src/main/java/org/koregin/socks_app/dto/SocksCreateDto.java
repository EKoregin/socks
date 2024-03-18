package org.koregin.socks_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class SocksCreateDto {

    @NotBlank
    String name;

    @NotBlank
    @Size(min = 3, max = 16)
    String color;

    @Range(min = 0, max = 100)
    Integer cottonPart;
}
