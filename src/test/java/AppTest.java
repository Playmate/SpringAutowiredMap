import com.igor.autowiredmap.Application;
import com.igor.autowiredmap.business.LifeArea;
import com.igor.autowiredmap.business.Zoo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by Igor Dmitriev on 8/2/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class AppTest {

    @Autowired
    private Zoo zoo;

    @Test
    public void test() {
        String expected = "ow ow ow";
        String actual = zoo.say(LifeArea.SEA);
        Assert.assertThat(expected, is(actual));
    }
}
