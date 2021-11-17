package com.leijendary.spring.boot.template.flow;

/**
 * The Flow layer is created to prevent circular dependencies between the service layer
 * This layer is a part of the business layer wherein this orchestrates the service layer.
 *
 * For example, there is an instance that upon creation of a user, it will send a verification
 * logic to the user's email or mobile number, and this can also happen in the registration part.
 *
 * To avoid the problem stated above, the Flow layer will orchestrate the call of creating a user
 * and executing the verification service in a separate method.
 *
 * This layer will also service as the converter of the response from the service layer since
 * there are some cases that the entity model of the service is needed for the next service
 */
public abstract class AppFlow {
}
