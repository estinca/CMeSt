package com.est.repository.dbm.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDB {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "path", length = 1500, nullable = false)
    private String path;

    @Column(name = "published", nullable = false)
    private boolean published;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_id", nullable = true)
    private PageDB parent;

    @OneToMany(mappedBy = "parent")
    private List<PageDB> children;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "site_id", nullable = false)
    private SiteDB site;

//    @OneToOne(cascade = CascadeType.MERGE, optional = false)
//    @JoinColumn(name = "root_node", nullable = false)
//    private NodeDB rootNode;

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
