# 스프링 빈 사용



## 3.2 자바 설정



![스프링 애플리케이션의 시작 과정](https://user-images.githubusercontent.com/12541721/236750891-620de456-21d5-45d1-932a-b6d2745e4c99.png)

- 클래스를 스프링 빈으로 만드는 스프링 빈 정의를 설정한다.
- 설정된 스프링 빈 정의를 스프링 빈 컨테이너가 찾을 수 있도록 설정한다. 
- 서로 의존성이 있는 스프링 빈들을 조립할 수 있도록 설정한다. 



순서

1. 스프링 부트 프레임워크의 기본 스프링 빈 컨테이너 구현체인 ConfigurableApplicationContext가 설정 정보를 담은 자바 클래스를 찾는다.
2. 설정 파일(자바 클래스) 에 정의된 스프링 빈 정의를 로딩하고, 지정된 클래스 패스에 위치한 클래스들을 스캔하고, 스프링 빈 정의가 있으면 로딩한다. 
3. 로딩을 마친 스프링 빈 컨테이너는 정의된 대로 스프링 빈을 생성하고 컨테이너에서 관리한다.
4. 스프링 빈들 사이에 서로 의존성이 있는 객체들은 스프링 빈 컨테이너가 조립한다.
5. 스프링 빈 컨테이너 구현 클래스에 따라 추가 작업을 한다.
6. 작업이 완료되면 애플리케이션은 실행 준비를 완료한다. 



@Bena 애너테이션 

- 스프링 빈을 정의할 때 필요한 요소는 스프링 빈 이름, 클래스 타입, 객체다.
  - 빈 이름 : name 속성으로 설정. 설정하지 않는 경우 @Bean 애너테이션이 정의된 메서드 이름이 빈 이름으로 등록된다.
  - 클래스 타입 : @Bean 애너테이션이 정의된 메서드의 리턴 타입이 클래스 타입이다.
  - 객체 : @Bean 애너테이션이 정의된 메서드가 리턴하는 객체 



```java
package com.sjbaek;

import com.sjbaek.domain.PriceUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Locale;

@Slf4j
@SpringBootApplication
public class SpringBean01Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBean01Application.class, args);

        PriceUnit defaultPriceUnit = context.getBean("priceUnit", PriceUnit.class);
        log.info("Price #1 : {}", defaultPriceUnit.format(BigDecimal.valueOf(10.2)));

        PriceUnit wonPriceUnit = context.getBean("wonPriceUnit", PriceUnit.class);
        log.info("Price #2 : {}", wonPriceUnit.format(BigDecimal.valueOf(1000)));

        context.close();
    }

    @Bean(name = "priceUnit")
    public PriceUnit dollarPriceUnit() {
        return new PriceUnit(Locale.US);
    }

    @Bean
    public PriceUnit wonPriceUnit() {
        return new PriceUnit(Locale.KOREA);
    }
}
```

- Bean 정의시 이름 (priceUnit) 을 직정 정의하거나 메서드 이름이 사용 됨.
- Bean 의 클래스트 타입은 PriceUnit 
- Bean의 객체는 PriceUnit 클래스 타입 객체 2개가 존재한다. 
- 즉, 클래스 타입이 같은 여러 스프링 빈을 설정 가능하다. 



@ComponentScan

- 설정된 패키지 경로 및 하위 경로에 포함된 자바 설정 클래스(@Configuration) 들과 스트레오 타입 애너테이션들(@Component, @Controller 등) 이 선언된 클래스들을 스캔한다. 
- 자바 설정 클래스에 정의되어야 하기 때문에 @Configuration 과 함께 사용 된다. 

```java
package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackages = {"com.sjbaek.chapter3.config", "com.sjbaek.chapter3.domain"},
        basePackageClasses = {ServerConfiguration.class}
)
public class ServerConfiguration {
    
}
```

- 자바 설정 클래스에 정의되어야 하기 때문에 @Configuration 과 함께 사용 

- basePackages, basePackgeClasses 사용하여 scan 범위 설정 

  

@Import

- 명시된 여러 개의 자바 설정 클래스를 하나의 그룹으로 묶는 역할을 한다.
- 즉, @Import 애너테이션이 정의된 자바 설정 클래스가 다른 자바 설정 클래스들을 임포트(Import) 한다.
- 그러므로 비슷한 성질의 설정들을 하나의 그룹의로 묶는 기능을 한다. 
- @ComponentScan과 역활은 동일하나 대상 자바 클래스를 명시적으로 지정하기 때문에 직관적이나 @SpringBootApplication 애너테이션 내부에 ComponentScan 애너테이션이 정의되어 있으므로 관례에 따라 @ComponentScan 을 쓰는게 편하다. 

```java
package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
        basePackages = {"com.sjbaek.chapter3.config", "com.sjbaek.chapter3.domain"},
        basePackageClasses = {ServerConfiguration.class}
)
@Import(value = {ThreadPoolConfig.class})
public class ServerConfiguration {

}

```

- ServerConfiguration 자바 설정 클래스만 ApplicationContext가 로딩하면 나머지 설정 클래스 (ThreadPoolConfig) 도 로딩한다.





## 3.3 스트레오 타입 스프링 빈 사용

- 보통 애플리케이션 설정 영역은 @Bean 애너테이션을 사용하고, 비즈니스 로직 영역은 스트레오 타입 애너테이션을 사용하여 스프링 빈을 정의한다. 
  - @Component
    - 클래스를 스프링 빈으로 정의하는 데 사용하는 가장 일반적인 애너테이션이다. 다른 스트레오 타입 애너테이션들은 @Component 애너테이션에서 파생되었으며, 클래스의 목적, 디자인 패턴에 따라 적절한 애너테이션을 선택해야 한다. 
  - @Controller 
    - 컨트롤러 역할을 하는 클래스를 스프링 빈으로 정의한다.
  - @Service
    - 도메인 주소 설계의 서비스 역할인 클래스를 정의하는데 사용한다.
  - @Repository
    - 도메인 주도 설계의 리포지터리 역할인 클래스를 정의하는데 사용한다. 

```java
package com.sjbaek.chapter3.domain.format;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    @Override
    public String of(LocalDateTime target) {
        return Optional.ofNullable(target)
                .map(formatter::format)
                .orElse(null);
    }
}

```

```java
package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.format.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class SpringBean02Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean02Application.class, args);

        Formatter<LocalDateTime> formatter = context.getBean("localDateTimeFormatter", Formatter.class);
        String date = formatter.of(LocalDateTime.of(2022, 5, 8, 23, 59, 59));

        log.info("Date : " + date);
    }
}

```

- 스트레오 타입 애너테이션들도  @Bean 과 마찬가지로 스프링 빈을 정의하는데 이름, 클래스 타입, 객체가 필요하다.
- 이름을 명시하지 않았으므로 클래스 이름인 localDateTimeFormatter 이다. 
- 클래스 타입은 context.getBean 메서드에서 명시한 Formatter 타입이므로 명시적 형 변환이 필요하지 않다.
- 객체는 LocalDateTimeFormatter 객체이다. 





## 3.4 의존성 주입

- @Autowired 애너테이션은 의존성이 필요한 클래스 내부에 의존성 주입을 받는 곳을 표시한다. @Qualifier 애너테이션은 의존성을 주입할 스프링 빈 이름을 정의하는 역할을 한다. 클래스 타입은 같지만 이름이 각자다른 여러 스프링 빈 중 정의된 이름의 스프링 빈을 주입받기 위해 @Qualifier를 사용한다. 



![원칙과 패턴, 구현의 관계](https://user-images.githubusercontent.com/12541721/236975570-ad7b54c6-ac48-4a4c-8afc-65295301067c.png)

스프링에서 제공하는 의존성 주입 장점

- 객체 지향 프로그래밍 기반이므로 공통 객체를 재사용 할 수 있다.
- 테스트 케이스를 작성하는 경우 목 객체를 주입하기 편리하다.
- 강한 결합에서 약한 결합이 되므로 유연하고 변화에 빠른 대응이 가능하다.



애너테이션 기반 설정의 의존성 주입

- @Autowired : 클래스 타입에 의한 주입 
- @Qualifier : 이름에 의한 주입 
- 같은 클래스 타입의 이름이 다른 여러 클래스 빈이 있다면 @Qualifier 사용해야 한다. 

```java
package com.sjbaek.chapter3.service;

import com.sjbaek.chapter3.domain.ProductOrder;
import com.sjbaek.chapter3.domain.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;

@Service
public class OrderPrinter implements Printer<ProductOrder>{

    // 빈 필드 주입
    @Autowired
    @Qualifier("localDateTimeFormatter")
    private Formatter formatter01;


    // setter 메서드 빈 주입
    private Formatter formatter02;

    @Autowired
    public void setFormatter02(@Qualifier("localDateTimeFormatter") Formatter formatter) {
        this.formatter02 = formatter;
    }

    private Formatter formatter03;

    // 빈 생성자 주입
    @Autowired
    public OrderPrinter(@Qualifier("localDateTimeFormatter") Formatter formatter03) {
        this.formatter03 = formatter03;
    }

    @Override
    public void print(OutputStream os, ProductOrder productOrder) throws IOException {
        String orderAt = formatter01.of(productOrder.getOrderAt());
        os.write(orderAt.getBytes());
    }
}
```

- @Autowired 애너테이션을 통한 필드 주입, setter 메서드 주입, 생성자 주입을 통한 의존성 주입. 
- @Qualifier 애너테이션을 사용해 빈 이름을 정의할 수 있다. 





자바 설정의 의존성 주입 

- @Bean, @Configuration 애너테이션을 사용한 자바 설정 클래스 내에서 스프링 빈끼리 의존성 주입 

```java
package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.datetime.DateFormatter;

@Configuration
@ComponentScan(
        basePackages = {"com.sjbaek.chapter3.config", "com.sjbaek.chapter3.domain"},
        basePackageClasses = {ServerConfiguration.class}
)
@Import(value = {ThreadPoolConfig.class})
public class ServerConfiguration {

    @Bean
    public String datePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss.XXX";
    }

    @Bean
    public DateFormatter defaultDateFormatter() {
        return new DateFormatter(datePattern());
    }
}
```

- Bean 애네테이션이 선언된 메서드를 그대로 사용하여 의존성 주입 ( 메서드 참조 방식 )



- 여러 개의 설정 클래스 내에 존재하는 Bean 의존성 주입 방법

```java
package com.sjbaek.chapter3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DividePatternConfig {

    @Bean
    public String localDatePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss";
    }
}
```

```java
package com.sjbaek.chapter3.config;

import com.sjbaek.chapter3.domain.format.DateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DivideServerConfig {

    @Bean
    public DateFormatter localDateFormatter(String localDatePattern) {
        return new DateFormatter(localDatePattern);
    }
}
```

- localDatePattern 인자를 분석하여 이름이 localDatePattern이고 클래스 타입이 String인 빈을 빈 컨테이너에서 찾는다. 



![스프링 빈 컨테이너의 의존성 분석 과정](https://user-images.githubusercontent.com/12541721/236984355-5a2cc99c-aee3-4b78-8748-d25d5c3a7919.png)

1. ApplicationContext가 실행될 때 우선 설정 파일을 읽는다. 이때 생성해야 할 스프링 빈은 설정 파일에 있다. 
2. 설정 파일에 포함된 @ComponentScan 으로 설정된 패키지 경로에 있는 스프링 빈 설정을 로딩한다. 
3. 찾아낸 스프링 빈들의 의존성을 검사한다.
4. 하위 모듈에서 사우이 모듈 순으로 스프링 빈을 생성하고 순차적으로 스프링 빈 사이에 의존성 주입을 한다. 



## 3.5 ApplicationContext

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
}
```

- EnvironmentCapable

  - 환경 변수를 추상화한 Environment 객체를 제공하는 getter 메서드가 포함된 인터페이스

- ListableBeanFactory

  - 스프링 빈 리스트를 리턴하는 다양한 메서드를 포함

- HierarchicalBeanFactory

  - BeanFactory 들은 서로 부모-자식 간 관계를 맺을 수 있으며, 부모 BeanFactory를 리턴받을 수 있는 메서드를 제공한다.
  - 애플리케이션의 용도에 따라 스프링 빈들을 분리하고, 분리된 스프링 빈들을 관리하는 별도의 ApplicationContext를 설정한다. 하위 ApplicationContext에서 관리하지 않는 빈은 부모 ApplicationContext 에서 받아 올 수 있다. 

- MessageSource

  - 국제화(i18n) 메세지 처리

- ApplucationEventPublisher

  - 스플이 프레임워크에서 사용할 수 있는 이벤트를 생성할 수 있는 메서드를 제공하는 인터페이스

- ResourcePatternResolver

  - 패턴을 이용해서 Resource를 다룰 수 있는 메서드를 제공하는 인터페이스

  



## 3. 6 스프링 빈 스코프

- 스프링 빈 객체를 생성하는 시간부터 만든 객체가 소멸되기까지 기간을 빈 스코프(scope) 라고 한다. 
- singleton
  - 기본값. 스프링 빈을 스프링 컨테이너에서 오직 한개만 생성하여 공유된다.
- prototype
  - 의존성 주입할 때마다 새로운 객체를 생성하여 주입 한다.
- request
  - 웹 기능이 포함된 ApplicationContext에서만 사용 가능하며, HTTP 요청을 처리할 때마다 새로운 객체를 생성한다.
  - @RequestScope, @Scope("request")
- session
  - 웹 기능이 포함된 ApplicationContext에서만 사용 가능하며, HTTP session과 대응하는 새로운 객체를 생성한다.
  - @SessionScope, @Scope("session")
- application
  - 웹 기능이 포함된 ApplicationContext에서만 사용 가능하며, Servlet 컨텍스트와 대응하는 새로운 객체를 생성한다.
  - @ApplicationScope, @Scope("application")
- WebSocket
  - 웹 기능이 포함된 ApplicatonContext에서만 사용 가능하며, Web Socket Session과 대응하는 새로운 객체를 생성한다.
  - @Scope("websocket")



```java
package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.format.DateFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SpringBean05Application {

    public static void main(String[] args) throws InterruptedException{
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean05Application.class, args);
        ThreadPoolTaskExecutor taskExecutor =
                context.getBean(ThreadPoolTaskExecutor.class);

        final String dateString = "2020-12-24T23:59:59.-08:00";
        for(int i = 0; i < 100; i++) {
            taskExecutor.execute(() -> {
                try {
                    DateFormatter formatter = context.getBean("singletonDateFormatter", DateFormatter.class);
                    log.info("Date : {}, hasCode : {}", formatter.parse(dateString), formatter.hashCode());
                } catch (Exception e) {
                    log.error("error to parse", e);
                }
            });
        }
        TimeUnit.SECONDS.sleep(5);
        context.close();
    }

    @Bean
    public DateFormatter singletonDateFormatter() {
        return new DateFormatter("yyyy-MM-dd'T'HH:mm:ss");
    }
}
```

- 쓰레드에 안전하지 않는 SimpleDateFormat 클래스를 멀티 쓰레드에서 사용시 에러 발생
- Bean의 멤버에 쓰레드 안전하지 않는 필드를 사용하는지 확인 해야 한다. 
- Scope("prototype") 사용하거나 SimpleDateFormat 객체를 매번 생성하여 해결 가능하다.





## 3.7 스프링 빈 생셩주기 관리

- 스프링 빈을 생성,소멸하는 과정에서 개발자가 작성한 코드를 특정 시점에 호출할 수 있는데, 이를 콜백(call-back) 함수라고 한다. 
- 보통 자바 설정의 @Bean을 사용한 스프링 빈은 initMethod 속성을 사용하고, 스테레오 타입 애너테이션을 사용한 빈은 @PostConstruct를 사용한다. InitializingBean은 둘 다 사용 가능하다. 

![스프링 빈 생명주기에 관여할 수 있는 callback 메서드와 그 순서](https://user-images.githubusercontent.com/12541721/237012506-bae0cbef-ff17-472c-964f-08baab36ca71.png)

```java
package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.lifecycle.LifeCycleComponent;
import com.sjbaek.chapter3.domain.lifecycle.PrintableBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBean06Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBean06Application.class, args);
        context.close();
    }

    @Bean(initMethod = "init", destroyMethod = "clear")
    public LifeCycleComponent lifecycleComponent() {
        return new LifeCycleComponent();
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new PrintableBeanPostProcessor();
    }
}
```

```java
package com.sjbaek.chapter3.domain.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class LifeCycleComponent implements InitializingBean, DisposableBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("afterPropertiesSet from InitializingBean");
    }

    @Override
    public void destroy() throws Exception {
        log.error("destroy from DisposableBean");
    }

    public void init() {
        log.error("customized init method");
    }

    public void clear() {
        log.error("customized destroy method");
    }
}
```

```java
package com.sjbaek.chapter3.domain.lifecycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class PrintableBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("lifecycleComponent".equals(beanName)) {
            log.error("Called postProcessBeforeInitialization() for : {}", beanName);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("lifecycleComponent".equals(beanName)) {
            log.error("Called postProcessAfterInitialization() for : {}", beanName);
        }

        return bean;
    }
}
```

```shell
2023-05-09T15:24:32.372+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.l.PrintableBeanPostProcessor     : Called postProcessBeforeInitialization() for : lifecycleComponent
2023-05-09T15:24:32.373+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.lifecycle.LifeCycleComponent     : afterPropertiesSet from InitializingBean
2023-05-09T15:24:32.373+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.lifecycle.LifeCycleComponent     : customized init method
2023-05-09T15:24:32.374+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.l.PrintableBeanPostProcessor     : Called postProcessAfterInitialization() for : lifecycleComponent

