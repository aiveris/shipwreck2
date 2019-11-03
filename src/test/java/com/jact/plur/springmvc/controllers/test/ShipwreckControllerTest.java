package com.jact.plur.springmvc.controllers.test;

import com.jact.plur.springmvc.models.Shipwreck;
import com.jact.plur.springmvc.repositories.ShipwreckRepository;
import com.jact.plur.springmvc.repositories.ShipwreckRepositoryImpl;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jact.plur.springmvc.controllers.ShipwreckController;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Spring Boot testing with JUnit5
 * https://developer.okta.com/blog/2019/03/28/test-java-spring-boot-junit5
 *
 * The report about tests is to be found in
 * ../SpringMVC/target/site/surefire-report.html
 *
 * mvn surefire-report:report
 *
 * */

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration("classpath:resources")
@EnableWebMvc
@ComponentScan("com.jact.plur.springmvc.config")
class ShipwreckControllerTest {

    private static final Logger log = LoggerFactory.getLogger( ShipwreckControllerTest.class );

    @Autowired
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private ShipwreckController sc;

    @Autowired
    private ShipwreckRepositoryImpl shipwreckRepositoryImpl;

    protected MockMvc mockMvc;

    @Autowired
    MockHttpServletRequest mockHttpServletRequest;

    @Value("localhost")
    String serverName;

    @Value("8081")
    Integer serverPort;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @Tag("gets")
    @DisplayName("GET all ships REST test")
    public void testAllShips( TestInfo testInfo ) throws Exception {

        log.trace("Testing REST: " + testInfo.getDisplayName() + " started. ");

            assertNotNull(webApplicationContext);
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                    .alwaysDo(MockMvcResultHandlers.print())
                    .build();

            assertNotNull(mockHttpServletRequest);

            mockHttpServletRequest.setCharacterEncoding("UTF-8");
            mockHttpServletRequest.setScheme("http");
            mockHttpServletRequest.setServerName( this.serverName );
            mockHttpServletRequest.setServerPort( this.serverPort );
            mockHttpServletRequest.setRequestURI( "/api/v1/shipwrecks" );

            RequestContextHolder.setRequestAttributes(
                    new ServletRequestAttributes(mockHttpServletRequest));

            log.info( "REQ: " +
                    createURL( mockHttpServletRequest, mockHttpServletRequest.getRequestURI() ) );

            mockMvc.perform(get(mockHttpServletRequest.getRequestURI() )
                    .header("Host", "localhost")
                    .contextPath( mockHttpServletRequest.getContextPath() )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(new RequestPostProcessor() {
                public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                    request.setServerName( mockHttpServletRequest.getServerName() );
                    return request;
                }}).accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();
    }

    @Test
    @Tag("gets")
    @DisplayName("GET from the Repository test")
    public void testSelectShips( TestInfo testInfo ) throws Exception {

        log.trace("Testing SELECT: " + testInfo.getDisplayName() + " started. ");

        try {

            assertNotNull(this.shipwreckRepositoryImpl);
            Optional<Shipwreck> sw = Optional.of( shipwreckRepositoryImpl.findById(1l) );

            log.info( "RSP: " + ( sw.orElseThrow( () -> new RuntimeException( "" )) )+ "\n" +
            createURL( mockHttpServletRequest, mockHttpServletRequest.getRequestURI() ));

            log.info( "1st SHIP: " + sw.get().getName() );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void init(TestInfo testInfo) {

        String displayName = testInfo.getDisplayName();
        String methodName = testInfo.getTestMethod().get().getName();

    }

    protected static String createURL(HttpServletRequest request, String resourcePath) {

        int port = request.getServerPort();
        StringBuilder result = new StringBuilder();
        result.append(request.getScheme())
                .append("://")
                .append(request.getServerName());

        if ( (request.getScheme().equals("http") && port != 80)
                || (request.getScheme().equals("https") && port != 443) ) {
            result.append(':')
                    .append(port);
        }

        result.append(request.getContextPath());

        if(resourcePath != null && resourcePath.length() > 0) {
            if( ! resourcePath.startsWith("/")) {
                result.append("/");
            }
            result.append(resourcePath);
        }

        return result.toString();

    }

};
