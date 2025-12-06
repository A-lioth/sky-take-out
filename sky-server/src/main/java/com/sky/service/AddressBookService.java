package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    /**
     * 新增地址
     *
     * @param addressBook 新增的地址
     */
    void insert(AddressBook addressBook);

    /**
     * 查询地址列表
     *
     * @param addressBook 查询条件
     * @return 地址列表
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 设置默认地址
     *
     * @param addressBook 默认地址
     */
    void setDefault(AddressBook addressBook);

    /**
     * 删除地址
     *
     * @param id 要删除的地址id
     */
    void deleteById(Long id);

    /**
     * 修改地址
     *
     * @param addressBook 修改的参数
     */
    void updateById(AddressBook addressBook);

    /**
     * 根据id查询地址
     *
     * @param id 要查询的id
     * @return 地址
     */
    AddressBook getById(Long id);
}
