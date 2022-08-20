package com.kuymakov.socialmedia.articles.controller;

import com.kuymakov.socialmedia.articles.controller.ArticlesController;
import com.kuymakov.socialmedia.articles.model.Article;
import com.kuymakov.socialmedia.articles.repository.ArticlesRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticlesRepository repo;

    List<Article> articles = new ArrayList<>();

    @Before
    public void fillData() {
        for (int i = 0; i < 10; i++) {
            var article = Article.builder()
                    .title("article " + (i + 1))
                    .content("content " + (i + 1))
                    .theme("theme " + (i + 1))
                    .tags(i % 2 == 0 ? List.of("tag 1", "tag 2") : List.of("tag 3", "tag 4"))
                    .username("user " + (i + 1))
                    .isPrivate(i % 2 != 0)
                    .build();
            articles.add(article);
        }
    }

    @Test
    public void shouldReturnAllInsertedArticles() throws Exception {
        doReturn(articles).when(repo).findAllPublic();
        MvcResult res = mockMvc.perform(get("/articles/get")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(articles.size())))
                .andReturn();
        var objectMapper = new ObjectMapper();
        var actualArticles = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<List<Article>>() {
        });
        assertEquals(articles, actualArticles);
    }

}