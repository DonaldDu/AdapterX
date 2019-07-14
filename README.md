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
        adapter.setOnItemClickListener { }
    }

    /**
     *通过注解的方式指定ViewHolder的layout，方便查看。
     * */
    @LayoutId(R.layout.item)
    private class Holder(v: View) : IViewHolder<Int>(v) {
        override fun update(data: Int, position: Int) {
            itemView.run {
                tvName.text = "data: $data"
                tvCode.text = "position $position"
            }
        }
    }
```


