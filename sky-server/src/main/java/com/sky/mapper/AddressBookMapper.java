package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface AddressBookMapper {
    /**
     * 新增地址
     *
     * @param addressBook 新增的地址
     */
    @Insert("insert into address_book" +
            "(user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default)" +
            "values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 查询地址列表
     *
     * @param addressBook 查询条件
     * @return 地址列表
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 设置所有地址为非默认地址
     *
     * @param addressBook 查询条件
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void setNotDefault(AddressBook addressBook);

    /**
     * 设置某个地址为默认地址
     *
     * @param addressBook 默认地址
     */
    @Update("update address_book set is_default = 1 where id = #{id}")
    void updateDefaultById(AddressBook addressBook);
}
