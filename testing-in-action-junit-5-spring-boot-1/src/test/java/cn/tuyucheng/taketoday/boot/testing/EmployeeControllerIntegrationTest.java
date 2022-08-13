package cn.tuyucheng.taketoday.boot.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
//  It will autoconfigure the Spring MVC infrastructure for our unit tests.
@WebMvcTest(value = EmployeeRestController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class EmployeeControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmployeeService service;

    @BeforeEach
    public void setup() {
    }

    @Test
    @DisplayName("whenPostEmployee_thenCreateEmployee")
    void whenPostEmployee_thenCreateEmployee() throws Exception {
        Employee alex = new Employee("alex");
        given(service.save(Mockito.any())).willReturn(alex);
        mvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(alex)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("alex")));
        verify(service, VerificationModeFactory.times(1)).save(Mockito.any());
        reset(service);
    }

    @Test
    @DisplayName("givenEmployees_whenGetEmployees_thenReturnJsonArray")
    public void givenEmployees_whenGetEmployees_thenReturnJsonArray() throws Exception {
        Employee alex = new Employee("alex");
        Employee john = new Employee("john");
        Employee bob = new Employee("bob");

        List<Employee> allEmployees = Arrays.asList(alex, john, bob);

        given(service.getAllEmployees()).willReturn(allEmployees);

        mvc.perform(get("/api/employees").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(alex.getName())))
                .andExpect(jsonPath("$[1].name", is(john.getName())))
                .andExpect(jsonPath("$[2].name", is(bob.getName())));
        verify(service, VerificationModeFactory.times(1)).getAllEmployees();
        reset(service);
    }
}