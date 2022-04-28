var express = require('express');
var app = express();
var fs = require("fs");
var bodyParser = require('body-parser');
var request = require('request');
request = request.defaults({jar: true})
var serverURL = '154.8.230.175'; // 后端云盘服务器ip

app.use(bodyParser.urlencoded({ extended: false }));

app.post('/addUser', function (req, res) {
     
      var usr = req.body.userName;
      var psd = req.body.password;
      var role1 = "USER";
      var details = 'abc';

      var options1 = {
        url: 'http://'+serverURL+':9080/loginPost',
        form: {
            username:"Superadmin",
            password:"superadmin"
        }  
      };
      //1.登录管理员账号
      request.post(options1, function(error, response, body) {

        console.log("我是发往"+options1.url+"---"+body);
        console.log("我是发往"+options1.url+"---"+error);
        console.log(usr+"  "+psd+"  "+role1+"  "+details);
        var options = {
        url: 'http://'+serverURL+':9080/hos/v1/sys/user',
        form: {
            userName:usr,
            password:psd,
            role:role1,
            detail:details
            }  
        };
         console.log( JSON.stringify(options)   );
        //2.增加用户
        request.post(options, function(error, response, body) {
          console.log("我是发往"+options.url+"---"+JSON.stringify(response));
          console.log("我是发往"+options.url+"---"+body);
          console.log("我是发往"+options.url+"---"+error);
          res.json({ status: JSON.parse(body).code})
        })
      })
});


var server = app.listen(8081, function () {
  var host = server.address().address;
  var port = server.address().port;
  console.log("应用实例，访问地址为 http://%s:%s", host, port)
});