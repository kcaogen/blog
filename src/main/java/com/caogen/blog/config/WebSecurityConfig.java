package com.caogen.blog.config;

import com.caogen.blog.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() { //覆盖写userDetailsService方法 (1)
        return new UserService();

    }

    protected void configure(HttpSecurity http) throws Exception {
        /*super.configure(http);*/

        http.authorizeRequests()
                .antMatchers("/",
                        "/blog/**",
                        "/show/**",
                        "/error/**").permitAll()
                .anyRequest().authenticated().and()
                .formLogin().loginPage("/login")// 登录url请求路径 (3)
                .failureUrl("/login?error=true") //登录失败的跳转页面
                .defaultSuccessUrl("/admin").permitAll().and() // 登录成功跳转路径url(4)
                .logout().permitAll();

                http.logout().logoutSuccessUrl("/"); // 退出默认跳转页面 (5)
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth
            .inMemoryAuthentication()
                .withUser("root")
                .password("root")
                .roles("USER")
            .and()
                .withUser("admin")
                .password("admin")
                .roles("ADMIN", "USER")
            .and()
                .withUser("user")
                .password("user")
                .roles("USER");*/
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(new BCryptPasswordEncoder());      //密码加密
    }

    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/css/**",
                        "/editormd/**",
                        "/GeekBlog/**",
                        "/image/**",
                        "/js/**",
                        "/layer/**");
    }

}
