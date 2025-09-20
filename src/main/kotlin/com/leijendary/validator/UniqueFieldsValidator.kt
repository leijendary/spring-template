package com.leijendary.validator

import com.leijendary.extension.reflectGet
import com.leijendary.validator.annotation.UniqueFields
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.aot.hint.annotation.RegisterReflection

@RegisterReflection
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
        var isUnique = true

        context.disableDefaultConstraintViolation()

        list.forEachIndexed { index, target ->
            for (field in fields) {
                val fieldValue = target.reflectGet(field) ?: continue
                val fieldSet = fieldSets.getOrElse(field) { mutableSetOf() }
                val isAdded = fieldSet.add(fieldValue)

                if (!isAdded) {
                    val existingIndex = fieldSet.indexOf(fieldValue)

                    addViolation(existingIndex, context, field)
                    addViolation(index, context, field)

                    isUnique = false
                }

                fieldSets[field] = fieldSet
            }
        }

        return isUnique
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
