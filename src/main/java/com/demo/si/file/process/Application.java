package com.demo.si.file.process;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.messaging.support.MessageBuilder;

import com.demo.si.file.process.constants.Constants;
import com.demo.si.file.process.handler.S3FileHandler;
import com.demo.si.file.process.integration.IS3EventGateway;
import com.demo.si.file.process.transformer.StatisticsTransformer;

/**
 * Main class to load Visa Transactions
 */
@SpringBootApplication(exclude = { AopAutoConfiguration.class, DataSourceAutoConfiguration.class,
        EmbeddedServletContainerAutoConfiguration.class, WebMvcAutoConfiguration.class, JmxAutoConfiguration.class,
        PersistenceExceptionTranslationAutoConfiguration.class, RedisAutoConfiguration.class, WebClientAutoConfiguration.class })
@IntegrationComponentScan("com.demo.si.file.process")
@ImportResource("classpath:applicationContext-fileProcess.xml")
public class Application implements ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    static ApplicationContext context;

    public static void main(String[] args) {
        String baseDir = System.getProperty(Constants.BASE_DIR);
        String filePrefix = System.getProperty(Constants.FILE_PREFIX);
        String processingFilePattern = System.getProperty(Constants.PROCESSING_FILE_PATTERN);
        Application app = new Application();
        app.processFileMessage(baseDir, filePrefix, processingFilePattern);
    }

    public static Object getSpringBean(String beanName) {
        return context.getBean(beanName);
    }

    public void processFileMessage(String baseDir, String filePrefix, String processingFilePattern) {
        SpringApplication.run(Application.class, new String[0]).registerShutdownHook();
        String localFilePath = StringUtils.join(baseDir, "/", filePrefix, "/", processingFilePattern);
        Map<String, String> headers = new HashMap<>();
        headers.put(Constants.FILE_NAME, processingFilePattern);
        headers.put(Constants.BUCKET_NAME, baseDir);
        headers.put(Constants.BUCKET_PREFIX, filePrefix);
        getIs3EventGateway().processFile(MessageBuilder.withPayload(localFilePath).copyHeaders(headers).build());
        getStatisticsTransformer().saveStatistics();
        log.info("Process Completed --->>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    public void processFileMessage(String localFilePath, Map<String, String> headers) {
        SpringApplication.run(Application.class, new String[0]).registerShutdownHook();
        getIs3EventGateway().processFile(MessageBuilder.withPayload(localFilePath).copyHeaders(headers).build());
        getStatisticsTransformer().saveStatistics();
        getS3FileHandler().exportStatsFile();
        log.info("Process Completed --->>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @PreDestroy
    public void close() {
        log.info("Close Method Called >>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private StatisticsTransformer getStatisticsTransformer() {
        return (StatisticsTransformer) getSpringBean("statisticsTransformer");
    }

    private S3FileHandler getS3FileHandler() {
        return (S3FileHandler) getSpringBean("s3FileHandler");
    }

    private IS3EventGateway getIs3EventGateway() {
        return (IS3EventGateway) getSpringBean("s3Gateway");
    }
}
