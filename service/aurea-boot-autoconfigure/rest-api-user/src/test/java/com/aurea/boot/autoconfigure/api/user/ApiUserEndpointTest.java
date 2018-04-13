package com.aurea.boot.autoconfigure.api.user;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aurea.boot.autoconfigure.api.user.ApiConsts.User.Mapping;
import com.aurea.boot.autoconfigure.api.user.json.TokenPassword;
import com.aurea.boot.autoconfigure.data.user.UserRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

public class ApiUserEndpointTest {

    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new ApiUserEndpoint(mock(UserRepository.class)))
            .addFilter(new SecurityContextPersistenceFilter()).build();

    @Test
    @WithMockUser
    public void forgotPassword() throws Exception {
        this.mockMvc.perform(post(Mapping.API_USER + Mapping.FORGOT_PASSWORD)
                .content("{\"email\": \"dummy@email.com\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void resetPassword() throws Exception {
        TokenPassword tokenPasswordJson = new TokenPassword("token", "passowrd");
        this.mockMvc.perform(post(Mapping.API_USER + Mapping.RESET_PASSWORD)
                .content(new ObjectMapper().writeValueAsString(tokenPasswordJson))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void getCurrentUser() throws Exception {
        this.mockMvc.perform(get(Mapping.API_USER + Mapping.CURRENT_USER))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void checkResetPasswordToken() throws Exception {
        this.mockMvc.perform(get(Mapping.API_USER + Mapping.CHECK_RESET_PASSWORD_TOKEN)
                .content("{\"token\": \"token\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
