/**
 * This package contains domain-specific functionalities, grouped together based on the domain.
 *
 * e.g: The user domain is grouped in to the "user" package, the role domain is grouped in to the "role" package,
 * and so on...
 *
 * This way, it's easier to find things where they are, and packaging is also easier.
 *
 * To call another domain's service, the domain can just inject another domain service as a dependency
 * and use it. Just be careful in your design; make sure that there are no circular dependencies.
 */
package com.leijendary.domain
