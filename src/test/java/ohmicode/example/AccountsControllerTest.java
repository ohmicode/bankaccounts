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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.anything;
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

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/2/150").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
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

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
    }

    @Test
    public void testWithdrawError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));

        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/200").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("70.00")));

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
    }

    @Test
    public void testMoveError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));

        mvc.perform(MockMvcRequestBuilders.post("/move/1/2/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("70.00")));

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
    }

    @Test
    public void testMoveSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/1/70").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.post("/deposit/2/150").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));

        mvc.perform(MockMvcRequestBuilders.post("/move/1/2/10.50").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("59.50")));
        mvc.perform(MockMvcRequestBuilders.get("/balance/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("160.50")));

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/59.50").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/2/160.50").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
    }

    @Test
    public void testWithdrawParallel() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/deposit/1/100").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));

        requestWithdrawParallel(100);

        mvc.perform(MockMvcRequestBuilders.get("/balance/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("30.00")));

        //clean
        mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/30.00").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("success")));
    }

    private void requestWithdrawParallel(int size) {
        CountDownLatch countDownLatch = new CountDownLatch(size);

        ExecutorService executor = Executors.newFixedThreadPool(5);
        IntStream.range(0, size).forEach(i -> {
            executor.execute(withdrawRunnable(countDownLatch));
            countDownLatch.countDown();
        });
        executor.shutdown();
        try {
            executor.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            executor.shutdownNow();
        }
    }

    private Runnable withdrawRunnable(CountDownLatch barrier) {
        return () -> {
            try {
                barrier.await();

                mvc.perform(MockMvcRequestBuilders.post("/withdraw/1/70.00").accept(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(anything()));
            } catch (Exception ignored) {}
        };
    }
}
