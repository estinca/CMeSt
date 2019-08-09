package com.est.repository.api.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private String id;
    private String name;
    private String title;
    private String path;
    private boolean published = false;
    private Page parent;
    private List<Page> children;
    private Site site;
    //    private Node rootNode;
    private Date createdAt;
    private Date updatedAt;
}