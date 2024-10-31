package com.leijendary.domain.image

import org.springframework.data.repository.ListCrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface ImageMetadataRepository : ListCrudRepository<ImageMetadata, String>