2023-05-09T15:24:32.694+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.lifecycle.LifeCycleComponent     : destroy from DisposableBean
2023-05-09T15:24:32.694+09:00 ERROR 131516 --- [  restartedMain] c.s.c.d.lifecycle.LifeCycleComponent     : customized destroy method
```





## 3.8 스프링 빈 고급 정의

- @Primary 애너테이션을 사용하여 같은 클래스 타입의 여러 스프링 빈이 컨테이너에 존재할 때 어떤 빈을 주입할지 결정 할 수 있다.

```java
package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.PriceUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Locale;

@Slf4j
@SpringBootApplication
public class SpringBean07Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean07Application.class, args);
        PriceUnit priceUnit = context.getBean(PriceUnit.class);
        log.info("Locale in PriceUnit : {}", priceUnit.getLocale().toString());
        context.close();
    }

    @Primary
    @Bean
    public PriceUnit primaryPriceUnit() {
        return new PriceUnit(Locale.US);
    }

    @Bean
    public PriceUnit secondaryPriceUnit() {
        return new PriceUnit(Locale.KOREA);
    }
}
```



- @Lazy 애너테이션을 사용하면 스프링 빈 설정을 읽고 스프링 빈 객체를 생성하지 않고, 의존성이 주입되는 시점에 빈 객체를 생성한다. 

```java
package com.sjbaek.chapter3;

