package com.leijendary.spring.boot.template.core.projection

interface LocalizedProjection<T : LocaleProjection> : UUIDProjection {
    val translations: HashSet<T>
}