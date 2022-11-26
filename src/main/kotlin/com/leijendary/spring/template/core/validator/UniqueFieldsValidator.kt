package com.leijendary.spring.template.core.validator

import com.leijendary.spring.template.core.extension.reflectGet
import com.leijendary.spring.template.core.validator.annotation.UniqueFields
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UniqueFieldsValidator : ConstraintValidator<UniqueFields, List<Any>> {
    private lateinit var uniqueFields: Array<String>
    private lateinit var message: String

    override fun initialize(constraintAnnotation: UniqueFields) {
        super.initialize(constraintAnnotation)

        this.message = constraintAnnotation.message
        this.uniqueFields = constraintAnnotation.uniqueFields
    }

    override fun isValid(list: List<Any>?, context: ConstraintValidatorContext): Boolean {
        val fieldSets = mutableMapOf<String, MutableSet<Any>>()
        var hasDuplicate = false

        context.disableDefaultConstraintViolation()

        list?.forEachIndexed { index, target ->
            uniqueFields.forEach fieldLoop@{ field ->
                val value = target.reflectGet(field) ?: return@fieldLoop
                val set = fieldSets.getOrDefault(field, mutableSetOf())

                if (!set.add(value)) {
                    val existingIndex = set.indexOf(value)

                    addViolation(existingIndex, context, field)
                    addViolation(index, context, field)

                    hasDuplicate = true
                }

                fieldSets[field] = set
            }
        }

        return !hasDuplicate
    }

    private fun addViolation(index: Int, context: ConstraintValidatorContext, field: String) {
        context
            .buildConstraintViolationWithTemplate(message)
            .addPropertyNode(field)
            .inIterable()
            .atIndex(index)
            .addConstraintViolation()
    }
}