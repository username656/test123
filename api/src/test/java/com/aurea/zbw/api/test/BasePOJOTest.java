package com.aurea.zbw.api.test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsExceptStaticFinalRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

/**
 * This base test validates getters, setters and other rules over POJOs.
 */
@Slf4j
public abstract class BasePOJOTest {

    private final Set<Class<?>> redefinedSuperclasses;
    private final Map<Class<?>, Set<Class<?>>> redefinedSubclasses;
    private final Collection<Class<?>> classes;

    public BasePOJOTest(Class ... classes) {
      this(Collections.emptySet(), Collections.emptyMap(), classes);
    }

    /**
     * @param redefinedSuperclasses Are the redefined super classes to adjust the validation
     *  @see http://jqno.nl/equalsverifier/errormessages/symmetry-does-not-equal-superclass-instance
     * @param redefinedSubclasses Are the redefined sub classes to adjust the validation
     *  @see http://jqno.nl/equalsverifier/errormessages/subclass-equals-is-not-final
     * @param classes Are the classes to validatePOJOStructure
     */
    public BasePOJOTest(Set<Class<?>> redefinedSuperclasses,
        Map<Class<?>, Set<Class<?>>> redefinedSubclasses, Class ... classes) {
        this.redefinedSuperclasses = redefinedSuperclasses;
        this.redefinedSubclasses = redefinedSubclasses;
        this.classes = Arrays.asList(classes);
    }

    /**
     * This test validates getters, setters and other rules over POJOs through the OpenPOJO library.
     *
     * @see https://github.com/oshoukry/openpojo/
     * Refer to com.openpojo.validation.rule.impl.* for more validation rules.
     * Refer to com.openpojo.validation.test.impl.* for more testers.
     */
    @Test
    public void validatePOJOStructure() {
        Validator validator = ValidatorBuilder.create()
            .with(new GetterMustExistRule())
            .with(new SetterMustExistRule())
            .with(new NoPublicFieldsExceptStaticFinalRule())
            .with(new NoFieldShadowingRule())
            .with(new SetterTester())
            .with(new GetterTester())
            .build();

        for (final Class clazz : classes) {
            log.info("Validating getters/setters methods on {}", clazz);
            validator.validate(PojoClassFactory.getPojoClass(clazz));
        }
    }

    /**
     * @throws InstantiationException If some class can not be instantiated to test the toString
     * @throws IllegalAccessException If some class can not be instantiated to test the toString
     */
    @Test
    public void validateToString() throws InstantiationException, IllegalAccessException {
        for (final Class clazz : classes) {
            Object instance = clazz.newInstance();

            assertThat(instance.toString(), notNullValue());
            assertThat(instance.toString(), not(isEmptyString()));
        }
    }

    /**
     * This test validates equals and hashCode over POJOs through the EqualsVerifier library.
     *
     * @see https://github.com/jqno/equalsverifier
     * @throws InstantiationException If some class can not be instantiated to test the hashCode
     * @throws IllegalAccessException If some class can not be instantiated to test the hashCode
     */
    @Test
    public void validateEqualsAndHashCode() throws InstantiationException, IllegalAccessException {
        for (final Class clazz : classes) {
            EqualsVerifier verifier = EqualsVerifier.forClass(clazz)
                // We will not force the fields to be final
                // see http://jqno.nl/equalsverifier/errormessages/mutability-equals-depends-on-mutable-field/
                .suppress(Warning.NONFINAL_FIELDS);
            if (redefinedSuperclasses.contains(clazz)) {
                verifier = verifier.withRedefinedSuperclass();
            }
            for (Class redefinedSubclass : redefinedSubclasses.getOrDefault(clazz, Collections.emptySet())) {
                verifier = verifier.withRedefinedSubclass(redefinedSubclass);
            }

            verifier.verify();

            validateNullFieldsHashCode(clazz);
            validateNullFieldsEquals(clazz);
        }
    }

    /**
     * This test validates hashCode over an object without values on properties (ie all null).
     *
     * @param <T> The class that is being tested
     * @param clazz The class object of the class that is being tested
     * @throws InstantiationException If some class can not be instantiated to test the hashCode
     * @throws IllegalAccessException If some class can not be instantiated to test the hashCode
     */
    protected <T> void validateNullFieldsHashCode(Class<T> clazz)
        throws InstantiationException, IllegalAccessException {
        T instance = clazz.newInstance();

        assertThat(instance.hashCode(), notNullValue());
    }

    /**
     * This test validates equals over 2 objects without values on properties (ie all null).
     *
     * @param <T> The class that is being tested
     * @param clazz The class object of the class that is being tested
     * @throws InstantiationException If the class can not be instantiated to test the equals
     * @throws IllegalAccessException If the class can not be instantiated to test the quals
     */
    protected <T> void validateNullFieldsEquals(Class<T> clazz)
        throws InstantiationException, IllegalAccessException {
        T instance1 = clazz.newInstance();
        T instance2 = clazz.newInstance();

        assertThat(instance1.equals(instance2), is(true));
    }

}
