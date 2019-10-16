package com.lovesosoi.kotlin_shop.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.content.ContextWrapper
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.interfaces.OnAddFruit
import kotlinx.android.synthetic.main.dialog_add_fruit.*
import org.w3c.dom.Text

/**
 * 增加水果dialog
 * 2019-7-19 Lovesosoi
 */
class AddFruitDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    var listener: OnAddFruit? = null
    var stringArray: List<String>? = null
    var etName: EditText? = null
    var etPrice: EditText? = null
    var et_unit: EditText? = null
    var tvCancel: Button? = null
    var tvSure: Button? = null
    var customerSp: Spinner? = null
    var mId: Int = 0
    var cate=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(R.layout.dialog_add_fruit)
        val activity = scanForActivity(context)
        val windowManager = activity!!.windowManager
        val display = windowManager.getDefaultDisplay()
        val lp: WindowManager.LayoutParams = window.attributes
        lp.width = display.getWidth() * 4 / 5// 设置dialog宽度为屏幕的4/5
        window?.setAttributes(lp);
        setCanceledOnTouchOutside(true)//点击外部Dialog消失
        initView()
    }

    private fun initView() {
        etName = findViewById<EditText>(R.id.et_name)
        etPrice = findViewById<EditText>(R.id.et_price)
        et_unit = findViewById<EditText>(R.id.et_unit)
        tvCancel = findViewById<Button>(R.id.tv_cancel)
        tvSure = findViewById<Button>(R.id.tv_sure)
        customerSp = findViewById<Spinner>(R.id.customer_spinner)

        stringArray = (context.resources.getStringArray(R.array.unit)).toList()
        val startAdapter = ArrayAdapter(context, R.layout.spinner_fruit, stringArray)
        startAdapter.setDropDownViewResource(R.layout.spinner_fruit)

        val fruitspinneradapter =
                ArrayAdapter(context, R.layout.item_fruit_spinner, stringArray)
        fruitspinneradapter.setDropDownViewResource(R.layout.item_fruit_spinner)
        val customerListener = myItemClickListener()
        customerSp!!.adapter = fruitspinneradapter
        customerSp!!.onItemSelectedListener = customerListener

        et_unit!!.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    val tName = etName?.text
                    val tPrice = etPrice?.text
                    val tUnit = et_unit?.text
                    var price = 0.0
                    var name = ""
                    var unit = ""
                    if (tName.toString() != "") {
                        name = tName.toString()
                    } else {
                        Toast.makeText(scanForActivity(context), "请输入名字", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    if (tPrice.toString() != "") {
                        price = tPrice.toString().toDouble()
                    }

                    if (tUnit.toString() == "") {
                        Toast.makeText(scanForActivity(context), "请输入计量单位", Toast.LENGTH_SHORT).show()
                        return false
                    } else {
                        unit = tUnit.toString()
                    }
                    Log.e("Lovesosoi", name + " " + price + " " + unit)
                    if (mId == 0) {
                        listener?.add(name, price, unit,cate)
                    } else {
                        listener?.upDate(name, price, unit, mId,cate)
                    }
                    return true
                } else {
                    return false
                }
            }

        })

        tvSure?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val tName = etName?.text
                val tPrice = etPrice?.text
                val tUnit = et_unit?.text
                var price = 0.0
                var name = ""
                var unit = ""
                if (tName.toString() != "") {
                    name = tName.toString()
                } else {
                    Toast.makeText(scanForActivity(context), "请输入名字", Toast.LENGTH_SHORT).show()
                    return
                }
                if (tPrice.toString() != "") {
                    price = tPrice.toString().toDouble()
                }

                if (tUnit.toString() == "") {
                    Toast.makeText(scanForActivity(context), "请输入计量单位", Toast.LENGTH_SHORT).show()
                    return
                } else {
                    unit = tUnit.toString()
                }
                Log.e("Lovesosoi", name + " " + price + " " + unit)
                if (mId == 0) {
                    listener?.add(name, price, unit,cate)
                } else {
                    listener?.upDate(name, price, unit, mId,cate)
                }
            }
        })
        tvCancel?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                close()
            }
        })
    }

    fun setOnAddFruitListener(l: OnAddFruit) {
        listener = l
    }

    fun setDate(name: String, price: String, unit: String, id: Int,position:Int) {
        etName?.setText(name)
        etPrice?.setText(price)
        et_unit?.setText(unit)
        customerSp?.setSelection(position)
        this.mId = id
    }

    fun show(id: Int) {
        if (id == 0) {
            mId = 0
        } else {
            mId = id
        }
        show()
    }

    fun close() {
        mId = 0
        etName?.setText("")
        etPrice?.setText("")
        et_unit?.setText("")
        dismiss()
    }

    private fun scanForActivity(cont: Context?): Activity? {
        if (cont == null)
            return null
        else if (cont is Activity)
            return cont
        else if (cont is ContextWrapper)
            return scanForActivity(cont.baseContext)

        return null
    }

    internal inner class myItemClickListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            cate = stringArray!![position]
//            if (position!=0) {
//                Toast.makeText(context, "你的选择是：${stringArray!![position]}", Toast.LENGTH_SHORT).show()
//            }
        }
    }

}