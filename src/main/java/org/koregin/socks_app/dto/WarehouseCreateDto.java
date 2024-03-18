package org.koregin.socks_app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WarehouseCreateDto {

    private Integer quantity;
    private Long socksId;
}
