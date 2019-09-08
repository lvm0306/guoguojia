package com.colin.doumovie.api

import com.lovesosoi.kotlin_shop.bean.BaseStatus
import com.lovesosoi.kotlin_shop.bean.CCustomer
import com.lovesosoi.kotlin_shop.bean.CFruitBean
import com.lovesosoi.kotlin_shop.bean.OrderList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * rxjava+ retrofit
 * 2019-7-19 Lovesosoi
 */
interface ApiServers {

//    /**
//     * 获取正在上映的数据
//     */
    @GET(BaseURL.FRUIT_LIST)
    fun getFruitList(): Observable<CFruitBean>
    @GET(BaseURL.FRUIT_ADD)
    fun addFruit(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
    @GET(BaseURL.FRUIT_DELETE)
    fun deleteFruit(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
    @GET(BaseURL.CUSTOMER_LIST)
    fun getCustomerList(): Observable<CCustomer>
    @GET(BaseURL.CUSTOMER_ADD)
    fun addCustomer(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
    @GET(BaseURL.CUSTOMER_DELETE)
    fun deleteCustomer(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
    @GET(BaseURL.ORDER_LIST)
    fun getOrderHistory(@QueryMap map:HashMap<String, String>): Observable<OrderList>
    @GET(BaseURL.ORDER_ADD)
    fun addOrder(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
    @GET(BaseURL.ORDER_DELETE)
    fun deleteOrder(@QueryMap map:HashMap<String, String>): Observable<BaseStatus>
//
//    @GET("v2/movie/subject/{id}")
//    fun getMovieDetailById(@Path("id") id: String,
//                           @QueryMap par: HashMap<String, String>): Observable<MovieDetailResult>
//
//    @GET("v2/movie/subject/{id}/comments")
//    fun getMovieDetailCommentById(@Path("id") id: String,
//                                  @QueryMap par: HashMap<String, String>): Observable<CommentBean>
}