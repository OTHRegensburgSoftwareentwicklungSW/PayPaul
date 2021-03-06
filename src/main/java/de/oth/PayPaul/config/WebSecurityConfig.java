package de.oth.PayPaul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  private DataSource dataSource;

  @Value("${spring.queries.users-query}")
  private String usersQuery;

  @Value("${spring.queries.roles-query}")
  private String rolesQuery;

  @Autowired
  private void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private void setPasswordEncoder(BCryptPasswordEncoder encoder) {
    this.bCryptPasswordEncoder = encoder;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth)
          throws Exception {
    auth.
            jdbcAuthentication()
            .usersByUsernameQuery(usersQuery)
            .authoritiesByUsernameQuery(rolesQuery)
            .dataSource(dataSource)
            .passwordEncoder(bCryptPasswordEncoder);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/register", "/login").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .csrf().disable()
            .formLogin()
            .loginPage("/login")
            .failureUrl("/login?error=true")
            .failureHandler(getCustomAuthFailureHandler())
            .defaultSuccessUrl("/transactions")
            .usernameParameter("email")
            .passwordParameter("password_hash")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .and()
            .exceptionHandling()
            .accessDeniedPage("/access-denied")
            .and().httpBasic();
  }

  @Override
  public void configure(WebSecurity web) {
    web
            .ignoring()
            .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
  }

  @Bean
  public AuthenticationFailureHandler getCustomAuthFailureHandler() {
    return new CustomAuthFailureHandler();
  }
}
