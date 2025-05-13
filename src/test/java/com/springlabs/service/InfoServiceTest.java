package com.springlabs.service;

import com.springlabs.model.Info;
import com.springlabs.model.User;
import com.springlabs.repository.dao.InfoDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InfoServiceTest {

    @Mock
    private InfoDao infoDao;

    @InjectMocks
    private InfoService infoService;

    private Info info;
    private User user;

    @BeforeEach
    void setUp() {
        info = new Info();
        info.setId(1);
        info.setEmails("test@example.com");
        info.setPhones("+1234567890");

        user = new User();
        user.setId(1);
        user.setName("John");
        user.setSurname("Doe");
    }

    @Test
    void testFindAll() {
        when(infoDao.findAll()).thenReturn(Collections.singletonList(info));

        List<Info> result = infoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(info, result.get(0));
        verify(infoDao, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(infoDao.findById(1)).thenReturn(Optional.of(info));

        Optional<Info> result = infoService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(info, result.get());
        verify(infoDao, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        when(infoDao.findById(2)).thenReturn(Optional.empty());

        Optional<Info> result = infoService.findById(2);

        assertFalse(result.isPresent());
        verify(infoDao, times(1)).findById(2);
    }

    @Test
    void testSave() {
        when(infoDao.save(any(Info.class))).thenReturn(info);

        Info result = infoService.save(info);

        assertNotNull(result);
        assertEquals(info, result);
        verify(infoDao, times(1)).save(info);
    }

    @Test
    void testDelete() {
        doNothing().when(infoDao).delete(1);

        infoService.delete(1);

        verify(infoDao, times(1)).delete(1);
    }

    @Test
    void testUpdate() {
        Info updatedInfo = new Info();
        updatedInfo.setId(1);
        updatedInfo.setEmails("new@example.com");
        updatedInfo.setPhones("+9876543210");

        when(infoDao.findById(1)).thenReturn(Optional.of(info));
        when(infoDao.save(any(Info.class))).thenReturn(updatedInfo);

        Info result = infoService.update(updatedInfo);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmails());
        assertEquals("+9876543210", result.getPhones());
        verify(infoDao, times(1)).findById(1);
        verify(infoDao, times(1)).save(any(Info.class));
    }

    @Test
    void testUpdateNotFound() {
        Info updatedInfo = new Info();
        updatedInfo.setId(2);

        when(infoDao.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            infoService.update(updatedInfo);
        });

        assertEquals("Информация по айди: 2 не найдена", exception.getMessage());
        verify(infoDao, times(1)).findById(2);
        verify(infoDao, never()).save(any(Info.class));
    }

    @Test
    void testExtractEmails() {
        String text = "Emails: test@example.com, user@domain.com";
        String result = infoService.extractEmails(text);

        assertEquals("test@example.com user@domain.com", result);
    }

    @Test
    void testExtractPhones() {
        String text = "Phones: +1234567890, +9876543210";
        String result = infoService.extractPhones(text);

        assertEquals("+1234567890, +9876543210", result);
    }
}