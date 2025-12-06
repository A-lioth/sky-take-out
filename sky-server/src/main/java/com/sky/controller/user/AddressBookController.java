package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询地址列表
     *
     * @return 地址列表
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = AddressBook.builder().userId(BaseContext.getCurrentId()).build();
        return Result.success(addressBookService.list(addressBook));
    }

    /**
     * 新增地址
     *
     * @param addressBook 新增的地址信息
     * @return 新增成功的结果
     */
    @PostMapping
    public Result<String> insert(@RequestBody AddressBook addressBook) {
        addressBookService.insert(addressBook);
        return Result.success();
    }

    /**
     * 设置默认地址
     *
     * @param addressBook 设置默认的参数
     * @return 设置成功结果
     */
    @PutMapping("/default")
    public Result<String> setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     *
     * @return 默认地址
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = AddressBook.builder().userId(BaseContext.getCurrentId()).isDefault(1).build();
        List<AddressBook> list = addressBookService.list(addressBook);
        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }
        return Result.error("没有查询到默认地址");
    }

    /**
     * 删除地址
     *
     * @param id 要删除的id
     * @return 删除成功结果
     */
    @DeleteMapping
    public Result<String> delete(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改地址
     *
     * @param addressBook 修改的参数
     * @return 修改成功结果
     */
    @PutMapping()
    public Result<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     *
     * @param id 要查询的id
     * @return 地址
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
}
