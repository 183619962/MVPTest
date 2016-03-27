[toc]

这段时间看了不少基于MVP设计模式，然后结合RxJava+Retrofit写的开源项目，深受感触，为了能让更多像我这种基层码畜也能够体验一把大神们的世界，下面分享一点学习经验。

##什么是MVP
- **model**
	处理业务逻辑，主要是数据读写，或者与后台通信，说通俗点就是取数据的地方。

- **view**
	用于更新UI，由于Android中与用户交互的只要是activity或fragment，所以，view一般就是值activity或fragment

- **presenter**
	代理，用于协调管理model和view，通知model获取数据，model获取数据完之后，通知view更新界面

	

##为什么要用MVP
使用有一个最大的好处就是解耦，view就只负责更新UI，显示控件，完成与用户的交互；model的职责呢就是去加载数据；具体的model什么时候去获取数据，获取完了之后ui什么时候去更新，这一切都是由presenter去完成。这样做，一方面适合团队协作去开发，另一方面也方便测试，各个模块之间互不干扰。还有更多的好吃和有点请自行百度。


##怎么去完成一个MVP的设计呢
所谓的MVP，其实说通俗一点就是将功能拆分成各个模块，各自完成之后通过回调去做数据交互。前端（activity且已经实现了view接口）在实例化presenter的时候会将自己的view接口回调告诉它（presenter），此时UI的管理权就交给了presenter；presenter在实例化model对象的实例的时候，会将自己的接口回调传递给model对象（有人会说，这样就其实是把管理presenter的管理权交给了model，为什么这么写呢，因为网络请求都是需要耗时的，如果在presenter里面去调用model方法，并等待返回的话，会造成线程阻塞；如果不是耗时的操作，那么就可以在model中暴露方法让presenter去获取数据），model在获取完数据之后，通过presenter的回调告诉他，presenter在接受到model的数据之后又通过先前持有的view的回调将数据或者提示等信息告知UI并更新。如下图：

