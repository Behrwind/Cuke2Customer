modules = {
    application {
        resource url:'js/application.js'
    }

    jQueryCollapsibleList {
        dependsOn 'jquery'
        resource url:'/css/jquery.collapsibleList.css', disposition: 'head'
        resource url:'/js/jquery.collapsibleList.js', disposition: 'head'
        resource url:'/js/md5.min.js', disposition: 'head'
    }

    spinner {
        resource url:'/js/spin.min.js', disposition: 'head'
    }
}