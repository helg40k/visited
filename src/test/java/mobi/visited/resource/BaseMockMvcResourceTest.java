package mobi.visited.resource;

import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration()
@ContextConfiguration("classpath:test-Context.xml")
public abstract class BaseMockMvcResourceTest {

    {
        System.setProperty("environment", "development");
    }

    protected MediaType contentType = MediaType.APPLICATION_JSON;

    protected MockMvc mockMvc;

    public String loadTemplate(String templateName, Object... args) {
        StringBuilder sb = new StringBuilder();
        try{
            final InputStream is = ClassLoader.getSystemResourceAsStream(templateName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return String.format(sb.toString(), args);
    }
}