![](http://img.blog.csdn.net/20160327195423166)

---

>接下来，我以一个通过接口**获取号码归属地**的例子去剖析一下MVP，同时简单结合RxJava+Retrofit，最终效果如下图。
![](http://img.blog.csdn.net/20160327210817506)

##demo实例,功能虽小，五脏俱全

###准备工作

- 项目目录结构，如下图
	![](http://img.blog.csdn.net/20160326215811371)








- 获取号码归属地的接口及回复的说明
>接口地址：http://api.k780.com:88/?app=phone.get&phone=13888888888&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
>成功返回数据如下：
>{
    "success": "1",
    "result": {
        "status": "ALREADY_ATT",
        "phone": "13888888888",
        "area": "0871",
        "postno": "650000",
        "att": "中国,云南,昆明",
        "ctype": "中国移动138卡",
        "par": "1388888",
        "prefix": "138",
        "operators": "中国移动",
        "style_simcall": "中国,云南,昆明",
        "style_citynm": "中华人民共和国,云南省,昆明市"
    }
}
> 
>失败返回：
>{
    "success": "0",
    "msgid": "1000801",
    "msg": "手机号码不正确"
}
注：以上测试电话号码为任意输入的号码，无任何其他用意，还请机主能谅解。


- 定义收据归属地的实体类
	```
	import com.lpf.mvptest.base.BaseBean;
	
	/**
	 * 电话号码的归属地及其他信息的对象
	 * Created by Administrator on 2016/3/23.
	 */
	public class PhoneNumInfo extends BaseBean {
	
	    private ResultEntity result;
	    /**
	     * msg : 手机号码不正确
	     * msgid : 1000801
	     */
	
	    private String msg;
	
	    private String msgid;
	
	    public ResultEntity getResult() {
	        return result;
	    }
	
	    public void setResult(ResultEntity result) {
	        this.result = result;
	    }
	
	    public String getMsg() {
	        return msg;
	    }
	
	    public void setMsg(String msg) {
	        this.msg = msg;
	    }
	
	    public String getMsgid() {
	        return msgid;
	    }
	
	    public void setMsgid(String msgid) {
	        this.msgid = msgid;
	    }
	
	    public static class ResultEntity {
	        private String status;
	        private String phone;
	        private String area;
	        private String postno;
	        private String att;
	        private String ctype;
	        private String par;
	        private String prefix;
	        private String operators;
	        private String style_simcall;
	        private String style_citynm;
	
	        public String getStatus() {
	            return status;
	        }
	
	        public void setStatus(String status) {
	            this.status = status;
	        }
	
	        public String getPhone() {
	            return phone;
	        }
	
	        public void setPhone(String phone) {
	            this.phone = phone;
	        }
	
	        public String getArea() {
	            return area;
	        }
	
	        public void setArea(String area) {
	            this.area = area;
	        }
	
	        public String getPostno() {
	            return postno;
	        }
	
	        public void setPostno(String postno) {
	            this.postno = postno;
	        }
	
	        public String getAtt() {
	            return att;
	        }
	
	        public void setAtt(String att) {
	            this.att = att;
	        }
	
	        public String getCtype() {
	            return ctype;
	        }
	
	        public void setCtype(String ctype) {
	            this.ctype = ctype;
	        }
	
	        public String getPar() {
	            return par;
	        }
	
	        public void setPar(String par) {
	            this.par = par;
	        }
	
	        public String getPrefix() {
	            return prefix;
	        }
	
	        public void setPrefix(String prefix) {
	            this.prefix = prefix;
	        }
	
	        public String getOperators() {
	            return operators;
	        }
	
	        public void setOperators(String operators) {
	            this.operators = operators;
	        }
	
	        public String getStyle_simcall() {
	            return style_simcall;
	        }
	
	        public void setStyle_simcall(String style_simcall) {
	            this.style_simcall = style_simcall;
	        }
	
	        public String getStyle_citynm() {
	            return style_citynm;
	        }
	
	        public void setStyle_citynm(String style_citynm) {
	            this.style_citynm = style_citynm;
	        }
	
	        @Override
	        public String toString() {
	            return "ResultEntity{" +
	                    "status='" + status + '\'' +
	                    ", phone='" + phone + '\'' +
	                    ", area='" + area + '\'' +
	                    ", postno='" + postno + '\'' +
	                    ", att='" + att + '\'' +
	                    ", ctype='" + ctype + '\'' +
	                    ", par='" + par + '\'' +
	                    ", prefix='" + prefix + '\'' +
	                    ", operators='" + operators + '\'' +
	                    ", style_simcall='" + style_simcall + '\'' +
	                    ", style_citynm='" + style_citynm + '\'' +
	                    '}';
	        }
	    }
	
	    @Override
	    public String toString() {
	        return "PhoneNumInfo{" +
	                "success='" + success + '\'' +
	                ", result=" + result +
	                '}';
	    }
	}
	```


####View的基类
- 构建view的基础接口，一个界面，请求一次数据基本都分为下面几个步骤：**显示加载框-->加载数据成功(加载失败)-->更新UI(提示用户)-->关闭正在加载的框**这么5个事情，那么我们就定义一个需要做这5件事儿的接口，由于是基类，所以返回的对象由具体的业务子类去定义就好。最终这个类的实现我们在activity或者fragment中去完成，以下为view接口的基类
	```
	/**
	 * 视图基类
	 */
	public interface IBaseView<T> {
	    /**
	     * 通过toast提示用户
	     *
	     * @param msg        提示的信息
	     * @param requestTag 请求标识
	     */
	    void toast(String msg, int requestTag);
	
	    /**
	     * 显示进度
	     *
	     * @param requestTag 请求标识
	     */
	    void showProgress(int requestTag);
	
	    /**
	     * 隐藏进度
	     *
	     * @param requestTag 请求标识
	     */
	    void hideProgress(int requestTag);
	
	    /**
	     * 基础的请求的返回
	     *
	     * @param data
	     * @param requestTag 请求标识
	     */
	    void loadDataSuccess(T data, int requestTag);
	
	    /**
	     * 基础请求的错误
	     *
	     * @param e
	     * @param requestTag 请求标识
	     */
	    void loadDataError(Throwable e, int requestTag);
	}
	```

####presenter的基类
- 定义代理（presenter）基类
	```
		/**
	 * 代理基类
	 */
	public interface IBasePresenter {
	
	    /**
	     * 开始<br>
	     * 用于做一些初始化的操作
	     */
	    void onResume();
	
	    /**
	     * 销毁<br>
	     * 用于做一些销毁、回收等类型的操作
	     */
	    void onDestroy();
	}
	```
	
- 定义presenter的回调接口，用于model做完数据交互之后通知给presenter
	```
	/**
	 * 请求数据的回调<br>
	 * Presenter用于接受model获取（加载）数据后的回调
	 * Created by Administrator on 2016/3/23.
	 */
	public interface IBaseRequestCallBack<T> {
	    /**
	     * 开始请求之前
	     */
	    void beforeRequest(int requestTag);
	
	    /**
	     * 请求失败
	     *
	     * @param e 失败的原因
	     */
	    void requestError(Throwable e, int requestTag);
	
	    /**
	     * 请求结束
	     */
	    void requestComplete(int requestTag);
	
	    /**
	     * 请求成功
	     *
	     * @param callBack 根据业务返回相应的数据
	     */
	    void retuestSuccess(T callBack, int requestTag);
	}
	```
- 写一个presenter的具体实现的基础类**BasePresenterImpl**
> 或许会问，具体的实现放到具体的presenter的业务中去写不就好了嘛，何必要在这里写一遍呢，又做不了什么事情。NO！NO！NO！你想错了，是否还记得前面定义的IBaseView，里面定义了一些基本的UI操作；在这个BasePresenterImpl中，我们可以做一些基础的事情（所有请求都会有的，假如：打开Loading弹框），那么就不用在每个子类里面都要去写一次。同时这个类接受2个泛型T，用于分别指定View视图(T)及请求返回的结果(V)
	```
	/**
	 * 代理对象的基础实现
	 *
	 * @param <T> 视图接口对象(view) 具体业务各自继承自IBaseView
	 * @param <V> 业务请求返回的具体对象
	 */
	public class BasePresenterImpl<T extends IBaseView, V> implements IBasePresenter, IBaseRequestCallBack<V> {
	    public IBaseView iView;
	
	    /**
	     * 构造方法
	     *
	     * @param view 具体业务的接口对象
	     */
	    public BasePresenterImpl(T view) {
	        this.iView = view;
	    }
	
	    @Override
	    public void beforeRequest(int requestTag) {
	        //显示LOading
	        iView.showProgress(requestTag);
	    }
	
	    @Override
	    public void requestError(Throwable e, int requestTag) {
	        //通知UI具体的错误信息
	        iView.loadDataError(e,requestTag);
	    }
	
	    @Override
	    public void requestComplete(int requestTag) {
	        //隐藏Loading
	        iView.hideProgress(requestTag);
	    }
	
	    @Override
	    public void retuestSuccess(V callBack, int requestTag) {
	        //将获取的数据回调给UI（activity或者fragment）
	        iView.loadDataSuccess(callBack, requestTag);
	    }
	
	    @Override
	    public void onResume() {
	
	    }
	
	    @Override
	    public void onDestroy() {
	
	    }
	}
	```
	
		>代码中可以看到，加载前的弹框，加载成功回调给UI，加载失败通知UI错误信息，加载完成关闭弹框等都已经在这里做了一个基础的实现。如果其中的方法不能满足你的业务需求，你可以在具体业务的presenter实现中去重写相应方法添加具体缺失的实现。

####Model的基类
- 业务（model）类的基类
	>其中有写到RetrofitManager这个类，在下面将会介绍，这是一个用于初始化retrofit和service的类，也就是model的辅助对象。
	
	```
	/**
	 * 业务对象的基类
	 */
	public class BaseModel {
	    //retrofit请求数据的管理类
	    public RetrofitManager retrofitManager;
	
	    public BaseModel() {
		    //初始化retrofit
	        retrofitManager = RetrofitManager.builder();
	    }
	
	}
	```


- 定义一个请求归属地的服务

	```
	
	/**
	 * 归属地请求的服务
	 */
	public interface PhoneNunInfoService {
	    //http://api.k780.com:88/?app=phone.get&phone={phoneNum}&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json
	    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
	    @GET("/")
	    Observable<PhoneNumInfo> getBeforeNews(@Query("app") String app
	            , @Query("phone") String phone
	            , @Query("appkey") String appkey
	            , @Query("sign") String sign
	            , @Query("format") String format);
	
	}
	```

- 创建RetrofitManager对象，他的作用就是初始化retrofit、service以及添加缓存机制（如果不理解可以不关注他，将那一块的代码注释掉依然是可以运行的）
	```
	/**
	 * Retrofit管理类
	
	 */
	public class RetrofitManager {
	
	    //地址
	    public static final String BASE_PHONENUMINFO_URL = "http://api.k780.com:88";
	
	    //短缓存有效期为1分钟
	    public static final int CACHE_STALE_SHORT = 60;
	    //长缓存有效期为7天
	    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;
	
	    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
	
	    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
	    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_LONG;
	    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0时则不会使用缓存而请求服务器
	    public static final String CACHE_CONTROL_NETWORK = "max-age=0";
	    private static OkHttpClient mOkHttpClient;
	    private final PhoneNunInfoService phoneNunInfoService;
	
	    public static RetrofitManager builder() {
	        return new RetrofitManager();
	    }
	
	    public PhoneNunInfoService getService() {
	        return phoneNunInfoService;
	    }
	
	    private RetrofitManager() {
	        initOkHttpClient();
	
	        Retrofit retrofit = new Retrofit.Builder()
	                .baseUrl(BASE_PHONENUMINFO_URL)
	                .client(mOkHttpClient)
	                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
	                .addConverterFactory(GsonConverterFactory.create())
	                .build();
	        phoneNunInfoService = retrofit.create(PhoneNunInfoService.class);
	    }
	
	    private void initOkHttpClient() {
	        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
	        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
	        if (mOkHttpClient == null) {
	            synchronized (RetrofitManager.class) {
	                if (mOkHttpClient == null) {
	
	                    // 指定缓存路径,缓存大小100Mb
	                    Cache cache = new Cache(new File(MyApplication.getContext().getCacheDir(), "HttpCache"),
	                            1024 * 1024 * 100);
	
	                    mOkHttpClient = new OkHttpClient.Builder()
	                            .cache(cache)
	                            .addInterceptor(mRewriteCacheControlInterceptor)
	                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
	                            .addInterceptor(interceptor)
	                            .addNetworkInterceptor(new StethoInterceptor())
	                            .retryOnConnectionFailure(true)
	                            .connectTimeout(15, TimeUnit.SECONDS)
	                            .build();
	                }
	            }
	        }
	    }
	
	    // 云端响应头拦截器，用来配置缓存策略
	    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
	        @Override
	        public Response intercept(Chain chain) throws IOException {
	            Request request = chain.request();
	            if (!NetUtil.isNetworkConnected()) {
	                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
	            }
	            Response originalResponse = chain.proceed(request);
	            if (NetUtil.isNetworkConnected()) {
	                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
	                String cacheControl = request.cacheControl().toString();
	                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
	                        .removeHeader("Pragma").build();
	            } else {
	                return originalResponse.newBuilder()
	                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
	                        .removeHeader("Pragma").build();
	            }
	        }
	    };
	}
	```
>**到此，基础的东西已经完成了，这样的事情，虽然说在实现这样的小功能中确实是显的有些多余了，但是在实际的开发过程中，我们并不是只做一件事，只做一个请求，只显示一个界面，因此，这样往上抽一层还是很有必要的。下面将回归到真正写号码归属地的实际业务中来**




###具体的业务实现
- 查询号码归属地的view
>由于基础的view接口已经可以满足号码归属地查询的ui更新使用了，同时也为了项目方便管理，便于理解，我们新建一个PhoneNumInfoView，继承IBaseView，并告诉baseView请求成功后需要返回一个PhoneNumInfo的对象。
	```
	/**
	 * 号码归属地查询的View
	 */
	public interface PhoneNumInfoView extends IBaseView<PhoneNumInfo>{
	}
	```

- 定义获取归属地的Model
	```
	/**
 * 获取号码归属地的具体Model实现
 */
public class PhoneModelImpl extends BaseModel {
    private Context mContext;
    private PhoneNunInfoService phoneNunInfoService;

    public PhoneModelImpl(Context context) {
        super();
        this.mContext = context;
        phoneNunInfoService = retrofitManager.getService();
    }

    public void loadPhoneNumInfo(String phoneNum, final IBaseRequestCallBack<PhoneNumInfo> callBack, final int requestTag) {
        phoneNunInfoService.getBeforeNews("phone.get", phoneNum, "10003", "b59bc3ef6191eb9f747dd4e83c99f2a4", "json")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhoneNumInfo>() {
                    @Override
                    public void onCompleted() {
                        callBack.requestComplete(requestTag);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestError(e, requestTag);
                    }

                    @Override
                    public void onNext(PhoneNumInfo phoneNumInfo) {
                        if (null != phoneNumInfo && phoneNumInfo.getSuccess().equals("1"))
                            callBack.retuestSuccess(phoneNumInfo, requestTag);
                        else if (null != phoneNumInfo && phoneNumInfo.getSuccess().equals("0"))
                            callBack.requestError(new Exception(phoneNumInfo.getMsg()), requestTag);
                        else
                            callBack.requestError(new Exception("获取数据错误，请重试！"), requestTag);
                    }
                });
    }
}
	```
- 定义一个用于获取号码归属地的代理对象（presenter）
>在实例化代理对象的时候拿到前端（activity或者fragment）的view实例，并初始化Model对象，同时对外提供一个获取数据的方法，方便activity去获取数据。
	```
	/**
	 * Presenter的实现，协调model去加载数据，获取model加载完成时候的回调，控制界面加载框的显示与隐藏
	 */
	public class PhonePresenterImpl extends BasePresenterImpl<PhoneNumInfoView, PhoneNumInfo> {
	    private PhoneModelImpl phoneModel;
	    private Context mContext;
	
	
	    public PhonePresenterImpl(PhoneNumInfoView phoneNumInfoView, Context context) {
	        super(phoneNumInfoView);
	        this.mContext = context;
	        phoneModel = new PhoneModelImpl(mContext);
	    }
	
	    /**
	     * 获取归属地信息
	     *
	     * @param phoneNum   电话号码
	     * @param requestTag 请求标识
	     */
	    public void getPhoneNumInfo(String phoneNum, int requestTag) {
	        onResume();
	        beforeRequest(requestTag);
	        phoneModel.loadPhoneNumInfo(phoneNum, this, requestTag);
	    }
	}
	```

- 实现activity中相关的代码
>前端activity的代码并不多，就是实现PhoneNumInfoView的相应接口，然后最基础的获取控件，实例化presenter，把当前的view实例以参数的形式传递给presenter；接口的实现也就是要显示Loading的地方把框show出来，在获取数据成功的地方显示数据等等，各负其职。然后在按钮的点击事件的地方调用presenter中相应的获取数据的方法。具体的什么时候显示Loading，什么时候执行加载完成的操作等，就不需要activity去管了，安安心心的全权交个presenter去做就好了。代码如下：
	```
	public class MainActivity extends AppCompatActivity implements View.OnClickListener, PhoneNumInfoView {
	    //获取归属地的请求标识，用回页面多个地方请求的时候，标识是那一个完成并回调了
	    private static final int REQUESTMSG = 0;
	
	    //电话号码的EditText
	    private EditText phoneNum;
	    //获取归属地的按钮
	    private Button getPhoneInfo;
	    //用于显示最后获取的结果
	    private TextView msg;
	    //Loading弹框
	    private ProgressDialog progressDialog;
	    //获取归属地的代理对象
	    private PhonePresenterImpl phonePresenter;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        initView();
	
	    }
	
	    private void initView() {
	        //初始化控件
	        phoneNum = (EditText) findViewById(R.id.phonenum);
	        getPhoneInfo = (Button) findViewById(R.id.getphoneinfo);
	        msg = (TextView) findViewById(R.id.msg);
	        getPhoneInfo.setOnClickListener(this);
	
	        //初始化Loading
	        progressDialog = new ProgressDialog(this);
	        progressDialog.setMessage("Loading");
	
	        //初始化代理对象
	        phonePresenter = new PhonePresenterImpl(this, this);
	    }
	
	    @Override
	    public void toast(String msg, int requestTag) {
	
	    }
	
	    @Override
	    public void showProgress(int requestTag) {
	        if (null != progressDialog && !progressDialog.isShowing()) {
	            progressDialog.show();
	        }
	    }
	
	    @Override
	    public void hideProgress(int requestTag) {
	        if (null != progressDialog && progressDialog.isShowing()) {
	            progressDialog.dismiss();
	        }
	    }
	
	    @Override
	    public void loadDataSuccess(PhoneNumInfo phoneNumInfo, int requestTag) {
	        msg.setText(phoneNumInfo.toString());
	    }
	
	    @Override
	    public void loadDataError(Throwable e, int requestTag) {
	        msg.setText(e.getMessage());
	    }
	
	    @Override
	    public void onClick(View v) {
	        switch (v.getId()) {
	            case R.id.getphoneinfo:
	                phonePresenter.getPhoneNumInfo(phoneNum.getText().toString(), REQUESTMSG);
	                break;
	        }
	    }
	}
	```

- 效果图
	- 成功
	![](http://img.blog.csdn.net/20160327210817506)
	
	- 失败
	![](http://img.blog.csdn.net/20160327210925221)

>到这里，一个基于MVP的DEMO就写完了，其中有用到RxJava和Retrofit，但是没有做明确的说明，如果想了解可以阅读：[RxJava 与 Retrofit 结合的最佳实践](http://gank.io/post/56e80c2c677659311bed9841)