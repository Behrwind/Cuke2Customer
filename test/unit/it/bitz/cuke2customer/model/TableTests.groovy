package it.bitz.cuke2customer.model

import grails.test.mixin.TestFor
import static org.junit.Assert.*
import org.junit.Test

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
class TableTests {

    @Test
    void addValues () {
        Table table = addValuesAndAssertRowCount(['| value1.1 | value1.2 | value1.3 |', '| value2.1 | value2.2 | value2.3 |'])
        assertEquals (['value1.1', 'value1.2', 'value1.3'], table.rows[0].tableCells)
        assertEquals (['value2.1', 'value2.2', 'value2.3'], table.rows[1].tableCells)
    }

    @Test
    void addsEmptyCellsForEmtpyValues () {
        Table table = addValuesAndAssertRowCount([' | value1 |     |value2| |'])
        assertEquals (['value1', '', 'value2', ''], table.rows[0].tableCells)
    }

    private Table addValuesAndAssertRowCount (List<String> valueLines) {
        Table table = new Table ()
        table.addValues (valueLines)
        assertEquals (valueLines.size(), table.rows.size ())
        return table
    }
}
