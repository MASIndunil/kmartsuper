package lk.kmart_super.configuration;


import lk.kmart_super.asset.user_management.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String[] ALL_PERMIT_URL = {"/favicon.ico", "/img/**", "/css/**", "/js/**", "/webjars/**",
      "/login", "/select/**", "/", "/index"};

  @Bean
  public UserDetailsServiceImpl userDetailsService() {
    return new UserDetailsServiceImpl();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  /*Session management - bean start*/
  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
  /*Session management - bean end*/

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return new CustomAuthenticationSuccessHandler();
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new CustomLogoutSuccessHandler();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
  /*  http.csrf().disable();
    http.authorizeRequests().antMatchers("/").permitAll();
*/
    // For developing easy to give permission all lin
// {"ADMIN","PROCUREMENT_MANAGER","CASHIER","MANAGER","HR_MANAGER","ACCOUNT_MANAGER"}

    http.authorizeRequests(
        authorizeRequests ->
            authorizeRequests
                .antMatchers(ALL_PERMIT_URL).permitAll()
                .antMatchers("/employee/**").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/user/**").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/role/**").hasAnyRole("ADMIN")
                .antMatchers("/supplier/**").hasAnyRole("ADMIN","MANAGER","PROCUREMENT_MANAGER")
                .antMatchers("/supplierItem/**").hasAnyRole("ADMIN","MANAGER","PROCUREMENT_MANAGER")
                .antMatchers("/customer/**").hasAnyRole("ADMIN","MANAGER","CASHIER")
                .antMatchers("/category/**").hasAnyRole("ADMIN","MANAGER")
                .antMatchers("/item/**").hasAnyRole("ADMIN","MANAGER","CASHIER","PROCUREMENT_MANAGER")
                .antMatchers("/ledger/**").hasAnyRole("ADMIN","MANAGER","CASHIER","PROCUREMENT_MANAGER")
                .antMatchers("/purchaseOrder/**").hasAnyRole("ADMIN","MANAGER","CASHIER","PROCUREMENT_MANAGER")
                .antMatchers("/goodReceivedNote/**").hasAnyRole("ADMIN","MANAGER","PROCUREMENT_MANAGER")
                .antMatchers("/payment/**").hasAnyRole("ADMIN","MANAGER","CASHIER")
                .antMatchers("/invoice/**").hasAnyRole("ADMIN","MANAGER","CASHIER")
                .antMatchers("/discountRatio/**").hasAnyRole("ADMIN","MANAGER")
                .anyRequest()
                .authenticated()
    )
        // Login form
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    //Username and password for validation
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler())
                    .failureUrl("/login?error")
                  )
        //Logout controlling
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(customLogoutSuccessHandler())
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true))
        //session management
        .sessionManagement(
            sessionManagement ->
                sessionManagement
                    .sessionFixation().migrateSession()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .invalidSessionUrl("/login")
                    .maximumSessions(6)
                    .expiredUrl("/logout")
                    .sessionRegistry(sessionRegistry()))
        //Cross site disable
        .csrf(AbstractHttpConfigurer::disable)
//        .exceptionHandling();
            .exceptionHandling().and()
            .headers()
            .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

  }
}

