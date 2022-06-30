package cn.qianzf.widget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class GyfhApp extends SpringApplication{

    public GyfhApp(Class<?>... primarySources) {
        super(primarySources);
    }

    public static void main(String[] args) {
       new GyfhApp(GyfhApp.class).run(args);
    }

    @Override
    protected void configureEnvironment(ConfigurableEnvironment environment, String[] args) {
        super.configureEnvironment(environment, args);
        Map<String, Object> source=new HashMap<>();
        List<String> list=new ArrayList<>();
        list.add("application");
        list.add("testapp");
        list.add("devapp");
        source.put(ConfigFileApplicationListener.CONFIG_NAME_PROPERTY, StringUtils.collectionToCommaDelimitedString(list));
        MapPropertySource mapPropertySource=new MapPropertySource("gyfhExtendProperty",source);
        environment.getPropertySources().addFirst(mapPropertySource);
    }
}
