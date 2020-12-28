package com.farmdiagnostician.web;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.farmdiagnostician.web");

        noClasses()
            .that()
            .resideInAnyPackage("com.farmdiagnostician.web.service..")
            .or()
            .resideInAnyPackage("com.farmdiagnostician.web.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.farmdiagnostician.web.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
