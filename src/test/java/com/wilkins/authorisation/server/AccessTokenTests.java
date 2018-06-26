package com.wilkins.authorisation.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wilkins.authorisation.server.model.User;
import com.wilkins.authorisation.server.properties.ServiceProperties;
import com.wilkins.authorisation.server.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccessTokenTests {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ServiceProperties serviceProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        createTestUser();
    }

    @Test
    public void accessTokenCanBeObtainedWithValidCredentials() {
        assertNotNull(getAccessToken("test.test@foo.bar", "password", mockMvc));
    }

    private OAuth2AccessToken getAccessToken(final String username, final String password, final MockMvc mockMvc) {
        MockHttpServletResponse response;
        OAuth2AccessToken accessToken = null;
        try {
            response = mockMvc.perform(post("/oauth/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .param("grant_type", "password")
                    .param("username", username)
                    .param("password", password)
                    .with(httpBasic(serviceProperties.getClientId(), serviceProperties.getClientSecret())))
                    .andReturn().getResponse();
            accessToken = MAPPER.readValue(response.getContentAsString(), OAuth2AccessToken.class);
        } catch (Exception e) {
            fail();
        }
        assertNotNull(accessToken);
        return accessToken;
    }


    private void createTestUser() {
        User testUser = new User();
        testUser.setUsername("test.test@foo.bar");
        testUser.setPassword(passwordEncoder.encode("password"));
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        testUser.setRoles(roles);
        userRepository.save(testUser);
    }
}
