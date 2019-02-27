package com.rjsoft.jycj.admin.web.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.rjsoft.jycj.admin.web.interceptor.HttpLogInterceptor;
import com.rjsoft.jycj.admin.web.interceptor.UserInterceptor;
import com.rjsoft.jycj.admin.web.resolver.RangeMethodArgumentResolver;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.format.DistanceFormatter;
import org.springframework.data.geo.format.PointFormatter;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.ProxyingHandlerMethodArgumentResolver;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.tuple.StringToDateConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.List;

/**
 * Spring Boot 配置web启动信息，
 * 用于替换 web.xml
 * <p>
 */
@SpringBootConfiguration
public class WebMvcConfigBean implements WebMvcConfigurer, ApplicationContextAware {

    @Autowired
    @Qualifier("mvcConversionService")
    private ObjectFactory<ConversionService> conversionService;

    @Autowired
    private ObjectFactory<ObjectMapper> objectMapper;

    private ApplicationContext applicationContext;


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);

        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(stringConverter);
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(new AllEncompassingFormHttpMessageConverter());

        /**
         * 集成FastJson
         */
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        fastJsonConfig.setDateFormat("yyyy-MM-dd");

        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(
                objectMapper.getObject());
        converters.add(jackson2HttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(httpLogInteceptor()).addPathPatterns("/**");
        registry.addInterceptor(userInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(DistanceFormatter.INSTANCE);
        registry.addFormatter(PointFormatter.INSTANCE);
        registry.addConverter(new StringToDateConverter());

        if (!(registry instanceof FormattingConversionService)) {
            return;
        }

        FormattingConversionService conversionService = (FormattingConversionService) registry;

        DomainClassConverter<FormattingConversionService> converter = new DomainClassConverter<>(conversionService);
        converter.setApplicationContext(applicationContext);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(sortResolver());
        argumentResolvers.add(pageableResolver());

        ProxyingHandlerMethodArgumentResolver resolver = new ProxyingHandlerMethodArgumentResolver(conversionService, true);
        resolver.setBeanFactory(applicationContext);
        resolver.setBeanClassLoader(applicationContext.getClassLoader());

        argumentResolvers.add(resolver);

        RangeMethodArgumentResolver<?> rangeMethodArgumentResolver = new RangeMethodArgumentResolver<>(conversionService.getObject());
        argumentResolvers.add(rangeMethodArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(false).maxAge(3600);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    public HttpLogInterceptor httpLogInteceptor() {
        return new HttpLogInterceptor();
    }

    /**
     * 获取用户登录信息拦截器
     *
     * @return 用户登录拦截器
     */
    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver(
                sortResolver()) {
            @Override
            public Pageable resolveArgument(MethodParameter methodParameter, ModelAndViewContainer mavContainer,
                                            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
            }
        };
        pageableHandlerMethodArgumentResolver.setPageParameterName("page");
        pageableHandlerMethodArgumentResolver.setOneIndexedParameters(true);
        pageableHandlerMethodArgumentResolver.setSizeParameterName("size");
        return pageableHandlerMethodArgumentResolver;
    }

    @Bean
    public SortHandlerMethodArgumentResolver sortResolver() {
        return new SortHandlerMethodArgumentResolver();
    }

    /**
     * Bean Utils
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

}