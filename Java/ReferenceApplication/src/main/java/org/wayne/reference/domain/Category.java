package org.wayne.reference.domain;

import java.util.Date;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
public class Category {
	
	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private String title;
	
	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
    private Date date;

	
    public Category() {}
	
	public Category(String _title) {
		this.title = _title;
	}

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}