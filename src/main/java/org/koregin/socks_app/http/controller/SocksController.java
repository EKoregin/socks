package org.koregin.socks_app.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.koregin.socks_app.dto.IncomeCreateDto;
import org.koregin.socks_app.dto.SocksCreateDto;
import org.koregin.socks_app.dto.SocksIncomeCreateDto;
import org.koregin.socks_app.dto.SocksReadDto;
import org.koregin.socks_app.service.SocksServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/socks")
public class SocksController {

    private final SocksServiceImpl sockService;

    @GetMapping
    public Integer countByParams(@RequestParam String color,
                                        @RequestParam Integer cottonPart,
                                        @RequestParam String operation) throws BadRequestException {
        log.info("Request socks with Color: {}, cottonPart: {}, operation: {}", color, cottonPart, operation);
        return sockService.countByParams(color, cottonPart, operation);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocksReadDto create(@Validated @RequestBody SocksCreateDto socksCreateDto) {
        return sockService.create(socksCreateDto);
    }

    @PutMapping("/{id}")
    public SocksReadDto update(@PathVariable("id") Long id,
                               @Validated @RequestBody SocksCreateDto socksCreateDto) {
        return sockService.update(id, socksCreateDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /*
    Create new empty income
     */
    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public Long incomeCreate(@RequestBody IncomeCreateDto incomeCreateDto) {
       return sockService.incomeCreate(incomeCreateDto);
    }

    /*
    Add socks to income
     */
    @PostMapping("/income/add")
    @ResponseStatus(HttpStatus.OK)
    public void addSocksToIncome(@RequestBody SocksIncomeCreateDto socksIncomeCreateDto) {
        sockService.addSocksToIncome(socksIncomeCreateDto);
    }

    @PostMapping("/income/accept/{incomeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptIncome(@PathVariable("incomeId") Long incomeId) {
        sockService.acceptIncome(incomeId);
    }
}
