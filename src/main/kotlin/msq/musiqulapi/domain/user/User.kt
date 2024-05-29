package msq.musiqulapi.domain.user

import lombok.Value
import java.util.*

@Value(staticConstructor = "of")
data class User(
  val userId: UserId,
  val displayName: DisplayName,
  val mailAddress: MailAddress,
) {
  companion object {
    fun create(displayName: DisplayName): User {
      return User(
        userId = UserId.generate(),
        displayName = displayName,
        mailAddress = MailAddress(""),
      )
    }
  }
}

@JvmInline
value class MailAddress(val value: String) {
  companion object {
    private val MAIL_ADDRESS_MAX_LENGTH = 255
  }
  init {
    require(value.isNotBlank()) { "Mail address is required" }
    require(value.length <= MAIL_ADDRESS_MAX_LENGTH) {
      "Mail address must be ${MAIL_ADDRESS_MAX_LENGTH} characters or less"
    }
  }
}

@JvmInline
@Value(staticConstructor = "of")
value class UserId(val value: String) {
  companion object {
    fun generate(): UserId {
      return UserId(UUID.randomUUID().toString())
    }
  }
}

@JvmInline
value class DisplayName(val value: String) {
  init {
    require(value.length in 1..30) { "User name must be between 1 and 30 characters" }
  }
}