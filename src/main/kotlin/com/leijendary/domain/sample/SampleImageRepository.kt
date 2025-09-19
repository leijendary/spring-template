package com.leijendary.domain.sample

import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface SampleImageRepository : CrudRepository<SampleImage, String>
