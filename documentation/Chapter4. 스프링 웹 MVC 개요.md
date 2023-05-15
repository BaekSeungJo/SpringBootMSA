# 스프링 웹 MVC 개요



## 4.2 스프링 웹 MVC 프레임워크

### 4.2.1 MVC 패턴

![스프링 MVC 프레임워크를 이용한 웹 서버 애플리케이션](https://github.com/BaekSeungJo/Diagram/assets/12541721/d3729609-930c-44fb-bb58-ba27e6f8cbd7)

1. 사용자의 모든 요청은 DispatcherServlet이 우선적으로 처리한다.
2. HTTP 메세지의 요청 라인에 요청된 리소스에 따라 어떤 컨트롤러 스프링 빈이 처리할지 판단하고, 적절한 Controller 스프링 빈으로 사용자 요청이 전달한다.
3. 컨트롤러 스프링 빈은 서비스 기능을 구현한 서비스(@Service), 컴포넌트(@Component), 리포지토리(@Repository) 스프링 빈들의 메서드들을 호출한다. 여러 객체의 메서드들이 조합되어 실행되고, 그 결과를 다시 컨트롤러 클래스가 받아 객체를 리턴하거나 기능을 종료한다.
4. 컨트롤러 클래스는 ModelAndView 객체를 이용하여 UI를 처리할 수 있는 View를 찾아 3에서 리턴하는 데이터와 함께 전달할 수 있다. 
5. 선택된 View는 전달받은 데이터를 매핑하여 사용자가 요청한 대로 응답 데이터를 생성한다. 그리고 생성된 응답 데이터는 다시 DispatcherServlet에 전달되어 클라이언트에 전달된다. 



### 4.2.2 DispatcherServlet

![스프링 MVC 컴포넌트와 동작 순서](https://github.com/BaekSeungJo/Diagram/assets/12541721/e2868f05-ab6d-4545-85e3-89cf3eae4602)

1. 클라이언트에서 전송된 모든 HTTP 요청 메시지는 가장 먼저 DispatcherServlet이 받아 처리한다.
2. DispatcherServlet은 요청 메시지의 요청 라인과 헤더들을 파악한 후 어떤 컨트롤러 클래스의 어떤 메서드로 전달할지 HandlerMapping 컴포넌트의 메서드를 사용하여 확인한다.
3. DispatcherServlet은 사용자 요청을 처리하기에 적합한 컨트롤러 클래스에 HTTP 요청 메시지를 전달하기 위해 전달 역할을 하는 HandlerAdapter에 전달한다. 
4. HandlerAdapter는 해당 컨트롤러 클래스에 클라이언트 요청을 전달한다.
5. 컨트롤러 클래스는 개발자가 개발한 비즈니스 로직을 실행한다. 비즈니스 로직은 개발자가 정의한 스프링 빈과 일반 자바 객체의 조합으로 구성된다. 실행한 결과는 다시 컨트롤러 클래스로 전달되고, 컨트롤러 클래스는 이 데이터를 어떤 뷰로 전달할지 결정한다. 
6. HandlerAdapter는 처리할 뷰와 뷰에 맵핑할 데이터를 ModleAndView 객체에 포함하여 DispatcherServlet에 전달한다.
7. DispatcherServlet은 처리할 뷰 정보를 ViewResolver에 확인한다.
8. DispatcherServlet은 View에 데이터를 전달하고, View는 데이터를 HTML,XML 등 적합한 포맷으로 변환한다. 그리고 변환한 데이터를 DispatcherServlet으로 전달한다.
9. DispatcherServlet은 최종적으로 변환된 데이터를 클라이언트에 전달한다. 



- HandlerMapping
  - org.springframework.web.servlet.HandlerMapping 인터페이스를 구현한 컴포넌트이다. HttpServletRequest 객체를 받아 사용자 요청을 처리하는 핸들러 객체를 조회하는 getHandler( ) 메서드를 제공한다. 리턴받은 핸들러 객체는 org.springframework.web.servlet.HandlerExecutionChain 이며, 어떤 클래스의 어떤 메서드인지 알 수 있다.
  - 스프링 부트 프레임워크 기본 설정으로 실행 시 RequestMappingHandlerMapping 구현체가 실행된다. 해당 구현체는 @ReuqestMapping 애너테이션의 속성 정보를 로드 할수 있다.
- HandlerAdapter
  - org.springframework.web.servlet.HandlerAdapter 인터페이스를 구현한 컴포넌트이다. 사용자의 요청과 응답을 추상화한 HttpServletRequest, HttpServletResponse 객체를 컨트롤러 클래스의 메서드에 전달하는 오브젝트 어댑터 역할을 한다. ModelAndView 를 리턴하는 handle( ) 메서드를 제공하며, RequestMappingHandlerMapping 과 한 쌍으로 RequestMappingHandlerAdapter 구현체를 많이 사용한다. 
- ModelAndView
  - 컨트롤러 클래스가 처리한 결과를 어떤 뷰에서 처리할지 결정하고 뷰에 전달할 데이터를 포함하는 클래스이다.
- ViewResolver
  - 문자열 기반의 View 이름을 실제 View 구현체로 변경한다.



### 4.2.3 서블릿 스택과 스레드 모델

- 스프링 웹 MVC 프레임워크는 5.0 버전부터 두 가지 방식으로 설정할 수 있다.
- 동기식 프로그래밍 애플리케이션을 개발할 경우 서블릿 스택을 사용하고 비동기식 프로그래밍 애플리케이션을 개발할 경우 리엑티브 스택을 사용한다. 



![WAS의 스레드 풀과 스프링 애플리케이션의 동작](https://github.com/BaekSeungJo/Diagram/assets/12541721/58860e5d-3363-4f69-a770-84e543fd8afe)

- WAS는 스레드를 효율적으로 관리하고자 스레드들을 관리하는 스레드 풀을 포함한다.
- 사용자의 요청부터 응답까지 하나의 스레드에서 모든 작업이 실행된다. (Thread Per Request)
- 사용자 요청과 스레드 생명주기가 일치하므로 쉽게 개발 및 운영할 수 있다.



### 4.2.4 스프링 부트 설정

- spring-boot-starter-web 의존성 추가시 spring-boot-autoconfiguration 프로젝트의 org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration 가 실행된다.
- 웹 애플리케이션의 주요 컴포넌트를 설정하여, EnableWebMvcConfiguration, WebMvcAutoConfigurationAdapter 클래스를 포함하는 구조다.
- EnableWebMvcConfiguration 클래스는 MVC 프레임워크의 RequestMappingHandlerMapping, RequestMappingHandlerAdapter 컴포넌트를 생성하고 설정한다.
- WebMvcAutoConfiguration 클래스는 HttpMessageConverter을 설정할 수 있는 configureMessageConverter 메서드와 같은 MVC 애플리케이션을 구성할 수 있는 여러 추상 메서드를 제공한다. 



### 4.4.1 @ResponseBody와 HttpMesageConverter

```java
package org.springframework.http.converter;

public interface HttpMessageConverter<T> {

	boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);

	boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);

	default List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
		return (canRead(clazz, null) || canWrite(clazz, null) ?
				getSupportedMediaTypes() : Collections.emptyList());
	}

	T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException;

	void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException;
}
```

- MediaType은 HTTP 헤더의 Content-type or Accept 헤더와 호환된다. 
- canRead 메서드는 클라이언트에서 받은 MediaType을 clazz 타입으로 변경할 수 있는지 확인
- canWrite 메서드는 클라이언트에 응답할 clazz 타입의 객체를 MediaType 메시지로 변경할 수 있는지 확인
- read 메서드는 클라이언트로부터 받은 메시지를 clazz 객체로 변경한다.
- write 메서드는 클라이언트에게 응답할 메시지, 즉 outputMessage 객체에 MediaType으로 변경하여 작성한다.

- REST-API는 JSON 형식을 사용하며 HttpMessageConverter 구현체 중 하나인 MappingJackson2HttpMessageConverter가 스프링 MVC에서 이미 제공한다.



























