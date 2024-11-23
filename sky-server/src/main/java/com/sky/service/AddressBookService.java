package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

/**
 * @Author: 程浩然
 * @Create: 2024/11/23 - 13:35
 * @Description: 地址簿Service层接口
 */
public interface AddressBookService {

    List<AddressBook> list(AddressBook addressBook);

    void save(AddressBook addressBook);

    AddressBook getById(Long id);

    void update(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    void deleteById(Long id);

}