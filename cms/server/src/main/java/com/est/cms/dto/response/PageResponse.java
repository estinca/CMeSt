package com.est.cms.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {

    private String id;
    private String name;
    private String title;
    private String path;
    private boolean published;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SiteResponse site;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PageResponse parent;
    private Date createdAt;
    private Date updatedAt;
}
