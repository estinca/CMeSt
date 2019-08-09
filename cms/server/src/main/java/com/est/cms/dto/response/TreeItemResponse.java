package com.est.cms.dto.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TreeItemResponse {

    private String id;
    private String name;
    private List<TreeItemResponse> children = new ArrayList<>();

    public TreeItemResponse(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void addChildItem(TreeItemResponse child) {
        this.children.add(child);
    }
}
