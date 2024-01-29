package com.leijendary.validator

import com.leijendary.extension.reflectGet
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class UniqueFieldsValidator : ConstraintValidator<UniqueFields, List<Any>> {
    private lateinit var fields: Array<String>
    private lateinit var message: String

    override fun initialize(constraintAnnotation: UniqueFields) {
        super.initialize(constraintAnnotation)

        this.fields = constraintAnnotation.fields
        this.message = constraintAnnotation.message
    }

    override fun isValid(list: List<Any>?, context: ConstraintValidatorContext): Boolean {
        if (list === null || list.isEmpty()) {
            return true
        }

        val fieldSets = mutableMapOf<String, MutableSet<Any>>()
        var hasDuplicate = false

        context.disableDefaultConstraintViolation()

        list.forEachIndexed { index, target ->
            for (field in fields) {
                val value = target.reflectGet(field) ?: continue
                val set = fieldSets.getOrDefault(field, mutableSetOf())
                val added = set.add(value)

                if (!added) {
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
