package com.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/employee")
public class EmployeeController {


    @RequestMapping(value = {"/addblog"}, method = RequestMethod.GET)
    public String addblog() {
        return "employee/addblog.html";
    }

    @RequestMapping(value = {"/addimportproduct"}, method = RequestMethod.GET)
    public String addimportproduct() {
        return "employee/addimportproduct.html";
    }

    @RequestMapping(value = {"/blog"}, method = RequestMethod.GET)
    public String blog() {
        return "employee/blog.html";
    }

    @RequestMapping(value = {"/importproduct"}, method = RequestMethod.GET)
    public String importproduct() {
        return "employee/importproduct.html";
    }

    @RequestMapping(value = {"/invoice"}, method = RequestMethod.GET)
    public String invoice() {
        return "employee/invoice.html";
    }

    @RequestMapping(value = {"/product"}, method = RequestMethod.GET)
    public String product() {
        return "employee/product.html";
    }

    @RequestMapping(value = {"/nhacungcap"}, method = RequestMethod.GET)
    public String nhacungcap() {
        return "employee/nhacungcap.html";
    }

    @RequestMapping(value = {"/taikhoan"}, method = RequestMethod.GET)
    public String taikhoan() {
        return "employee/taikhoan.html";
    }

}
