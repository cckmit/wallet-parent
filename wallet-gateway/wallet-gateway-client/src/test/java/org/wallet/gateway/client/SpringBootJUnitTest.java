package org.wallet.gateway.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.dubbo.ServiceRequest;
import org.wallet.gateway.client.run.ServiceStarter;

/**
 * Spring Boot 测试基类
 *
 * @author zengfucheng
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceStarter.class)
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@ActiveProfiles("local")
public class SpringBootJUnitTest {
    protected Logger log = LoggerFactory.getLogger(getClass());

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Value("${spring.application.name}")
    protected String applicationName;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                // Mock方式需要自行将Filter注入到MockMvc中
                .build();
    }

    @Test
    public void loadContext() throws InterruptedException {
        Thread.sleep(20000);
    }

    protected ServiceRequest getRequest(){
        ServiceRequest request = ServiceRequest.newInstance();

        request.setAppId(applicationName);
        request.setServiceId(applicationName);

        return request;
    }

    protected <T> T get(String uri, TypeReference<SimpleResult<T>> type) {
        return get(uri, type, null);
    }

    protected <T> T get(String uri, TypeReference<SimpleResult<T>> type, ResultCode returnCode) {
        return request(uri, null, type, RequestMethod.GET, returnCode);
    }

    protected <T> T postJson(String uri, Object data, TypeReference<SimpleResult<T>> type) {
        return postJson(uri, data, type, null);
    }

    protected <T> T postJson(String uri, Object data, TypeReference<SimpleResult<T>> type, ResultCode returnCode) {
        return request(uri, data, type, RequestMethod.POST, returnCode);
    }

    protected <T> T request(String uri, Object data, TypeReference<SimpleResult<T>> type, RequestMethod method, ResultCode returnCode) {
        MockHttpServletRequestBuilder builder;
        switch (method) {
            case POST:
                builder = MockMvcRequestBuilders.post(uri);
                break;
            case GET:
            default:
                builder = MockMvcRequestBuilders.get(uri);
        }
        builder.accept(MediaType.APPLICATION_JSON_UTF8);
        if (null != data) {
            builder.contentType(MediaType.APPLICATION_JSON_UTF8);
            builder.content(JSON.toJSONBytes(data));
        }

        String responseContent;
        try {
            MockHttpServletResponse response = mockMvc.perform(builder)
                    .andDo(MockMvcResultHandlers.print())
                    .andReturn().getResponse();
            if (response.getStatus() < 200 || response.getStatus() >= 300) {
                log.debug("Http status " + response.getStatus());
            }
            if (response.getStatus() == 404) {
                throw new RuntimeException("Http 404");
            }
            responseContent = response.getContentAsString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assert.assertTrue("Response empty.", !StringUtils.isEmpty(responseContent));

        SimpleResult<T> result = JSON.parseObject(responseContent, type);

        Assert.assertNotNull("Error json syntax.", result);

        if (null != returnCode && returnCode.getCode().equals(result.getCode())) {

        } else {
            Assert.assertEquals("Error code " + result.getCode() + ": " + result.getMsg(), ResultCode.Success.getCode(), result.getCode());
        }

        return result.getData();
    }

}
