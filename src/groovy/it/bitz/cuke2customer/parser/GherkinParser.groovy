package it.bitz.cuke2customer.parser

import gherkin.formatter.Formatter
import gherkin.formatter.model.DescribedStatement
import gherkin.formatter.model.Examples
import gherkin.formatter.model.Row
import gherkin.formatter.model.ScenarioOutline
import gherkin.formatter.model.TagStatement
import gherkin.parser.Parser
import it.bitz.cuke2customer.model.AbstractFeatureDescription
import it.bitz.cuke2customer.model.AbstractFeatureDescriptionWithSteps
import it.bitz.cuke2customer.model.Background
import it.bitz.cuke2customer.model.Feature
import it.bitz.cuke2customer.model.Scenario
import it.bitz.cuke2customer.model.Step
import it.bitz.cuke2customer.model.Table
import it.bitz.cuke2customer.model.TableRow
import it.bitz.cuke2customer.model.Tag

class GherkinParser {

    private Feature feature
    private boolean jiraIntegration

    GherkinParser (boolean jiraIntegration) {
        this.jiraIntegration = jiraIntegration
    }

    Feature parse(String gherkin) {
        feature = null
        new Parser(new FeatureParserFormatter()).parse(gherkin, '', 0)
        return feature
    }

    private class FeatureParserFormatter implements Formatter {

        AbstractFeatureDescriptionWithSteps currentStepScope

        @Override
        void feature(gherkin.formatter.model.Feature parsedFeature) {
            feature = new Feature()
            extractTitleDescriptionAndTags(feature, parsedFeature)
            if (jiraIntegration) {
                feature.jiraId = feature.tags.tag.find { it.isNumber() }
            }
        }

        @Override
        void background(gherkin.formatter.model.Background background) {
            feature.background = new Background()
            extractTitleDescriptionAndTags(feature.background, background)
            currentStepScope = feature.background
        }

        @Override
        void scenario(gherkin.formatter.model.Scenario parsedScenario) {
            parseAndAddNewScenario(parsedScenario)
        }

        @Override
        void scenarioOutline(ScenarioOutline scenarioOutline) {
            Scenario scenario = parseAndAddNewScenario(scenarioOutline)
            scenario.outline = true
        }

        private Scenario parseAndAddNewScenario(TagStatement scenarioOrOutline) {
            feature.scenarios = feature.scenarios ?: []
            Scenario scenario = new Scenario()
            extractTitleDescriptionAndTags(scenario, scenarioOrOutline)
            feature.scenarios += scenario
            currentStepScope = scenario
            return scenario
        }

        private void extractTitleDescriptionAndTags(AbstractFeatureDescription featureDescription, DescribedStatement describedStatement) {
            featureDescription.title = describedStatement.name
            featureDescription.description = trimEachLine(describedStatement.description)
            if(describedStatement instanceof TagStatement) {
                featureDescription.tags = describedStatement.tags.name.collect { new Tag(tag: it - '@') }
            }
        }

        @Override
        void examples(Examples examples) {
            if (currentStepScope instanceof Scenario) {
                currentStepScope.examplesHeading = examples.keyword
                currentStepScope.examplesTable = extractTable(examples.rows)
            }
        }

        private trimEachLine(String multipleLinesString) {
            return multipleLinesString.readLines().collect { it.trim() }.join('\n')
        }

        @Override
        void step(gherkin.formatter.model.Step parsedStep) {
            currentStepScope.steps = currentStepScope.steps ?: []
            Step step = new Step(step: parsedStep.keyword + parsedStep.name)
            currentStepScope.steps += step
            if(parsedStep.rows) {
                step.table = extractTable(parsedStep.rows)
            }
        }

        private Table extractTable(List<Row> tableRows) {
            if(tableRows.size() == 1) {
                return extractOneLineDataTable(tableRows)
            }
            return extractMultiLineTable(tableRows)
        }

        private Table extractOneLineDataTable(Iterable<Row> tableRows) {
            List<String> tableCells = extractDataTableRowCells(tableRows.first())
            return new Table(rows: [new TableRow(tableCells: tableCells)])
        }

        private Table extractMultiLineTable(List<Row> tableRows) {
            List<String> columnHeads = extractDataTableRowCells(tableRows.first())
            List<TableRow> rows = tableRows[1..-1].collect {Row dataTableRow ->
                new TableRow(tableCells: extractDataTableRowCells(dataTableRow))
            }
            return new Table (columnHeads: columnHeads, rows: rows)
        }

        private List<String> extractDataTableRowCells (Row tableRow) {
            List<String> tableCells = []
            tableCells += (tableRow.cells)
            return tableCells
        }

        @Override
        void done() {}

        @Override
        void startOfScenarioLifeCycle(gherkin.formatter.model.Scenario scenario) {}

        @Override
        void endOfScenarioLifeCycle(gherkin.formatter.model.Scenario scenario) {}

        @Override
        void close() {}

        @Override
        void eof() {}

        @Override
        void syntaxError(String s, String s2, List<String> strings, String s3, Integer integer) {}

        @Override
        void uri(String s) {}
    }

}
