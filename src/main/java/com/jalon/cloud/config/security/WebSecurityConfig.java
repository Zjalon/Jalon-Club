package com.jalon.cloud.config.security;

import com.jalon.cloud.config.filter.TokenAuthFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final Log logger = LogFactory.getLog(WebSecurityConfig.class);

    @Autowired
    private CustomerUserService customerUserService;

    @Autowired
    private TokenAuthFilter tokenAuthFilter;

    /**
     * WebSecurity 全局请求忽略规则配置
     *
     * @param web
     */
    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
        logger.info("WebSecurity忽略规则配置完成");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                // 允许跨域设置
                .cors()
                // 关闭csrf认证
                .and().csrf().disable()
                // 关闭session
                // ALWAYS,总是创建HttpSession
                // NEVER,Spring Security不会创建HttpSession，但如果它已经存在，将可以使用HttpSession
                // IF_REQUIRED,Spring Security只会在需要时创建一个HttpSession
                // STATELESS,Spring Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                // 不需要认证的请求
                .antMatchers("").permitAll()
                .and().addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * WebSecurity 认证配置--form表单提交方式
     *
     * @throws Exception
     */
//    @Override
//    protected void configure(final HttpSecurity http) throws Exception {
//        http
//                .exceptionHandling().authenticationEntryPoint(new ExceptionEntryPoint())
//                .and()
//                // 允许跨域
//                .cors()
//                // 关闭csrf验证
//                .and().csrf().disable()
//                .authorizeRequests()
//                // 下面这一行是不需要验证路径
//                .antMatchers("/", "/index", "/login").permitAll()
//                // 其他都需要认证
//                .anyRequest().authenticated()
//                .and()
//                // 设置登录相关
//                .formLogin()
//                // 当需要用户登录时,会跳转到下面页面
//                .loginPage("/index")
//                // 登录成功处理
//                .successHandler(new AjaxAuthSuccessHandler())
//                // 登录失败处理
//                .failureHandler(new AjaxAuthFailureHandler())
//                // 指定登录请求
//                .loginProcessingUrl("/login")
//                .permitAll()
//                .and()
//                // 退出时,销毁session,然后跳转页面
//                .logout()
//                .logoutSuccessHandler(new AjaxLogoutSuccessHandler())
//                .logoutUrl("/logout")
//                .and()
//                // session管理,同时只能用一个用户登录,强制踢出前一个用户并跳转到/login?expired
//                .sessionManagement()
//                .maximumSessions(1)
//                .expiredUrl("/login?expired");
//        logger.info("WebSecurity认证规则配置完成");
//    }
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("123456").roles("USER");
        auth.userDetailsService(customerUserService).passwordEncoder(passwordEncoder());
        logger.info("WebSecurity认证方式配置完成");
    }

    /**
     * 密码加密方式
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录成功处理
     */
    private class AjaxAuthSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication auth) throws IOException, ServletException {
            logger.info("商户[" + SecurityContextHolder.getContext().getAuthentication().getPrincipal() + "]登陆成功！");
            //登陆成功后移除session中验证码信息
            request.getSession().removeAttribute("codeValue");
            request.getSession().removeAttribute("codeTime");

            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"200\",\"msg\":\"登录成功\"}");
            out.flush();
            out.close();
        }
    }

    /**
     * 登录失败处理
     */
    private class AjaxAuthFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException e) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"401\",\"msg\":\"请检查用户名、密码或验证码是否正确\"}");
            out.flush();
            out.close();
        }
    }

    /**
     * 退出成功处理
     */
    private class AjaxLogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
        @Override
        public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write("{\"status\":\"200\",\"msg\":\"登出成功\"}");
            out.flush();
            out.close();
        }
    }

    /**
     * 异常信息处理
     */
    private class ExceptionEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
        @Override
        public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException e) throws IOException, ServletException {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        }
    }
}
