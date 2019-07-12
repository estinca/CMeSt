package com.est.repository.dbm.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name="sites")
@Data
public class SiteDB {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private String id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "base_path", unique = true, nullable = false, length = 500)
	private String basePath;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "update_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
}
