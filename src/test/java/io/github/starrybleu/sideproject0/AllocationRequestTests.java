package io.github.starrybleu.sideproject0;

import io.github.starrybleu.sideproject0.auth.JwtTokenProvider;
import io.github.starrybleu.sideproject0.entity.AllocationRequest;
import io.github.starrybleu.sideproject0.service.AllocationRequestService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = MockUserTestConfig.class
)
@AutoConfigureMockMvc
public class AllocationRequestTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    AllocationRequestService service;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserDetailsService userDetailsService;

    String passengerJwt;
    String driverJwt;

    @Before
    public void setup() {
        UserDetails passenger = userDetailsService.loadUserByUsername("passenger@company.com");
        this.passengerJwt = tokenProvider.createToken(passenger);
        UserDetails driver = userDetailsService.loadUserByUsername("driver@company.com");
        this.driverJwt = tokenProvider.createToken(driver);
    }

    @Test
    public void applicationContextLoaded() throws Exception {
        assertThat(mvc).isNotNull();
    }

    @Test
    public void 인증하지않은_승객이_본인_요청목록조회하면_403_Forbidden_예외를_받는다() throws Exception {
        mvc.perform(get("/api/allocation-requests/mine"))
                .andExpect(status().is(403))
        ;
    }

    @Test
    public void 인증된_승객이_본인_요청목록조회하면_자신의_배차요청목록을_받는다() throws Exception {
        when(service.getMyAllocationRequests(any(), any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        mvc.perform(get("/api/allocation-requests/mine")
                .header("X-AUTH-TOKEN", passengerJwt))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").value(hasKey("content")))
                .andExpect(jsonPath("$").value(hasKey("number")))
                .andExpect(jsonPath("$").value(hasKey("size")))
                .andExpect(jsonPath("$").value(hasKey("totalElements")))
                .andExpect(jsonPath("$").value(hasKey("totalPages")))
        ;
    }

    @Test
    public void 인증하지않은_기사가_전체_요청목록조회하면_403_Forbidden_예외를_받는다() throws Exception {
        mvc.perform(get("/api/allocation-requests"))
                .andExpect(status().is(403))
        ;
    }

    @Test
    public void 인증된_기사가_전체_요청목록조회하면_전체배차요청목록을_받는다() throws Exception {
        when(service.getAllocationRequests(any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        mvc.perform(get("/api/allocation-requests")
                .header("X-AUTH-TOKEN", driverJwt))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").value(hasKey("content")))
                .andExpect(jsonPath("$").value(hasKey("number")))
                .andExpect(jsonPath("$").value(hasKey("size")))
                .andExpect(jsonPath("$").value(hasKey("totalElements")))
                .andExpect(jsonPath("$").value(hasKey("totalPages")))
        ;
    }

    @Test
    public void 인증된_기사가_배차_요청을_하면_403_Forbidden_예외를_받는다() throws Exception {
        mvc.perform(post("/api/allocation-requests")
                .content("서울 강남구")
                .header("X-AUTH-TOKEN", driverJwt)
                .header("content-type", MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().is(403))
        ;
    }

    @Test
    public void 인증된_승객이_배차_요청을_하면_201_Created_응답을_받는다() throws Exception {
        AllocationRequest mockAllocationRequest = mock(AllocationRequest.class);
        when(mockAllocationRequest.getId())
                .thenReturn(1239);
        when(service.createAllocationRequest(any(), any()))
                .thenReturn(mockAllocationRequest);

        mvc.perform(post("/api/allocation-requests")
                .content("서울 강남구")
                .header("X-AUTH-TOKEN", passengerJwt)
                .header("content-type", MediaType.TEXT_PLAIN_VALUE))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id").value(1239))
        ;
    }

    @Test
    public void 인증된_승객이_배차_요청을_점유하려하면_403_Forbidden_예외를_받는다() throws Exception {
        mvc.perform(patch("/api/allocation-requests/1")
                .content("{\"driverNo\": 9}")
                .header("X-AUTH-TOKEN", passengerJwt)
                .header("content-type", MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
        ;
    }

    @Test
    public void 인증된_기사가_배차_요청을_점유하려하면_정상적인_응답을_받는다() throws Exception {
        AllocationRequest mockAllocationRequest = mock(AllocationRequest.class);
        when(mockAllocationRequest.getId())
                .thenReturn(1239);
        when(service.takeRequest(any(), any()))
                .thenReturn(mockAllocationRequest);

        mvc.perform(patch("/api/allocation-requests/1")
                .content("{\"driverNo\": 9}")
                .header("X-AUTH-TOKEN", driverJwt)
                .header("content-type", MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1239))
        ;
    }

    @Test
    public void 인증된_기사가_다른_기사로_배차_요청을_점유시도하면_403_Forbidden_예외를_받는다() throws Exception {
        mvc.perform(patch("/api/allocation-requests/1")
                .content("{\"driverNo\": 111}")
                .header("X-AUTH-TOKEN", driverJwt)
                .header("content-type", MediaType.APPLICATION_JSON))
                .andExpect(status().is(403))
        ;
    }

}
