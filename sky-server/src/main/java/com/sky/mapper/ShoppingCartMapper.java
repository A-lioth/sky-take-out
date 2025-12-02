package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 查询购物车数据
     *
     * @param shoppingCart 购物车数据
     * @return 购物车数据列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 修改购物车数据
     *
     * @param cart 购物车数据
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void update(ShoppingCart cart);

    /**
     * 添加购物车数据
     *
     * @param shoppingCart 购物车数据
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "values(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     *
     * @param id 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{id}")
    void deleteByUserId(Long id);
}
