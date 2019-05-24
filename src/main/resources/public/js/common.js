chinese = {
    lengthMenu: "每页显示 _MENU_ 条记录",
    // info: "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
    info: "共有 <span>_TOTAL_</span> 条数据",
    infoEmpty: "",
    infoFiltered: "(从 _MAX_ 条数据中检索)",
    zeroRecords: "没有检索到数据",
    sProcessing: "<div class=\"loader-inner ball-beat\"><div></div><div></div><div></div></div>" + "加载中...",
    search: "名称:",
    paginate: {
        sFirst: "&lt;&lt;",
        sPrevious: "&lt;",
        sNext: "&gt;",
        sLast: "&gt;&gt;"
    }
};
var pathname = location.pathname;
$(".sidebar-menu a").each(function () {
    var href = this.href;
    href = href.substring(href.length - pathname.length, href.length);
    if (href.indexOf(pathname) > -1){
        var parent = $(this).parent();
        parent.addClass("active");
        var ul = parent.parent();
        if (ul.hasClass("treeview-menu")){
            ul.parent().addClass("active");
        }
    } else {
        $(".sidebar-menu .header").addClass("active");
    }
});