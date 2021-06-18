package ZhienlinAzamat.RN_uchet.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity // (debug = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //    In memory authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER")
                .and()
                .withUser("userReader")
                .password(passwordEncoder().encode("userReader"))
                .roles("READER")
                .and()
                .withUser("УНП2")
                .password(passwordEncoder().encode("строганина"))
                .roles("USER")
                .and()
                .withUser("УНП2_ЧТЕНИЕ")
                .password(passwordEncoder().encode("УНП2_ЧТЕНИЕ"))
                .roles("READER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/","/profile","allinfo","/info*")
                    .hasAnyRole("READER","USER")
                .and()
                .authorizeRequests()
                    .antMatchers("/**")
                    .hasRole("USER")
                .and()
                .authorizeRequests()
                    .antMatchers("/templates/**","/static/**").authenticated()
                    .and()
                .formLogin()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/css/**")
                .antMatchers("/image/**");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}