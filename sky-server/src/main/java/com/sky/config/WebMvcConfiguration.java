package com.sky.config;

import com.sky.interceptor.JwtTokenAdminInterceptor;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;
    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/user/register")   // 新增：注册免登录
                .excludePathPatterns("/user/dish/**")          // 新增：菜品浏览
                .excludePathPatterns("/user/category/**")      // 新增：分类浏览
                .excludePathPatterns("/user/setmeal/**")      // 新增：套餐浏览
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过knife4j(OpenAPI3)生成接口文档
     *
     * @return
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("苍穹外卖项目接口文档")
                        .version("2.0")
                        .description("苍穹外卖项目接口文档"))
                // 声明两个 apiKey 安全方案，knife4j 会显示 Authorize 按钮，
                // 填入 token 后自动加到调试请求头。管理端用 token，用户端用 authentication。
                .components(new Components()
                        .addSecuritySchemes("token", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("token"))
                        .addSecuritySchemes("authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("authentication")))
                // 全局声明需要上述方案，knife4j 调试时才会把 Authorize 里填的 token
                // 附加到请求头（仅定义 scheme 不声明 security 是不会自动发送的）。
                // 同时列出两个头无害：管理端读 token、用户端读 authentication，多余的头会被忽略。
                .addSecurityItem(new SecurityRequirement().addList("token").addList("authentication"));
    }

    /**
     * 设置静态资源映射
     * 
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

        // 将本地文件系统的 uploads 目录映射为 /uploads/** 的 URL
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

    /**
     * 扩展mvc框架的消息转换器
     * 
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        // 给已有的 Jackson 转换器替换为自定义 ObjectMapper（统一日期格式）。
        // 注意：不能把新转换器 add(0, ...) 插到最前面——那样会抢在 ByteArrayHttpMessageConverter
        // 之前接管 byte[] 返回值（如 springdoc 的 /v3/api-docs），被 Jackson 序列化成 Base64
        // 字符串，导致 knife4j 文档页 “文档请求异常”。
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(new JacksonObjectMapper());
            }
        }
    }

    /**
     * 配置跨域
     * 
     * @param registry
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")   // 前端 dev 地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
