package id.ten.auth.kotlinjwt.repositories

import id.ten.auth.kotlinjwt.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Int> {
}