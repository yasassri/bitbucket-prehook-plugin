package ut.org.ycr.bitbucket;

import org.junit.Test;
import org.ycr.bitbucket.api.MyPluginComponent;
import org.ycr.bitbucket.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest {
    @Test
    public void testMyName() {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent", component.getName());
    }
}