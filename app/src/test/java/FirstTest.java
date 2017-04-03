import android.content.Intent;
import android.os.Bundle;

import com.rollercoders.smartkedex.Welcome;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by TheFe on 26/03/17.
 */

public class FirstTest {

    Welcome welcome;
    Bundle savedInstanceState = null;

    @Before
    public void setUp () {
        welcome = new Welcome();
    }

    @Test
    public void Test01 () {
        Intent i = new Intent(null, Welcome.class);
    }
}
