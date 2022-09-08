package id.ten.auth.kotlinjwt.controllers

import id.ten.auth.kotlinjwt.dtos.LoginDto
import id.ten.auth.kotlinjwt.dtos.Message
import id.ten.auth.kotlinjwt.dtos.RegisterDto
import id.ten.auth.kotlinjwt.models.User
import id.ten.auth.kotlinjwt.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.coyote.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Date
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

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
    fun login(@RequestBody payload: LoginDto, response: HttpServletResponse): ResponseEntity<Any> {

        val user = this.userService.findByEmail(payload.email)
            ?: return ResponseEntity.badRequest().body(Message("user not found"))

        if(!user.comparePassword(payload.password)){
            return ResponseEntity.badRequest().body(Message("invalid password"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000)) // 1 day
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("Login Sucess"))
    }

    @GetMapping("profile")
    fun profile(@CookieValue("jwt") jwt: String): ResponseEntity<Any> {
        try {
            if(jwt == null) {
                return ResponseEntity.status(401).body(Message("unauthenticated"))
            }

            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body

            return ResponseEntity.ok(this.userService.getUserById(body.issuer.toInt()))
        }catch (e: Exception) {
            return ResponseEntity.status(401).body(Message("unauthenticated"))
        }
    }
}