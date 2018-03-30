package ohmicode.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void testDeposit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/2/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.post("/deposit/2/50").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("150.00")));
    }

    @Test
    public void testWithdrawSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/1/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/30").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("70.00")));
    }

    @Test
    public void testWithdrawError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/200").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("70.00")));
    }

    @Test
    public void testMoveError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/move/1/2/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("70.00")));
    }

    @Test
    public void testMoveSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/move/1/2/10.50").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("59.50")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("160.50")));
    }

}
