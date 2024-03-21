package org.koregin.socks_app.http.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.koregin.socks_app.dto.*;
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
    public SocksResponseDto createSocks(@Validated @RequestBody SocksRequestDto socksRequestDto) {
        return socksService.create(socksRequestDto);
    }

    @PutMapping("/{id}")
    public SocksResponseDto updateSocks(@PathVariable("id") Long id,
                                        @Validated @RequestBody SocksRequestDto socksRequestDto) {
        return socksService.update(id, socksRequestDto);
    }

    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createIncome(@RequestBody IncomeRequestDto incomeRequestDto) {
       return socksFacade.createIncome(incomeRequestDto);
    }

    @PostMapping("/income/add")
    @ResponseStatus(HttpStatus.OK)
    public void addSocksToIncome(@RequestBody SocksIncomeRequestDto socksIncomeRequestDto) {
        socksFacade.addSocksToIncome(socksIncomeRequestDto);
    }

    @PostMapping("/income/accept/{incomeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptIncome(@PathVariable("incomeId") Long incomeId) {
        socksFacade.acceptIncome(incomeId);
    }

    @PostMapping("/outcome")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createOutcome(@RequestBody OutcomeRequestDto outcomeRequestDto) {
        return socksFacade.createOutcome(outcomeRequestDto);
    }

    @PostMapping("/outcome/add")
    @ResponseStatus(HttpStatus.OK)
    public void addSocksToOutcome(@RequestBody SocksOutcomeRequestDto socksOutcomeRequestDto) {
        socksFacade.addSocksToOutcome(socksOutcomeRequestDto);
    }

    @PostMapping("/outcome/accept/{outcomeId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void acceptOutcome(@PathVariable("outcomeId") Long outcomeId) {
        socksFacade.acceptOutcome(outcomeId);
    }

}
