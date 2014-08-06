package it.bitz.cuke2customer.model

class Table {

    List<String> columnHeads
    List<TableRow> rows = []

    void addValues (List<String> pipeSeparatedValues) {
        final List<String[]> values = pipeSeparatedValues*.trim()*.split ('\\|').collect {
            it.findAll {it}.collect { it.trim () }.toList ()
        }
        values.each { row ->
            rows.add (new TableRow (tableCells: row))
        }
    }
}
