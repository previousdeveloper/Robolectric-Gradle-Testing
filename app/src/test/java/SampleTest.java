import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import testcase.unittestwithroboelectric.BuildConfig;
import testcase.unittestwithroboelectric.MainActivity;
import testcase.unittestwithroboelectric.R;

import static junit.framework.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;



@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class SampleTest {

    private Button test_button;
    private TextView textView;
    private ListView mListView;
    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(MainActivity.class);
        test_button = (Button) activity.findViewById(R.id.test_button);
        textView = (TextView) activity.findViewById(R.id.textView);
        mListView = (ListView) activity.findViewById(R.id.root_listview);

    }

    @Test
    public void shouldMainActivityNotBeNull() throws Exception {

        assertTrue(Robolectric.buildActivity(MainActivity.class).create().get() != null);

        String hello = new MainActivity().getResources().getString(R.string.hello_world);

        Assert.assertEquals(hello, "Hello world!");
    }

    @Test
    public void shouldTextViewNameChanged() throws Exception {

        test_button.performClick();
        Assert.assertEquals(textView.getText().toString(), "changed");
    }

    @Test
    public void shouldRootLayoutLoaded() {

        Assert.assertEquals(R.id.root_layout, shadowOf(activity).getContentView().getId());
    }


    @Test
    public void shouldApplicationIdBeCorrect() {
        String appId = BuildConfig.APPLICATION_ID;
        Assert.assertEquals(appId, "testcase.unittestwithroboelectric");
    }

    @Test
    public void shouldListViewItemSizeGreaterThanO() {
        int listViewItemSize = mListView.getCount();
        Assert.assertTrue(listViewItemSize > 0);
    }

    @Test
    public void shouldButtonNameCorrect(){
        Assert.assertEquals(test_button.getText().toString(),"New Button");
    }


}


