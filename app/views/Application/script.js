(function(globals){
  "use strict";
  globals.routes = {
    javascripts:"@{'/public/javascripts'}",
    createIssue: "@{Issues.create()}",
    html2canvasURL: "@{'/public/javascripts/vendor/html2canvas.min.js'}"
  };
#{if common.webpack.WebpackPlugin.devserverEnabled}
  // spostiamoci su webpack-dev-server:
  if (window.location.port == 9000) {
    window.location.port = 8080;
  }
#{/if}
}(this));
