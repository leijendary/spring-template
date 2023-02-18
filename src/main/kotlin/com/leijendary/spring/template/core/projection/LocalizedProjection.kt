package com.leijendary.spring.template.core.projection

interface LocalizedProjection<T : LocaleProjection> : UUIDProjection {
    val translations: MutableSet<T>
}
