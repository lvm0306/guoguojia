package com.lovesosoi.kotlin_shop

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //    @BindView(R.id.tv_title)
//    lateinit var tv_title: TextView
//    @BindView(R.id.tv_weather)
//    lateinit var tv_weather: TextView
//    @BindView(R.id.tv_menu1)
//    lateinit var tv_menu1: TextView
//    @BindView(R.id.tv_menu2)
//    lateinit var tv_menu2: TextView
//    @BindView(R.id.tv_menu3)
//    lateinit var tv_menu3: TextView
//    @BindView(R.id.tv_menu4)
//    lateinit var tv_menu4: TextView
//    @BindView(R.id.rv_menu)
//    lateinit var rv_menu: RecyclerView
//    @BindView(R.id.rv_item)
//    lateinit var rv_item: RecyclerView
    lateinit var context: Context
    var orderList = mutableListOf<OrderBean>()
    var fruitList = mutableListOf<FruitBean>()
    lateinit var order_adapter: OrderAdapter
    lateinit var fruit_adapter: FruitAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        initData()
        initView()
    }

    private fun initData() {
//        orderList.add(OrderBean("黄瓜",1,1.00))
//        orderList.add(OrderBean("茄子",2,1.50))
//        orderList.add(OrderBean("豆角",3,2.00))
//        orderList.add(OrderBean("豆腐",4,2.30))

        fruitList.add(FruitBean("黄瓜", 1.00))
        fruitList.add(FruitBean("茄子", 1.30))
        fruitList.add(FruitBean("豆角", 1.80))
        fruitList.add(FruitBean("豆腐", 2.20))
        fruitList.add(FruitBean("土豆", 1.66))
        fruitList.add(FruitBean("金针菇", 2.50))
        fruitList.add(FruitBean("拉皮", 1.50))
        fruitList.add(FruitBean("肥羊粉", 2.50))
        fruitList.add(FruitBean("鲜蘑", 4.50))
        fruitList.add(FruitBean("鸡蛋", 4.20))
        fruitList.add(FruitBean("西瓜", 1.50))

    }

    private fun initView() {
        context = this
        //rv 初始化
        order_adapter = OrderAdapter(context, orderList)
        fruit_adapter = FruitAdapter(context, fruitList)
        rv_menu.adapter = order_adapter
        rv_menu.layoutManager = LinearLayoutManager(this, OrientationHelper.VERTICAL, false)
        rv_item.adapter = fruit_adapter
        rv_item.layoutManager = GridLayoutManager(this, 3)

        order_adapter.setOnItemClickListener(object : OnItemClick {
            override fun sub(position: Int, view: View, data: Any) {
                var count=orderList.get(position).count
                if (count>1){
                   orderList.get(position).count-=1
                }else{
                    orderList.removeAt(position)
                }
                order_adapter.notifyDataSetChanged()
                refreshOrder()
            }

            override fun add(position: Int, view: View, data: Any) {

                orderList.get(position).count+=1
                order_adapter.notifyDataSetChanged()
                refreshOrder()
            }

            override fun click(position: Int, view: View, data: Any) {
                if (data is OrderBean) {

                }
            }
        })

        fruit_adapter.setOnItemClickListener(object : OnItemClick {
            override fun add(position: Int, view: View, data: Any) {

            }

            override fun sub(position: Int, view: View, data: Any) {

            }

            override fun click(position: Int, view: View, data: Any) {
                if (data is FruitBean) {
                    var flag = false
                    for ((index, value) in orderList.withIndex()) {
                        flag = value.name == data.name
                        if (flag) {
                            orderList.get(index).count += 1
                            break
                        }
                    }
                    if (!flag) {
                        orderList.add(OrderBean(data.name, 1, data.price))
                    }
                    order_adapter.notifyDataSetChanged()
                    refreshOrder()
                }
            }
        })

    }

    fun refreshOrder(){
        var pay = 0.00
        var count = 0
        for ((index, value) in orderList.withIndex()) {
            pay += value.count * value.price
            count++
        }
        tv_pay.text="总计:"+String.format("%.2f",pay)+"元"
        tv_goodnum.text="共计"+count+"件货物"
    }
    @OnClick(R.id.tv_menu1, R.id.tv_menu2, R.id.tv_menu3, R.id.tv_menu4)
    fun onClick(view: View) {
        when (view.id) {
            R.id.tv_menu1 -> {
                Toast.makeText(this, tv_menu1.text, Toast.LENGTH_SHORT).show()
            }
            R.id.tv_menu2 -> {
                Toast.makeText(this, tv_menu2.text, Toast.LENGTH_SHORT).show()
            }
            R.id.tv_menu3 -> {
                Toast.makeText(this, tv_menu3.text, Toast.LENGTH_SHORT).show()
            }
            R.id.tv_menu4 -> {
                Toast.makeText(this, tv_menu4.text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
