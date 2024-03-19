package org.koregin.socks_app.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksIncomeCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.koregin.socks_app.facade.SocksFacade;
import org.koregin.socks_app.service.SocksService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksFacade socksFacade;
    private final SocksService socksService;

    @GetMapping
    public Integer countByParams(@RequestParam String color,
                                        @RequestParam Integer cottonPart,
                                        @RequestParam String operation) {
        log.info("Request socks with Color: {}, cottonPart: {}, operation: {}", color, cottonPart, operation);
        return socksFacade.countByParams(color, cottonPart, operation);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocksReadDto createSocks(@Validated @RequestBody SocksCreateDto socksCreateDto) {
        return socksService.create(socksCreateDto);
    }

    @PutMapping("/{id}")
    public SocksReadDto updateSocks(@PathVariable("id") Long id,
                               @Validated @RequestBody SocksCreateDto socksCreateDto) {
        return socksService.update(id, socksCreateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createIncome(@RequestBody IncomeCreateDto incomeCreateDto) {
       return socksFacade.createIncome(incomeCreateDto);
    }

    @PostMapping("/income/add")
    @ResponseStatus(HttpStatus.OK)
    public void addSocksToIncome(@RequestBody SocksIncomeCreateDto socksIncomeCreateDto) {
        socksFacade.addSocksToIncome(socksIncomeCreateDto);
    }

    @PostMapping("/income/accept/{incomeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptIncome(@PathVariable("incomeId") Long incomeId) {
        socksFacade.acceptIncome(incomeId);
    }
}
