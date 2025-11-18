package com.ide.web.domain.service

import com.ide.web.domain.entity.UserDetailsEntity
import com.ide.web.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository) : UserDetailsService {

    /**
     * Email로 찾기
     * @param username 유저 이메일
     */
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User $username not found")

        return UserDetailsEntity(user)
    }
}