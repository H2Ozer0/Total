package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class test_controller {
    @RequestMapping("/test1")
    public String test(Model model, HttpServletRequest request) {

        model.addAttribute("test_string","这是一个测试的字符串");
        return "test";
    }
}


