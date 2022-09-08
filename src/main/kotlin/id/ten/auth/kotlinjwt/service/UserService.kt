package id.ten.auth.kotlinjwt.service

import id.ten.auth.kotlinjwt.models.User
import id.ten.auth.kotlinjwt.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun save(user: User): User {
        return this.userRepository.save(user);
    }

    fun findByEmail(email: String): User? {
        return this.userRepository.findByEmail(email)
    }

    fun getUserById(id: Int): User {
        return this.userRepository.getById(id)
    }
}