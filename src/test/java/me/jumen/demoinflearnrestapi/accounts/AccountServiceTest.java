package me.jumen.demoinflearnrestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {

    @Rule
    public ExpectedException expectedException;

    @Autowired
    AccountService accountService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    void findByUsername() {
        //Given
        String username = "jumen@naver.com";
        String password = "5215";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();

        this.accountService.saveAccount(account);   // using passwordEncoder

        // When
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();


    }

    @Test
    void findByUsernameFail() {
        // Expected
        String username = "random@naver.com";
        expectedException.expect(UsernameNotFoundException.class);  // 발생을 기대할 예외
        expectedException.expectMessage(Matchers.containsString(username));

        // When
        accountService.loadUserByUsername(username);


    }
}