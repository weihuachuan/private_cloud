var express = require('express')
var app = express()
var bodyParser = require('body-parser')
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

var port = process.env.PORT || 81
var path = require('path');

//var moment = require('moment'); //定义在app.locals中的键值对可以在模板中使用

var handlebars = require('express-handlebars').create({
    partialsDir: './app/view/partials',
  //  layoutsDir: './app/views/layouts',
  /*  defaultLayout: 'layout',*/
    extname: '.hbs',
    helpers: {
      dir: function(context, options){
        //console.log("context:  "+context)
          var out="";
          var arr =context[0].key.split('/');

          for(var i=0;i<arr.length-1;i++)
          { console.log("context:  "+arr[i])
            out = out+"<li class=\"flag1\"><a href=\"#\">"+arr[i]+"</a></li>";
          } 
        
          return out;
      },
      resStr:function(context,options){
          var result={
            res:context.data.root.res
          };
          return JSON.stringify(result);
      }
   }
});

app.use('/public/upload', express.static(__dirname + '/public/upload'));
//app.use('/public/javascripts', express.static(__dirname + '/public/javascripts'));
app.use('/img', express.static(__dirname + '/public/img'));
app.use('/css', express.static(__dirname + '/public/css'));
app.use('/js', express.static(__dirname + '/public/js'));


app.use(bodyParser.urlencoded({extended: true}));
app.engine('.hbs', handlebars.engine);
app.set('view engine', 'hbs');
app.set('views','./app/view');



var cookieParser = require('cookie-parser');
var session = require('express-session');
app.use(cookieParser());
app.use(session({
  secret: 'jinnou',
  resave: false,
  saveUninitialized: true
}));

var route = require("./routes/route");
app.use('/', route);


app.listen(port);


console.log("成功启动"+port );



