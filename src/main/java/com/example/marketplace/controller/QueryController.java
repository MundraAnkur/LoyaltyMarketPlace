package com.example.marketplace.controller;

import com.example.marketplace.util.QueryDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * @author ankurmundra
 * November 09, 2021
 */
@Controller
@RequestMapping("/query")
public class QueryController {
    private final JdbcTemplate jdbcTemplate;

    public QueryController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/ask")
    public String askQuery(@ModelAttribute("queryDto") QueryDTO query, Model model)
    {
        QueryDTO queryDTO = new QueryDTO();
        model.addAttribute("editQuery",query.getQuery());
        model.addAttribute("queryDto", queryDTO);
        return "ask_query";
    }

    @PostMapping("/ask")
    public String queryResult(@ModelAttribute("queryDto") QueryDTO dto,  Model model)
    {
        System.out.println(dto);
        List<Map<String,Object>> mapList = jdbcTemplate.queryForList(dto.getQuery());
        model.addAttribute("query",dto.getQuery());
        model.addAttribute("data",mapList);
        return "query_result";
    }
}
