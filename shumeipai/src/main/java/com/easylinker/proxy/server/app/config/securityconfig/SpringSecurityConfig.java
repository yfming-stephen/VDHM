package com.easylinker.proxy.server.app.config.securityconfig;

import com.easylinker.proxy.server.app.config.securityconfig.filter.CustomUsernamePasswordFilter;
import com.easylinker.proxy.server.app.config.securityconfig.filter.OpenIdAuthenticationFilter;
import com.easylinker.proxy.server.app.config.securityconfig.handler.AnonymousHandler;
import com.easylinker.proxy.server.app.config.securityconfig.handler.LoginFailureHandler;
import com.easylinker.proxy.server.app.config.securityconfig.handler.LoginSuccessHandler;
import com.easylinker.proxy.server.app.service.AppUserDetailService;
import com.easylinker.proxy.server.app.utils.MD5Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


/**
 * Created by wwhai on 2018/3/14.
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AppUserDetailService appUserDetailService;
    @Autowired
    LoginSuccessHandler loginSuccessHandler;
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    AnonymousHandler anonymousHandler;
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    /**
     * WEB资源路径配置器
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().mvcMatchers(
                "/public.html",
                "/css/**",
                "/avatar/**",//头像
                "/js/**",
                "/dest/**");//css路径放行
    }


    /**
     * HTTP资源配置
     * 默认以下地址不检查
     * /register 注册入口
     * /userLogin 登陆入口
     * /index 默认首页
     *
     * @param http
     * @throws Exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(getCustomUsernamePasswordFilter());
        http.addFilter(getOpenIdAuthenticationFilter());
        //配置不用过滤的路由
        http.authorizeRequests()
                .antMatchers(
                        "/",//首页
                        "/userLogin",//一般登陆
                        "/wechatLogin",//微信登陆
                        "/user/register",//注册
                        "/wechat/register",//微信注册
                        "/wechat/bind",//微信绑定
                        "/wechat/whetherBind",//微信绑定
                        "/user/activeUser/*",//激活
                        "/trace/**"//溯源
//                        ,"/device/**"
                )
                .permitAll();


        http.authorizeRequests().anyRequest().authenticated()
                .and().formLogin().disable().httpBasic().disable()
                .logout().permitAll()
                .and().logout().logoutSuccessHandler(logoutSuccessHandler)
                .logoutUrl("/logOut")
                .and().rememberMe().alwaysRemember(true)
                // 配置Cookie过期时间
                .tokenValiditySeconds(10)
                // 配置UserDetailsService
                .and().exceptionHandling()
                .authenticationEntryPoint(anonymousHandler)
                .and().csrf().disable();
    }


    @Bean
    public CustomUsernamePasswordFilter getCustomUsernamePasswordFilter() throws Exception {
        CustomUsernamePasswordFilter customUsernamePasswordFilter = new CustomUsernamePasswordFilter();
        customUsernamePasswordFilter.setAuthenticationManager(super.authenticationManager());
        customUsernamePasswordFilter.setAuthenticationFailureHandler(loginFailureHandler);
        customUsernamePasswordFilter.setAuthenticationSuccessHandler(loginSuccessHandler);

        return customUsernamePasswordFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public OpenIdAuthenticationFilter getOpenIdAuthenticationFilter() throws Exception {
        OpenIdAuthenticationFilter openIdAuthenticationFilter = new OpenIdAuthenticationFilter();
        openIdAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        openIdAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler);
        openIdAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        return openIdAuthenticationFilter;
    }


    /**
     * 密码认证过程
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailService).passwordEncoder(new PasswordEncoder() {

            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Generator.EncodingMD5((String) rawPassword);
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(rawPassword);
            }
        });
    }

}
