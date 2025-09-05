package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; // 注意这里的变化

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer { // 实现 WebMvcConfigurer 接口

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    // Swagger 3 (OpenAPI) 相关的路径
    private static final String[] SWAGGER_PATHS = {
            "/doc.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**", // 注意这里是'/**', 因为可能有 /v3/api-docs/group-name 等
            "/webjars/**"
    };

    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**") // 拦截 admin 路径下的所有请求
                .excludePathPatterns("/admin/employee/login"); // 排除登录接口
    }

    /*
      实现 WebMvcConfigurer 后，Spring Boot 的自动配置会处理静态资源。
      你不再需要手动重写 addResourceHandlers 来映射 Swagger UI 的资源。
      自动配置会处理好 /swagger-ui.html 和 /webjars/** 等路径。
      因此，addResourceHandlers 方法可以被安全地删除。
     */

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建自己的消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //给消息转换器设置对象转换器，底层使用Jackson将Java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        //将自己的消息转换器添加到MVC框架的转换器集合中
        converters.add(0,messageConverter); // 0 表示最高优先级

    }
}