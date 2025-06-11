package com.animal.persona.service;

import com.animal.persona.model.Users;
import com.animal.persona.repository.UsersRepository;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UsersRepository usersRepository;
    @Mock FirebaseAuth firebaseAuth;
    @Mock FirebaseToken firebaseToken;

    @InjectMocks UserService userService;

    @Test
    void getOrCreateFirebaseUser_shouldReturnExistingUser() throws Exception {
        // arrange
        String testUid = "firebaseUid123";
        String idToken   = "mockedToken";

        // prepare an existing DB user
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setFirebaseUid(testUid);
        user.setUsername("test@gmail.com");
        user.setName("Test User");
        when(usersRepository.findByFirebaseUid(testUid))
                .thenReturn(Optional.of(user));

        // stub only the UID (that code actually uses)
        when(firebaseToken.getUid()).thenReturn(testUid);

        // now mock the static Firebase calls
        try (MockedStatic<FirebaseAuth> authMock = mockStatic(FirebaseAuth.class);
             MockedStatic<FirebaseApp> appMock   = mockStatic(FirebaseApp.class)) {

            // pretend FirebaseApp is already initialized
            appMock.when(FirebaseApp::getApps)
                    .thenReturn(List.of(mock(FirebaseApp.class)));

            // stub Auth.getInstance() → our mock, and verifyIdToken
            authMock.when(FirebaseAuth::getInstance).thenReturn(firebaseAuth);
            when(firebaseAuth.verifyIdToken(idToken)).thenReturn(firebaseToken);

            // act
            Users result = userService.getOrCreateFirebaseUser(idToken);

            // assert
            assertEquals("test@gmail.com", result.getUsername());
            verify(usersRepository).findByFirebaseUid(testUid);
        }
    }
}