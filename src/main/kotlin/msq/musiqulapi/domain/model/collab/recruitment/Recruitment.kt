package msq.musiqulapi.domain.model.collab.recruitment

import arrow.core.Option
import msq.musiqulapi.domain.DomainEvent
import msq.musiqulapi.domain.DomainEventId
import msq.musiqulapi.lib.IdFactory


data class Dog(
  val dogId: Int,
  val name: String
) {
  companion object {
    fun f(recruitment: Recruitment) {
      recruitment.name
    }
  }

  fun updateName(name: String): Dog {
    return this.copy(name = name)
  }
}

class Recruitment private constructor(
  val occurredEvents: List<DomainEvent>,
  val id: RecruitmentId,
  val name: RecruitmentName,
//  val owner: PlayerId,
  val genre: List<MusicGenre>,
  val songTitle: Option<SongTitle>,
  val ownerInstruments: List<Instrument>,
  val recruitedInstruments: RequiredInstrumentsAndCounts,
  val requiredAgeRange: Option<RequiredAgeRange>,
  val requiredGender: RequiredGender,
  val deadline: DeadLine,
  val memo: Memo,
  val recruitmentStatus: RecruitmentStatus,
  val deleted: Boolean
) {
  companion object {
    fun recruit(
      eventIdFactory: IdFactory<DomainEventId>,
      idFactory: IdFactory<RecruitmentId>,
      command: RecruitCommand
    ): Recruitment {
      val eventId = eventIdFactory.generate()
      val id = idFactory.generate()
      val recruitedEvent = RecruitedEvent(
        eventId,
        id,
        command.name,
        command.genre,
        command.songTitle,
        command.ownerInstruments,
        command.recruitedInstruments,
        command.requiredAgeRange,
        command.requiredGender,
        command.deadline,
        command.memo
      )
      return Recruitment(
        listOf(recruitedEvent),
        id,
        command.name,
        command.genre,
        command.songTitle,
        command.ownerInstruments,
        command.recruitedInstruments,
        command.requiredAgeRange,
        command.requiredGender,
        command.deadline,
        command.memo,
        RecruitmentStatus.OPEN,
        false
      )
    }
  }


  fun close(eventIdFactory: IdFactory<DomainEventId>): Recruitment {
    val eventId = eventIdFactory.generate()
    val event = RecruitmentClosedEvent(eventId)
    return Recruitment(
      this.occurredEvents.plus(event),
      this.id,
      this.name,
      this.genre,
      this.songTitle,
      this.ownerInstruments,
      this.recruitedInstruments,
      this.requiredAgeRange,
      this.requiredGender,
      this.deadline,
      this.memo,
      RecruitmentStatus.CLOSE,
      this.deleted
    )
  }

  fun edit() {}

//  fun reopen() {}

  fun delete() {}
}

data class RecruitCommand(
  val name: RecruitmentName,
  val genre: List<MusicGenre>,
  val songTitle: Option<SongTitle>,
  val ownerInstruments: List<Instrument>,
  val recruitedInstruments: RequiredInstrumentsAndCounts,
  val requiredAgeRange: Option<RequiredAgeRange>,
  val requiredGender: RequiredGender,
  val deadline: DeadLine,
  val memo: Memo
)

data class RecruitedEvent(
  override val eventId: DomainEventId,
  val id: RecruitmentId,
  val name: RecruitmentName,
  val genre: List<MusicGenre>,
  val songTitle: Option<SongTitle>,
  val ownerInstruments: List<Instrument>,
  val recruitedInstruments: RequiredInstrumentsAndCounts,
  val requiredAgeRange: Option<RequiredAgeRange>,
  val requiredGender: RequiredGender,
  val deadline: DeadLine,
  val memo: Memo
) : DomainEvent

data class RecruitmentClosedEvent(
  override val eventId: DomainEventId
) : DomainEvent
