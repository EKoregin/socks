package org.koregin.socks_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SocksResponseDto {

    Long id;
    String name;
    String color;
    Integer cottonPart;
}
