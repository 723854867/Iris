({
    baseUrl: 'src/',
    dir:'js/',
    paths: {
        'jquery':"libs/jquery-2.1.3",
        'jqueryFace':"libs/jquery.qqFace",
        'head':"views/head"
    },
    modules:[
        {
            name:"views/head",
        },
        {
            name:"views/video_show",
        },
        {
            name:"views/active",
        },{
            name:"views/active_index",
        },{
            name:"views/active_list",
        }
    ]
})