package org.koregin.socks_app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.koregin.socks_app.database.entity.Socks;
import org.koregin.socks_app.database.repository.SocksRepository;
import org.koregin.socks_app.dto.SocksRequestDto;
import org.koregin.socks_app.dto.SocksResponseDto;
import org.koregin.socks_app.mapper.SocksMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocksServiceImplTest {

    private static final Long SOCKS_ID = 99L;

    @Mock
    private SocksRepository socksRepository;

    @Mock
    private SocksMapper socksMapper;

    @InjectMocks
    private SocksServiceImpl socksService;

    @Test
    void create() {
        SocksRequestDto requestDto = new SocksRequestDto("Socks", "BLACK", 50);
        Socks newSocks = new Socks(null, "Socks", "BLACK", 50);
        Socks savedSocks = new Socks(SOCKS_ID, "Socks", "BLACK", 50);
        SocksResponseDto responseDto = new SocksResponseDto(SOCKS_ID, "Socks", "BLACK", 50);

        when(socksRepository.save(newSocks)).thenReturn(savedSocks);
        when(socksMapper.dtoToSocks(requestDto)).thenReturn(newSocks);
        when(socksMapper.socksToDto(savedSocks)).thenReturn(responseDto);

        var actualResult = socksService.create(requestDto);
        assertEquals(responseDto, actualResult);
    }

    @Test
    void updateWhenSocksFound() {
        Socks savedSocks = new Socks(SOCKS_ID, "Socks", "BLACK", 50);
        Socks updatedSocks = new Socks(SOCKS_ID, "Socks", "BLACK", 90);
        SocksRequestDto requestDto = new SocksRequestDto("Socks", "BLACK", 90);
        SocksResponseDto responseDto = new SocksResponseDto(SOCKS_ID, "Socks", "BLACK", 90);

        when(socksRepository.findById(SOCKS_ID)).thenReturn(Optional.of(savedSocks));
        when(socksMapper.update(requestDto, savedSocks)).thenReturn(updatedSocks);
        when(socksRepository.saveAndFlush(updatedSocks)).thenReturn(updatedSocks);
        when(socksMapper.socksToDto(updatedSocks)).thenReturn(responseDto);

        var actualResult = socksService.update(SOCKS_ID, requestDto);
        assertEquals(responseDto, actualResult);
    }

    @Test
    void updateWhenSocksNotFound() {
        SocksRequestDto requestDto = new SocksRequestDto("Socks", "BLACK", 90);
        var exception = assertThrows(ResponseStatusException.class, () -> socksService.update(SOCKS_ID, requestDto));
        assertThat(exception.getMessage()).isEqualTo("404 NOT_FOUND");
    }
}