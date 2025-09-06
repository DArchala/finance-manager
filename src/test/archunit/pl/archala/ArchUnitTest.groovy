package pl.archala

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.ArchRule
import spock.lang.Shared
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

class ArchUnitTest extends Specification {

    @Shared
    JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("pl.archala")

    def "Domain powinien być niezależny od frameworków i wyższych warstw"() {
        given:
        ArchRule domainOnlyDependsOnAllowed = classes()
                .that().resideInAnyPackage("pl.archala.domain..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "pl.archala.domain..",
                        "pl.archala.shared..",
                        "java..",
                        "jakarta..",
                        "lombok.."
                )

        expect:
        domainOnlyDependsOnAllowed.check(classes)
    }

    def "Application powinien zależeć tylko od domain i shared (bez infrastructure)"() {
        given:
        ArchRule appOnlyDependsOnDomainAndShared = classes()
                .that().resideInAnyPackage("pl.archala.application..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        "pl.archala.application..",
                        "pl.archala.domain..",
                        "pl.archala.shared..",
                        "java..",
                        "jakarta..",
                        "lombok..",
                        "org.slf4j.."
                )

        expect:
        appOnlyDependsOnDomainAndShared.check(classes)
    }

    def "Domain i Application nie powinny zależeć od Infrastructure (Infrastructure jako zewnętrzna warstwa)"() {
        given:
        ArchRule noDepsOnInfrastructure = noClasses()
                .that().resideInAnyPackage(
                "pl.archala.domain..",
                "pl.archala.application.."
        )
                .should().dependOnClassesThat()
                .resideInAnyPackage("pl.archala.infrastructure..")

        expect:
        noDepsOnInfrastructure.check(classes)
    }

    def "Kontrolery powinny znajdować się w adapterach wejściowych w Infrastructure"() {
        given:
        ArchRule controllersLocation = classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAnyPackage(
                "pl.archala.infrastructure..in..",
                "pl.archala.infrastructure.adapter.in..",
        )

        expect:
        controllersLocation.check(classes)
    }

    def "Repozytoria powinny znajdować się w adapterach wyjściowych w Infrastructure"() {
        given:
        ArchRule repositoriesLocation = classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAnyPackage(
                "pl.archala.infrastructure..out..",
                "pl.archala.infrastructure.adapter.out..",
        )

        expect:
        repositoriesLocation.check(classes)
    }
}