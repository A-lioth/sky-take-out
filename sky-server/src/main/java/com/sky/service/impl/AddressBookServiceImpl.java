package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     *
     * @param addressBook 新增的地址
     */
    public void insert(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询地址列表
     *
     * @param addressBook 查询条件
     * @return 地址列表
     */
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 默认地址
     */
    @Transactional
    public void setDefault(AddressBook addressBook) {
        // * 设置该用户的所有地址非默认
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.setNotDefault(addressBook);
        // * 设置新的默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.updateDefaultById(addressBook);
    }

    /**
     * 删除地址
     *
     * @param id 要删除的地址id
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * 修改地址
     *
     * @param addressBook 修改的参数
     */
    public void updateById(AddressBook addressBook) {
        addressBookMapper.updateById(addressBook);
    }

    /**
     * 根据id查询地址
     *
     * @param id 要查询的id
     * @return 地址
     */
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }
}
