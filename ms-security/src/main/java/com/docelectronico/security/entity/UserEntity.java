package com.docelectronico.security.entity;



import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.surtiensambes.commos.usuario.model.Usuario;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class UserEntity extends Usuario {	
		
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {return super.getId();}
	
	@Column(name = "user_name", unique = true, nullable = false)
	public String getUserName() {return super.getUserName();}
	
	@Column(name = "password")
	public String getPassword() {return super.getPassword();}
	
	@Column(name = "name")
	public String getName() {return super.getName();}
	
	@Column(name = "is_enable")
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
	public Set<RoleEntity> getRoles(){
		return roles;
	}
	
	
	@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})		
    private Set<RoleEntity> roles = new HashSet<>();
	
	
	
	
	
	

}
