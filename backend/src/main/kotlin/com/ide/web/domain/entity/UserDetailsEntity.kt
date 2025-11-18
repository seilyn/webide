package com.ide.web.domain.entity


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class UserDetailsEntity(
    private val user: UserEntity
) : UserDetails {

    fun getUserId(): UUID? = user.userId

    fun getUserPublicId(): String = user.userPublicId

    fun getEmail(): String = user.email

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role}"))
    }

    override fun getPassword(): String = user.passwordHash

    override fun getUsername(): String = user.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean {
        return user.lockedUntil?.let {
            it.isBefore(java.time.LocalDateTime.now())
        } ?: true
    }

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = user.isActive && !user.isDeleted
}