package com.example.myfoodapp.database

import androidx.room.*
import com.example.myfoodapp.model.Addon
import com.example.myfoodapp.model.Cart
import com.example.myfoodapp.model.Size
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface CartDao {
    @Query("select * from Cart where uid=:uid")
    fun getAllCart(uid:String):Flowable<List<Cart>>

    @Query("select count(*) from cart where uid=:uid")
    fun countItemInCart(uid:String):Single<Int>

    @Query("select sum((foodPrice*foodQuantity) + (foodExtraPrice*foodQuantity)) from cart where uid=:uid")
    fun sumItemCart(uid:String):Single<Double>

    @Query("select * from Cart where foodId=:foodId and uid=:uid")
    fun getItemInCart(foodId:String,uid:String):Single<Cart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceCartItems(vararg cart: Cart):Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCartItem(cart: Cart):Single<Int>

    @Delete
    fun deleteCartItem(cart: Cart):Single<Int>

    @Query("delete from cart where uid=:uid")
    fun clearDbCartForUserId(uid: String):Single<Int>

    @Query("select * from cart where foodId=:foodId and uid=:uid and foodSize=:foodSize and foodAddon=:foodAddon")
    fun getItemWithAllOption(foodId: String , uid: String , foodSize: String , foodAddon: String):Single<Cart>


}