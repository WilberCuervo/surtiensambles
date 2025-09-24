package com.docelectronico.security.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class RoleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "role_name", unique = true, nullable = false, updatable = false)
	private String roleName;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "role_permision",
			joinColumns = @JoinColumn(name = "role_id"),
			inverseJoinColumns =  @JoinColumn(name = "permission_id"),
			uniqueConstraints = { @UniqueConstraint(columnNames = {"role_id","permission_id"})})
	private Set<PermisionEntity> permisionList = new HashSet<>();
	

}
