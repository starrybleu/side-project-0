package io.github.starrybleu.sideproject0;

import io.github.starrybleu.sideproject0.entity.ApiUser;
import io.github.starrybleu.sideproject0.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    UserService service;

    @Test
    public void applicationContextLoaded() throws Exception {
        assertThat(mvc).isNotNull();
    }

    @Test
    public void 정상적인_회원_가입_테스트() throws Exception {
        ApiUser mockUser = mock(ApiUser.class);
        when(service.createUser(any()))
                .thenReturn(mockUser);

        mvc.perform(post("/api/users/sign-up")
                .header("content-type", MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"test@company.com\",\n" +
                        "  \"password\": \"password\",\n" +
                        "  \"userType\": \"driver\"\n" +
                        "}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").value(hasKey("id")))
        ;
    }

    @Test
    public void 정상적인_로그인_테스트() throws Exception {
        String mockToken = "mock-json-web-token";
        when(service.authenticate(any()))
                .thenReturn(mockToken);


        mvc.perform(post("/api/users/sign-in")
                .header("content-type", MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"email\": \"test@company.com\",\n" +
                        "  \"password\": \"password\"\n" +
                        "}"))
                .andExpect(status().is(200))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(content().string(mockToken))
        ;
    }

}