import com.sjbaek.chapter3.domain.PriceUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.Locale;

@Slf4j
@SpringBootApplication
public class SpringBean09Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBean09Application.class, args);
        log.info("------ Done to initialize spring beans");
        PriceUnit priceUnit = context.getBean("lazyPriceUnit", PriceUnit.class);
        log.info("Locale in PriceUnit : {}", priceUnit.getLocale().toString());
        context.close();
    }

    @Lazy
    @Bean
    public PriceUnit lazyPriceUnit() {
        log.info("initialize lazyPriceUnit");
        return new PriceUnit(Locale.US);
    }
}
```

```shell
2023-05-09T16:02:11.617+09:00  INFO 130088 --- [  restartedMain] c.s.chapter3.SpringBean09Application     : ------ Done to initialize spring beans
2023-05-09T16:02:11.618+09:00  INFO 130088 --- [  restartedMain] c.s.chapter3.SpringBean09Application     : initialize lazyPriceUnit
2023-05-09T16:02:11.618+09:00  INFO 130088 --- [  restartedMain] c.s.chapter3.SpringBean09Application     : Locale in PriceUnit : en_US
```



## 3.9 용어 정리

- 스프링 빈 
  - 스프링 빈은 객체와 이름, 클래스 타입 정보가 스프링 컨테이너로 관리되는 객체를 의미한다.
- 자바 빈
  - 기본 생성자가 선언되어 있고, getter/setter 패턴으로 클래스 내부 속성에 접근할 수 있어야 하며, java.io.Serializable을 구현하고 있어야 한다.
- DTO(Data Transfer Object)
  - DTO는 소프트웨어 사이에 데이터를 전달하는 객체이기 때문에 비즈니스 로직이 없어야 하며, getter 메서드가 필요하다. 
- 값 객체(Value Object: VO)
  - 특정 데이터를 추상화하여 데이터를 표현하는 객체를 의미한다. 따라서 equals 매서드를 구현하여 비교를 수행할 수 있어야 하며, DDD에서는 불변 속성을 가지고 있어야 한다. 



- DTO, 값 객체는 불변 객체이어야 하며 설계 방법은 다음과 같다
  - 클래스를 반드시 final로 선언한다.
  - 클래스의 멤버 변수를 반드시 final로 선언한다.
  - 생성자를 직접 선언하여 기본 생성자가 있지 않도록 한다.
  - 멤버 변수에서는 setter 메서드를 만들지 말고 getter 메서드를 만들어서 사용한다.

```java
package com.sjbaek.chapter3.domain;

import java.io.Serializable;
import java.util.Currency;
import java.util.Objects;

public final class Money implements Serializable {
    private final Long value;
    private final Currency currency;

    public Money(Long value, Currency currency) {
        if(value == null || value < 0) {
            throw new IllegalArgumentException("invalid value=" + value);
        }
        
        if(currency == null) {
            throw new IllegalArgumentException("invalid currency");
        }
        
        this.value = value;
        this.currency = currency;
    }

    public Long getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value) && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }
}
```

- final class 선언으로 불변 객체, 멤버 필드에 final로 불변, equals 구현, getter 선언, 생성자 직접 지정 















