package cz.jan;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.jan.order.InvalidateOrderJob;
import cz.jan.order.repository.OrderRepository;
import cz.jan.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
public abstract class AbstractEshopIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected InvalidateOrderJob invalidateOrderJob;

    protected ResultActions callPostWithBody(String url, Object requestBody) throws Exception {
        return mockMvc.perform(post(url)
                .content(jsonAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected <T> T callPostWithBody(String url, Object requestBody, Class<T> clazz) throws Exception {
        return parseJsonToObject(mockMvc.perform(post(url)
                        .content(jsonAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(), clazz);
    }

    protected <T> T callPostWithBodyBadRequest(String url, Object requestBody, Class<T> clazz) throws Exception {
        return parseJsonToObject(mockMvc.perform(post(url)
                        .content(jsonAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString(), clazz);
    }

    protected ResultActions callPost(String url) throws Exception {
        return mockMvc.perform(post(url));
    }

    protected String jsonAsString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected <T> T parseJsonToObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


}
