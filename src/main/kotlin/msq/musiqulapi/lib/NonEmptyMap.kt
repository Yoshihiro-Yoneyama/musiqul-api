package msq.musiqulapi.lib

data class NonEmptyMap<K, V>(private val map: Map<K, V>) {
  init {
    if (map.isEmpty()) throw IllegalArgumentException("map is empty")
  }
}