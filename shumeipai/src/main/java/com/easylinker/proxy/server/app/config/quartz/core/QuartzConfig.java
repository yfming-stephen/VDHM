package com.easylinker.proxy.server.app.config.quartz.core;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Spring boot 集成Quartz核心配置
 */
@Configuration
public class QuartzConfig {
@Autowired
QuartzJobFactory quartzJobFactory;
    /**
     * 配置一个JOB任务工厂
     * @return
     */
    @Bean
    public JobFactory jobFactory() {
        return new SpringBeanJobFactory();
    }

    /**
     * 从配置文件加载Quartz的配置
     * @param dataSource
     * @param jobFactory
     * @return
     */

//    @Bean
//    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("dataSource") DataSource dataSource, JobFactory jobFactory) {
//        SchedulerFactoryBean factory = new SchedulerFactoryBean();
//        factory.setOverwriteExistingJobs(true);
//
//        // 延时启动
//        factory.setStartupDelay(20);
//        factory.setJobFactory(jobFactory);
//        factory.setApplicationContextSchedulerContextKey(BaseJob.APPLICATION_CONTEXT_KEY);
//
//        // 加载quartz数据源配置
//        factory.setConfigLocation(new ClassPathResource("quartz.properties"));
//        factory.setDataSource(dataSource);
//        // 自定义Job Factory，用于Spring注入
//        factory.setJobFactory(jobFactory());
//        factory.setSchedulerName("quartz-scheduler");
//        return factory;
//    }





    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setAutoStartup(true);
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(quartzJobFactory);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /*
     * quartz初始化监听器
     */
    @Bean
    public QuartzInitializerListener executorListener() {
        return new QuartzInitializerListener();
    }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name="Scheduler")
    public Scheduler scheduler() throws IOException {
        return schedulerFactoryBean().getScheduler();
    }

}