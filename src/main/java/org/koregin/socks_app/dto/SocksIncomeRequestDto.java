package org.koregin.socks_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocksIncomeRequestDto {

    private Integer quantity;
    private Long socksId;
    private Long incomeId;
}
