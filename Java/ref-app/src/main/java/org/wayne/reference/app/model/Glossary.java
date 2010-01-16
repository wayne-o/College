/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wayne.reference.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
// §@Cache(usage = CacheConcurrencyStrategy.NONE)
@Table(name = "GLOSSARY")
@BatchSize(size = 20)
@Indexed
public class Glossary implements Serializable {
	long glossaryId;
	
	String title;
	String description;
	String imageURL;

	@OneToMany
	@JoinColumn(name="GLOSSARY_ID")
	public Set<GlossaryEntry> entries;

	@Id
	@GeneratedValue
	@Column(name = "GLOSSARY_ID")
	@DocumentId
	public long getGlossaryId() {
		return glossaryId;
	}

	public void setGlossaryId(long id) {
		this.glossaryId = id;
	}

	@Column(name = "TITLE", nullable = false, length = 100)
	@Field(index = Index.TOKENIZED, store = Store.YES)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	@Field(index = Index.TOKENIZED)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGlossaryEntries(Set<GlossaryEntry> categories) {
		this.entries = categories;
	}

	@Column(name = "IMAGE_URL", length = 256)
	public String getImageURL() {
		return imageURL;
	}
	
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

}
