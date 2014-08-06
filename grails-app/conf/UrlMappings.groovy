class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/" (view: "/index")
        "500" (view: '/error')

        "/search/$query"(controller: 'search', action: 'search')
    }
}
