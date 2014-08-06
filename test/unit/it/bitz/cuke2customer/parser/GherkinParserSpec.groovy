package it.bitz.cuke2customer.parser

import it.bitz.cuke2customer.model.Feature
import it.bitz.cuke2customer.model.Scenario
import it.bitz.cuke2customer.model.Table
import spock.lang.Specification

class GherkinParserSpec extends Specification {

    GherkinParser parser = new GherkinParser(true)

    void "parses a feature's title"() {
        given: "a feature with the title 'test feature'"
        String gherkin = 'Feature: test feature'

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the parsed feature's title should be 'test feature'"
        feature.title == 'test feature'
    }

    void "parses a feature's description"() {
        given: "a feature with a description "
        String description = """
            This is the feature's description.
            It spreads across two lines, which should not be a problem.
        """
        String gherkin = "Feature: test feature $description"

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the parsed feature should have the description"
        feature.description == "This is the feature's description.\nIt spreads across two lines, which should not be a problem."
    }

    void "parses a feature's tags"() {
        given: 'a feature with tags'
        String gherkin = """
            @tag1 @tag2
            @tag3
            Feature: test feature
        """

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the parsed feature should have the tags"
        feature.tags.size() == 3
        feature.tags.tag.containsAll(['tag1', 'tag2', 'tag3'])
    }

    void "parses a feature's background"() {
        given: 'a feature with a background'
        String gherkin = """
            Feature: test feature

            Background:
            Given a feature's background
            """

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the parsed feature should have the background"
        feature.background != null
    }

    void "parses a feature's scenarios"() {
        given: 'a feature with scenarios'
        String gherkin = """
              Feature: test feature

              Scenario: first scenario
                When a step
                Then a Step

              Scenario: second scenario
                When a step
                Then a Step

              Scenario: third scenario
                When a step
                Then a Step
              """
        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the parsed feauture should have the scenarios"
        feature.scenarios.title == ['first scenario', 'second scenario', 'third scenario']
    }

    void "parses a feature's jiraId, if jira integration is activated"() {
        given: 'a feature with one tag comprising solely numbers'
        String gherkin = """
            @abc @123 @123abc
            Feature: test feature
            """
        and: 'jira integration is activated'
        parser = new GherkinParser(true)

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the number tag should be the parsed feature's jira id"
        feature.jiraId == '123'
    }

    void "does not parses a feature's jiraId, if jira is on"() {
        given: 'a feature with one tag comprising solely numbers'
        String gherkin = """
            @abc @123 @123abc
            Feature: test feature
        """
        and: 'jira integration is deactivated'
        parser = new GherkinParser(false)

        when: "parsing that feature"
        Feature feature = parser.parse(gherkin)

        then: "the feature should not have a jira id"
        feature.jiraId == null
    }

    void "parses a background's description"() {
        given: 'a background with a description'
        String gherkin = """
                Feature: test feature

                Background:
                This Background has a description
                that also spreads across two lines
                Given a feature's background
                """

        when: "parsing that background"
        Feature feature = parser.parse(gherkin)

        then: "the parsed background should have the description"
        feature.background.description == 'This Background has a description\nthat also spreads across two lines'
    }

    void "parses a background's steps"() {
        given: 'a background with steps'
        String gherkin = """
                Feature: test feature

                Background:
                This Background has a description
                that also spreads across two lines
                  Given a background's first step
                  And a second one
                  But not more than three
                """

        when: "parsing that background"
        Feature feature = parser.parse(gherkin)

        then: "the parsed background should have the steps"
        feature.background.steps.step == [
              'Given a background\'s first step',
              'And a second one',
              'But not more than three'
        ]
    }


    void "parses a scenario's description"() {
        given: 'a scenario with a description'
        String gherkin = """
                Feature: test feature

                Scenario: test scenario
                This scenario has a description
                that also spreads across two lines
                Given a feature's scenario
                """

        when: "parsing that scenario"
        Feature feature = parser.parse(gherkin)

        then: "the parsed scenario should have the description"
        feature.scenarios.first().description == 'This scenario has a description\nthat also spreads across two lines'
    }

    void "parses a scenario's steps"() {
        given: 'a scenario with steps'
        String gherkin = """
                Feature: test feature

                Scenario: test scenario
                  Given a scenario
                  And the scenario has steps
                  But it has no description
                  When that scenario is parsed
                  Then all steps should be contained
                  And they should be in the correct order
                  But all keywords must be supported
                """

        when: "parsing that scenario"
        Feature feature = parser.parse(gherkin)

        then: "the parsed scenario should have the steps"
        feature.scenarios.first().steps.step == [
              'Given a scenario',
              'And the scenario has steps',
              'But it has no description',
              'When that scenario is parsed',
              'Then all steps should be contained',
              'And they should be in the correct order',
              'But all keywords must be supported'
        ]
    }

    void "parses a scenario's tags"() {
        given: 'a scenario with tags'
        String gherkin = """
                Feature: test feature

                @tag1 @tag2
                @tag3
                Scenario: test scenario
                """

        when: "parsing that scenario"
        Feature feature = parser.parse(gherkin)

        then: "the parsed scenario should have the tags"
        feature.scenarios.first().tags.size() == 3
        feature.scenarios.first().tags.tag.containsAll(['tag1', 'tag2', 'tag3'])
    }

