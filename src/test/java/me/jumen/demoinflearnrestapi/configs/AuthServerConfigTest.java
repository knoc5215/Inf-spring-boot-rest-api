package me.jumen.demoinflearnrestapi.configs;

import me.jumen.demoinflearnrestapi.accounts.Account;
import me.jumen.demoinflearnrestapi.accounts.AccountRole;
import me.jumen.demoinflearnrestapi.accounts.AccountService;
import me.jumen.demoinflearnrestapi.common.BaseControllerTest;
import me.jumen.demoinflearnrestapi.common.TestDescription;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Base64Utils;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    void getAuthToken() throws Exception {
        // Given
        String username = "jumen@naver.com";
        String password = "5215";

        Account build = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(build);

        String clientId = "myApp";
        String clientSecret = "pass";


        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientSecret))
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password")
                )

                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("access_token").exists())
        ;
    }
}