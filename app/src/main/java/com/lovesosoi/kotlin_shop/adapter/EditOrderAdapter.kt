package com.lovesosoi.kotlin_shop.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.lovesosoi.kotlin_shop.R
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.EditOrderBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import com.lovesosoi.kotlin_shop.interfaces.IOnOrderEditListener
import com.lovesosoi.kotlin_shop.interfaces.IOnOrderListClick
import com.lovesosoi.kotlin_shop.interfaces.IOnPostionClick
import com.lovesosoi.kotlin_shop.interfaces.OnItemClick

/**
 * 水果
 * 2019-7-19 Lovesosoi
 */
class EditOrderAdapter(var context: Context, var data: MutableList<EditOrderBean>) :
    RecyclerView.Adapter<EditOrderAdapter.EditOrderHolder>() {
    var listener: IOnOrderEditListener? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EditOrderHolder {
        var inflate = LayoutInflater.from(context).inflate(R.layout.item_edit_order, p0, false)
        return EditOrderHolder(inflate)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(p0: EditOrderHolder, p1: Int) {
        val temp = data.get(p1)
        p0.tv_name.text = temp.fruit_name
        p0.tv_total.text = temp.fruit_total
        if (listener != null) {
            p0.tv_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener?.delete(p1, p0.itemView, data.get(p1))
                }
            })
            p0.tv_save.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener?.save(p1, p0.itemView, data.get(p1))
                }
            })

        }
        if (p0.et_amount.getTag() is TextWatcher) {
            p0.et_amount.removeTextChangedListener(p0.et_amount.getTag() as TextWatcher)
        }
        p0.et_amount.setText(temp.fruit_amount)

        val tw_amount: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var amout = 0.0
                if (s == null || s.length == 0) {
                    amout = 0.0
                } else if (s == ".") {
                    amout = 0.0
                } else {
                    amout = p0.et_amount.text.toString().toDouble()
                }
                data.get(p1).fruit_amount = amout.toString()
                val new_total =
                    String.format("%.2f",amout * data.get(p1).fruit_unit.toDouble())
                data.get(p1).fruit_total = new_total
//                p0.tv_total.text = new_total

            }
        }
        p0.et_amount.addTextChangedListener(tw_amount)
        p0.et_amount.setTag(tw_amount)


        if (p0.et_unit.getTag() is TextWatcher) {
            p0.et_unit.removeTextChangedListener(p0.et_unit.getTag() as TextWatcher)
        }
        p0.et_unit.setText(temp.fruit_unit)

        val tw_unit: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var unit = 0.0
                if (s == null || s.length == 0) {
                    unit = 0.0
                } else if (s == ".") {
                    unit = 0.0
                } else {
                    unit = p0.et_unit.text.toString().toDouble()
                }
                data.get(p1).fruit_unit = unit.toString()
                val amount=data.get(p1).fruit_amount
                val new_total = String.format("%.2f",amount.toDouble() * unit)
                data.get(p1).fruit_total = new_total
//                p0.tv_total.text = new_total

            }
        }

        p0.et_unit.addTextChangedListener(tw_unit)
        p0.et_unit.setTag(tw_unit)


        if (p0.et_danwei.getTag() is TextWatcher) {
            p0.et_danwei.removeTextChangedListener(p0.et_danwei.getTag() as TextWatcher)
        }
        p0.et_danwei.setText(temp.fruit_danwei)

        val tw_danwei: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.length > 0) {
                    data.get(p1).fruit_danwei = p0.et_danwei.text.toString()
                }

            }
        }

        p0.et_danwei.addTextChangedListener(tw_danwei)
        p0.et_danwei.setTag(tw_danwei)

//        p0.et_danwei.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s!!.length > 0) {
//                    data.get(p1).fruit_danwei = p0.et_danwei.text.toString()
//                }
//
//            }
//        })


//        p0.et_amount.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                var amout=0.0
//                if (s==null||s.length==0){
//                    amout=0.0
//                }else if(s=="."){
//                    amout=0.0
//                }
//                else{
//                    amout=p0.et_amount.text.toString().toDouble()
//                }
//                data.get(p1).fruit_amount = amout.toString()
////                val new_total =
////                    (amout * data.get(p1).fruit_unit.toDouble()).toString()
////                data.get(p1).fruit_total = new_total
////                p0.tv_total.text = new_total
//
//            }
//        })
//        p0.et_unit.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                var unit = 0.0
//                if (s == null || s.length == 0) {
//                    unit = 0.0
//                } else if (s == ".") {
//                    unit = 0.0
//                } else {
//                    unit = p0.et_unit.text.toString().toDouble()
//                }
//                data.get(p1).fruit_unit = unit.toString()
////                val new_total =
////                    (p0.et_amount.text.toString().toDouble() * unit).toString()
////                data.get(p1).fruit_total = new_total
////                p0.tv_total.text = new_total
//
//            }
//        })
    }

    fun setOnItemClickListener(click: IOnOrderEditListener) {
        listener = click
    }

    inner class EditOrderHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView = view.findViewById(R.id.tv_name)
        var tv_total: TextView = view.findViewById(R.id.tv_total)
        var et_unit: EditText = view.findViewById(R.id.et_unit)
        var et_amount: EditText = view.findViewById(R.id.et_amount)
        var et_danwei: EditText = view.findViewById(R.id.et_danwei)
        var tv_delete: TextView = view.findViewById(R.id.tv_delete)
        var tv_save: TextView = view.findViewById(R.id.tv_save)
    }
}