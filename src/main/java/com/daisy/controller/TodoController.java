package com.daisy.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 * @author wangyunpeng
 */
@RestController
@RequestMapping(value = "/todo")
@PreAuthorize("hasRole('USER')")
public class TodoController {
}
