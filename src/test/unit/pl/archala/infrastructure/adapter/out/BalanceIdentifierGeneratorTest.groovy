package pl.archala.infrastructure.adapter.out

import pl.archala.infrastructure.adapter.in.generate.BalanceIdentifierGenerator
import spock.lang.Specification

import java.security.SecureRandom
import java.util.regex.Pattern

class BalanceIdentifierGeneratorTest extends Specification {

    SecureRandom secureRandom = new SecureRandom()

    def 'Should generate correct random balance id'() {
        given:
        def balanceIdPattern = Pattern.compile("^[0-9]{20}\$")

        def generator = new BalanceIdentifierGenerator(secureRandom)

        when:
        def generatedId = generator.generate()

        then:
        assert balanceIdPattern.matcher(generatedId.id()).matches()
    }

}
