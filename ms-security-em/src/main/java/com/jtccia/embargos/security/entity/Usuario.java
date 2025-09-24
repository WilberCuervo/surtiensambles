package com.jtccia.embargos.security.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column (name= "id", unique = true, updatable = false)
	private Long id;
	
	@Column (name= "user_name", nullable = false, unique = true, updatable = false)
	private String userName;

	@Column (name= "password", nullable = false)
	private String password;
	
	@Column(name ="name")
	private String name;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@Column(name = "account_no_expired")
	private boolean accountNoExpired;
	
	@Column(name = "account_no_loked")
	private boolean accounNoLoked;
	
	@Column(name = "credential_no_expired")
	private boolean credentialNoExpired;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name= "role_id"),
			uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}
	)
	@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})		
    private Set<RoleEntity> roles = new HashSet<>();

}
