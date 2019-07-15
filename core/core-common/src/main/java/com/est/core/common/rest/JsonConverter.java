package com.est.core.common.rest;

import org.springframework.data.domain.Page;

public interface JsonConverter<Entity, Request, Response> {
	Entity fromJson(Request request);
	Response toJson(Entity entity);
	Page<Response> toJson(Page<Entity> entities);
}
