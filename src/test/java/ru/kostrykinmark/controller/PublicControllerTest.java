package ru.kostrykinmark.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class PublicControllerTest {
    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @Transactional
    void signUp_isAccessible() throws Exception {
        mockMvc.perform(post("/public/signUp")
                        .content(new ObjectMapper().writeValueAsString(
                                SignUpUserDto.builder()
                                        .username("kostrykinmark")
                                        .email("kostrykinmark@gmail.com")
                                        .password("12345")
                                        .build()
                        )).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @Transactional
    void signIn_isAccessible() throws Exception {
        mockMvc.perform(post("/public/signUp")
                .content(new ObjectMapper().writeValueAsString(
                        SignUpUserDto.builder()
                                .username("kostrykinmark")
                                .email("kostrykinmark@gmail.com")
                                .password("12345")
                                .build()
                )).contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/public/signIn")
                .content(new ObjectMapper().writeValueAsString(
                        SignInUserDto.builder()
                                .email("kostrykinmark@gmail.com")
                                .password("12345")
                                .build()
                ))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void signIn_IncorrectCredentials_returnsForbidden() throws Exception {

        mockMvc.perform(post("/public/signIn")
                .content(new ObjectMapper().writeValueAsString(
                        SignInUserDto.builder()
                                .email("kostrykinmark@gmail.com")
                                .password("1245")
                                .build()
                ))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

}