    void "parses a scenario outline"() {
        given: 'a feature with a scenario outline '
        String gherkin = """
            Feature: test feature

              @tag1 @tag2
              Scenario Outline: scenario outline
              This is the outline's description
                Given a scenario outline with <examples>
                When parsing that scenario outline
                Then it should be parsed

                Examples:
                  | head 1    | head 2    | head 3    |
                  | value 1.1 | value 1.2 | value 1.3 |
                  | value 2.1 | value 2.2 | value 2.3 |
            """

        when: 'parsing that feature'
        Feature feature = parser.parse(gherkin)

        then: 'the feature should have the schenario outline'
        Scenario scenario = feature.scenarios.first()
        scenario.title == 'scenario outline'
        scenario.outline
        scenario.tags.size() == 2
        scenario.tags.tag.containsAll(['tag1', 'tag2'])
        scenario.description == "This is the outline's description"
    }

    void "parses a scenario outline's examples"() {
        given: 'a scenario outline with examples'
        String gherkin = """
            Feature: test feature

              Scenario Outline: scenario outline
                Given a scenario outline with <examples>
                When parsing that scenario outline
                Then it should be parsed

                Examples:
                  | head 1    | head 2    | head 3    |
                  | value 1.1 | value 1.2 | value 1.3 |
                  | value 2.1 | value 2.2 | value 2.3 |
            """

        when: 'parsing that scenario outline'
        Feature feature = parser.parse(gherkin)

        then: 'the scenario outline should have the examples'
        Scenario scenario = feature.scenarios.first()
        scenario.examplesHeading == 'Examples'
        scenario.examplesTable.columnHeads == ['head 1', 'head 2', 'head 3']
        scenario.examplesTable.rows.size() == 2
        scenario.examplesTable.rows.first().tableCells == ['value 1.1', 'value 1.2', 'value 1.3']
        scenario.examplesTable.rows.last().tableCells == ['value 2.1', 'value 2.2', 'value 2.3']

    }

    void "parses a step with a one row data table"() {
        given: 'a step with a datatable comprising only one row'
        String gherkin = """
            Feature: test feature

            Scenario: test scenario
              Given a step with a one row data table:
                | value 1 | value 2 | value 3 |
        """

        when: 'parsing that step'
        Feature feature = parser.parse(gherkin)

        then: 'the parsed step should have a table containing a row but no column heads'
        Table table = feature.scenarios.first().steps.first().table
        table.columnHeads == null
        table.rows.size() == 1
        table.rows.first().tableCells == ['value 1', 'value 2', 'value 3']
    }

    void "parses a step with a multiple rows data table"() {
        given: 'a step with a datatable comprising multiple rows'
        String gherkin = """
            Feature: test feature

            Scenario: test scenario
              Given a step with a multiple rows data table:
                | head 1    | head 2    | head 3    |
                | value 1.1 | value 1.2 | value 1.3 |
                | value 2.1 | value 2.2 | value 2.3 |
        """

        when: 'parsing that step'
        Feature feature = parser.parse(gherkin)

        then: 'the parsed step should have a table containing a column heads an rows'
        Table table = feature.scenarios.first().steps.first().table
        table.columnHeads == ['head 1', 'head 2', 'head 3']
        table.rows.size() == 2
        table.rows.first().tableCells == ['value 1.1', 'value 1.2', 'value 1.3']
        table.rows.last().tableCells == ['value 2.1', 'value 2.2', 'value 2.3']
    }

    void "parses German features"() {
        given: 'a german feature description'
        String gherkin = """
            # language: de
            Funktionalität: deutsches Feature

            Grundlage:
              Angenommen diesesFeature hat Grundlagen
              Und die Grundlagen nutzen alle Schlüsselwörter
              Aber nur die, die bei den Grundlagen Sinn machen

            Szenario: deutsches Szenario
            Mit Kommentar
              Angenommen dieses Szenario ist auf Deutsch verfasst
              Und auch hier werden alles Schlüsselwörter verwendet
              Aber diesmal sind es mehr als in den Grundlagen
              Wenn das Feature eingelesen wird
              Dann soll es dieses Szenario enthalten

            Szenariogrundriss: deutscher Szenariogrundriss
              Angenommen ich komme aus <Land>
              Wenn ich Cucumber Szenarien schreibe
              Dann schreibe ich sie auf Deutsch
              Beispiele:
                | Land        |
                | Deutschland |
                | Östeerreich |
        """

        when: 'parsing that feature'
        Feature feature = parser.parse(gherkin)

        then: 'the parsed feature should have all the defined elements'
        feature.title == 'deutsches Feature'
        feature.background != null
        feature.background.steps.size() == 3
        feature.scenarios.size() == 2
        feature.scenarios.first().title == 'deutsches Szenario'
        feature.scenarios.first().description == 'Mit Kommentar'
        feature.scenarios.first().steps.size() == 5
        feature.scenarios.last().title == 'deutscher Szenariogrundriss'
        feature.scenarios.last().steps.size() == 3
        feature.scenarios.last().examplesHeading == 'Beispiele'
        feature.scenarios.last().examplesTable != null
    }
}
