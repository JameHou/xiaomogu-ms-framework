import com.github.xiaomogu.framework.web.ApplicationRun;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @auther JameHou
 * @date 2019/1/7 16:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationRun.class})
@WebAppConfiguration
@Slf4j
public class ApplicationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }



    @Test
    public void testGetUser(){
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/hello/getUser")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .param("name", "jamehou").param("age", "18")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
