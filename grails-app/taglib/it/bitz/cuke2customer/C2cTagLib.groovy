package it.bitz.cuke2customer

import it.bitz.cuke2customer.model.*
import it.bitz.cuke2customer.parser.GherkinParser
import it.bitz.cuke2customer.view.FeatureMenuRenderer

class C2cTagLib {

    static namespace = 'c2c'

    private Closure manualRenderer = { boolean shortOutput ->
        renderTagMetadataIcon ('hand', message(code: 'test.manual').toString(), shortOutput)
    }

    private Closure doneRenderer = {boolean shortOutput ->
        renderTagMetadataIcon ('check', message(code: 'done').toString(), shortOutput)
    }

    private Map<String, Closure> metadataRenderers = [
          'Done': doneRenderer,
          'done': doneRenderer,
          'manual': manualRenderer,
          'manuell': manualRenderer,
          'new': { boolean shortOutput ->
              renderTagMetadataIcon ('new', message(code: 'new').toString(), shortOutput)
          }
    ]

    private void renderTagMetadataIcon (String filename, String title, boolean shortOutput) {
        filename = "$filename${shortOutput ? '-small' : ''}.png"
        out << img (dir: 'images', file: filename, title: title)
    }

    def tagMetadataSymbolKey = {
        out << '<div class="tagMetadataSymbolKey lineOverScenarios">'
        String label = message(code: 'new').toString()
        renderTagMetadataIcon ('new', label, true)
        out << "= $label"
        label = message(code: 'done').toString()
        renderTagMetadataIcon ('check', label, true)
        out << "= $label"
        label = message(code: 'test.manual').toString()
        renderTagMetadataIcon ('hand', label, true)
        out << "= $label"
        out << '</div>'
    }

    def tags = { attrs, body ->
        Collection<Tag> tags = attrs.remove ('tags')
        out << "<p class=\"tags\">${tags*.tag.join (', ')}</p>"
    }

    def tagMetadata = { attrs, body ->
        AbstractFeatureDescription taggable = attrs.remove ('taggable')
        boolean shortOutput = attrs.remove ('short') ?: false
        taggable?.tags?.each {
            metadataRenderers[it.tag]?.call (shortOutput)
        }
    }

    def table = { attrs ->
        Table table = attrs.remove ('table')
        if (table) {
            renderTable (table)
        }
    }

    private renderTable (Table table) {
        out << '<table class="indented">'
        renderColumnHeads (table)
        renderTableBody (table)
        out << '</table>'
    }

    private renderColumnHeads (Table table) {
        final columnHeads = table.columnHeads
        if (columnHeads && !table.columnHeads.empty) {
            out << '<thead><tr>'
            columnHeads.each { head ->
                out << "<th style=\"padding: 0\"><div style=\"padding: .3em\">$head</div></th>"
            }
            out << '</tr></thead>'
        }
    }

    private renderTableBody (Table table) {
        List<TableRow> rows = table.rows
        if (rows && !rows.empty) {
            out << '<tbody>'
            rows.each { row ->
                out << '<tr>'
                row.tableCells.each { cellValue ->
                    out << "<td>$cellValue</td>"
                }
                out << '</tr>'
            }
            out << '</tbody>'
        }
    }

    def featureMenu = { attrs ->
        String featureDir = grailsApplication.config.project.feature.directory
        new FeatureMenuRenderer(out: out, createLink: createLink).renderFeatureMenu(featureDir)
    }

    def steps = { attrs ->
        List<Step> steps = attrs.remove ('steps')
        steps.each { step ->
            out << '<p class="steps indented">'
            out << assembleWordsOfStep (step)
            out << '</p>'
            if (step.table) {
                renderTable (step.table)
            }
        }
    }

    private String assembleWordsOfStep (Step step) {
        List<String> words = step.toString().split(' ')
        words.collect { word ->
            if (words.indexOf (word) == 0) {
                return "<span class=\"keyword\">$word</span>"
            }
            if (word.toString ().startsWith ('<') || word.toString ().endsWith ('>')) {
                return "<span class=\"placeHolder\">${word.toString ().encodeAsHTML ()}</span>"
            }
            return word.toString ()
        }.join (' ')
    }

    def description = { attrs ->
        String description = attrs.remove ('description')
        out << '<p class="description">'
        if (description) {
            out << description.replaceAll ('[\n\r]', '<br/>')
        }
        out << '</p>'
    }

    def jiraLinks = { attrs ->
        Feature feature = attrs.remove ('feature')
        List<Comment> comments = attrs.remove ('comments')
        if (feature.jiraId) {
            out << '<div class="jira">'
            out << '<div class="jiraLinks">'
            out << "<a href=\"#comments\">${comments.size ()} ${message(code: 'view.comments')}</a><br/>"
            out << "<a href=\"${jiraUrl (feature: feature)}\" target=\"_blank\">$feature.jiraId ${message(code: 'open.in.jira')}</a>"
            out << '</div>'
            out << '</div>'
        }
    }

    def jiraUrl = { attrs ->
        Feature feature = attrs.remove ('feature')
        out << jiraBrowseUrl + grailsApplication.config.project.jiraPrefix + feature.jiraId
    }

    /**
     * Uses the first Tag which is castable to an integer as JIRA number and generates a link for it.
     */
    def jiraUrlFromTags = { attrs ->
      List<Tag> tags = attrs.remove('tags')

      Tag jiraTag = tags?.find{it.tag.isInteger() }
      jiraTag ? out << "<a href=\"$jiraBrowseUrl$grailsApplication.config.project.jiraPrefix$jiraTag.tag\" target=\"_blank\">($grailsApplication.config.project.jiraPrefix$jiraTag.tag)</a>" : out <<""
    }

    private String getJiraBrowseUrl() {
        String jiraUrl = grailsApplication.config.project.jiraUrl
        if (!jiraUrl.endsWith('/')) {
            jiraUrl += '/'
        }
        return jiraUrl + 'browse/'
    }

    def searchResult = { attrs ->
        String featureString = attrs.remove('feature')
        Feature feature = new GherkinParser(false).parse(featureString)
        String featureLink = createLink(controller: 'feature', action: 'showFeature', params: [featureHashCode: featureString.hashCode()])
        out << "<p class=\"title\"><a href=\"${featureLink}\">$feature.title</a></p>"
    }
}
