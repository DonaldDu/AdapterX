# AdapterX  [![](https://jitpack.io/v/DonaldDu/AdapterX.svg)](https://jitpack.io/#DonaldDu/AdapterX)

快捷定义RecyclerView.Adapter的方法


```

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val datas = (1..5).toList()
        val adapter = AdapterX(this, Holder::class, datas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            Toast.makeText(this, "position ${it.postion}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *  通过【参数】指定layout，方便查看。【支持】Lib 项目。推荐使用，App和库项目都支持。
     * */
    private class Holder(v: View) : IViewHolder<Int>(v, R.layout.item) {
        override fun update(data: Int, position: Int) {
            tvName.text = "data: $data"
            tvCode.text = "position $position"
        }
    }
```


