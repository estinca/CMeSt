package com.est.repository.dbm.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@Column(name = "path", unique = true, nullable = false, length = 500)
	private String path;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "updated_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt;
	
	@JsonIgnore
	@OneToMany(mappedBy="site", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<PageDB> pages;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = new Date();
		this.updatedAt = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = new Date();
	}
	
}
