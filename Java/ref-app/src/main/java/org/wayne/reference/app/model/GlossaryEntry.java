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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.BatchSize;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;

@Entity
@Table(name = "GLOSSARY_ENTRY")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@BatchSize(size = 20)
@Indexed
public class GlossaryEntry implements Serializable {
	int id;
	String word;
	String def;
	private Glossary glossary;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	@DocumentId
	public int getGlossaryEntryId() {
		return id;
	}

	public void setGlossaryEntryId(int id) {
		this.id = id;
	}

	@Column(name = "WORD", nullable = false, unique = true, length = 50)
	@Field(index = Index.TOKENIZED)
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	@Column(name = "DEFINITION", nullable = false, unique = true, length = 50)
	@Field(index = Index.TOKENIZED)
	public String getDefinition() {
		return def;
	}

	public void setDefinition(String def) {
		this.def = def;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="GLOSSARY_ID", nullable=false)
    public Glossary getGlossary() { return glossary; }
	
	public void setGlossary(Glossary glossary) {
		this.glossary = glossary;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof GlossaryEntry)) {
			return false;
		}

		GlossaryEntry otherCategory = (GlossaryEntry) other;
		return (getGlossaryEntryId() == otherCategory.getGlossaryEntryId());
	}

	@Override
	public int hashCode() {
		return 37 * getGlossaryEntryId() + 97;
	}
}
