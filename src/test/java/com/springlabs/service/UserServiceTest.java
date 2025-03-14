package com.springlabs.service;

import com.springlabs.exceptions.UserNotFoundException;
import com.springlabs.model.User;
import com.springlabs.repository.dao.UserDao;
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
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setName("John");
        user.setSurname("Doe");
    }

    @Test
    void testFindAll() {
        when(userDao.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testFindAllException() {
        when(userDao.findAll()).thenThrow(new RuntimeException("Ошибка базы данных"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.findAll();
        });

        assertEquals("Ошибка при получении пользователей", exception.getMessage());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testSave() {
        when(userDao.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testSaveInvalidUser() {
        user.setName(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.save(user);
        });

        assertEquals("Имя и фамилия пользователя не должны быть пустыми", exception.getMessage());
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testFindById() {
        when(userDao.findById(1)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userDao, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        when(userDao.findById(2)).thenReturn(Optional.empty());

        Optional<User> result = userService.findById(2);

        assertFalse(result.isPresent());
        verify(userDao, times(1)).findById(2);
    }

    @Test
    void testUpdate() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Jane");
        updatedUser.setSurname("Smith");

        when(userDao.findById(1)).thenReturn(Optional.of(user));
        when(userDao.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(updatedUser);

        assertNotNull(result);
        assertEquals("Jane", result.getName());
        assertEquals("Smith", result.getSurname());
        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).save(user);
    }

    @Test
    void testUpdateInvalidUser() {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName(null);
        updatedUser.setSurname("Smith");

        when(userDao.findById(1)).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.update(updatedUser);
        });

        assertEquals("Имя и фамилия пользователя не должны быть пустыми", exception.getMessage());
        verify(userDao, times(1)).findById(1);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setId(2);

        when(userDao.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.update(updatedUser);
        });

        assertEquals("Пользователь не найден с ID: 2", exception.getMessage());
        verify(userDao, times(1)).findById(2);
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void testSaveAll() {
        List<User> users = Collections.singletonList(user);
        when(userDao.saveAll(anyList())).thenReturn(users);

        List<User> result = userService.saveAll(users);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userDao, times(1)).saveAll(users);
    }

    @Test
    void testSaveAllInvalidUser() {
        user.setName(null);
        List<User> users = Collections.singletonList(user);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.saveAll(users);
        });

        assertEquals("Имя и фамилия пользователя не должны быть пустыми", exception.getMessage());
        verify(userDao, never()).saveAll(anyList());
    }

    @Test
    void testDelete() {
        when(userDao.findById(1)).thenReturn(Optional.of(user));
        doNothing().when(userDao).delete(1);

        userService.delete(1);

        verify(userDao, times(1)).findById(1);
        verify(userDao, times(1)).delete(1);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userDao.findById(2)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.delete(2);
        });

        assertEquals("Пользователь не найден с ID: 2", exception.getMessage());
        verify(userDao, times(1)).findById(2);
        verify(userDao, never()).delete(anyInt());
    }

    @Test
    void testFindByNameAndSurname() {
        when(userDao.findAll()).thenReturn(Collections.singletonList(user));

        List<User> result = userService.findByNameAndSurname("John", "Doe");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testFindByNameAndSurnameNotFound() {
        when(userDao.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.findByNameAndSurname("Jane", "Smith");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userDao, times(1)).findAll();
    }
}