### PullUpLoadHelper

非入侵式实现RecyclerView加载更多功能

#### 使用方式

    //首先设置Recyclerview的LayoutManager和Adapter
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mAdapter);
    
    //创建PullUpLoadHelper，传入RecyclerView和加载更多回调
    mPullUpLoadHelper = new PullUpLoadHelper(mRecyclerView, new PullUpLoad.OnPullUpLoadListener() {
        @Override
        public void onLoad() {
            getNextPage();
        }
    });
    
    //加载完成后调用PullUpLoadHelper的setLoaded方法
    private void getNextPage(){
    mPullUpLoadHelper.setLoading();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = getData();
                mAdapter.addItems(data);
                mPullUpLoadHelper.setLoaded();
            }
        },1000);
    }
    
