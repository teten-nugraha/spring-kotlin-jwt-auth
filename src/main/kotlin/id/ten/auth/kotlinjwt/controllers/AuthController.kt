package id.ten.auth.kotlinjwt.controllers

import id.ten.auth.kotlinjwt.dtos.LoginDto
import id.ten.auth.kotlinjwt.dtos.Message
import id.ten.auth.kotlinjwt.dtos.RegisterDto
import id.ten.auth.kotlinjwt.models.User
import id.ten.auth.kotlinjwt.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/")
class AuthController(private val userService: UserService) {

    @PostMapping("register")
    fun register(@RequestBody payload: RegisterDto): ResponseEntity<User> {

        val user = User()
        user.name = payload.name
        user.email = payload.email
        user.password = payload.password

        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody payload: LoginDto): ResponseEntity<Any> {

        val user = this.userService.findByEmail(payload.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if(!user.comparePassword(payload.password)){
            return ResponseEntity.badRequest().body(Message("invalid password"))
        }

        return ResponseEntity.ok(user)
    }
}