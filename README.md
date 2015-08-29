# Robolectric-Gradle-Testing

1. [English](#)

Robolectric, unit testleri oluşturmak için geliştirilmiş bir frameworktur.Robolectric testleri JVM üzerinde koşar, herhangi bir cihaz veya emülatör üzerinde çalışmaz.

JVM üzerinde çalıştığı için CI(continuous integration) için bize çok büyük kolaylık sağlar.Herhangi ek bir konfigürasyon yapmadan unit testlerimizi Jenkins gibi CI tool yardımıyla ile çalıştırabiliriz.

Robolectric bize view üzerinde elementlere,resource kaynaklarına erişmeyi sağlamıştır.

## Robolectric kurulumu ve konfigürasyon ayarları

Android studio oluşturduğumuz başlangıç projesinde dosya hiyerarşisinde **androidTest** ve **main** dosyaları gelmektedir.Robolectric ile unit testleri çalıştırabilmemiz için **test/java** adında bir dosya oluşturmak gerekmektedir.

![Build Variants](http://www.gokhankaradas.com/wp-content/uploads/2015/08/studio.jpg)

Ardından Android Studio solunda bulunan Build Variants kısmından Test Artifactini **Unit Test** olarak seçelim. Yukarıda görebilirsiniz.

## Gradle Plugin Gerekli Bağımlılıkların Yüklenmesi

```java
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.2.0'
    // Junit ve roboelectric tanımlanması
    //Test Framework
    testCompile 'junit:junit:4.12'
    testCompile "org.robolectric:robolectric:3.0"
}
```

Örnek bir test yazımı ve çalıştırılması.

Activity içinde yaptığımız basit işlemler.
```java
public class MainActivity extends Activity {

    private Button mBtn;
    private TextView mTextView;
    private ListView mListView;
    private List<String> stringList;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = (Button) findViewById(R.id.test_button);
        mTextView = (TextView) findViewById(R.id.textView);
        mListView = (ListView) findViewById(R.id.root_listview);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mTextView.setText("changed");
            }
        });
        adapter();
    }

    private void adapter() {

        stringList = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            stringList.add(String.valueOf(i));
        }

        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringList);
        mListView.setAdapter(listAdapter);
    }
```

Layout tarafında oluşturduğumuz bileşenler
```java
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools" android:layout_width="match_parent"
    android:layout_height="match_parent" android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    android:paddingBottom="@dimen/activity_vertical_margin" tools:context=".MainActivity"
    android:id="@+id/root_layout">

    <TextView android:text="@string/hello_world" android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/textView" />

    <Button
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="New Button"
        android:id="@+id/test_button"
        android:layout_below="@+id/textView"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true" />

    <ListView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/root_listview"
        android:layout_alignParentBottom="true"
        android:layout_toRightOf="@+id/test_button"
        android:layout_toEndOf="@+id/test_button" />

</RelativeLayout>
```

### Test Kodlarının yazılması
```java
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
```

![Sonuclarin Görüntülenmesi](http://www.gokhankaradas.com/wp-content/uploads/2015/08/test_results-1024x317.jpg)
